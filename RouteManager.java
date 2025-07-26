import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * This class primarily does the calculation of
 * routes between devices. The actions will be based
 * on the devices added to a particular route.
 * The devices added here should be a subset of the ones
 * added to the NetworkDeviceManager. You shouldn't add
 * a device to the RouteManager if they aren't in the
 * NetworkDeviceManager. 
 */

public class RouteManager {
    private final NetworkDeviceManager networkDeviceManager;
    private final Map<String, Set<String>> routes;
    private final LoggingManager logger;

    public RouteManager(NetworkDeviceManager networkDeviceManager, LoggingManager logger) {
        this.networkDeviceManager = networkDeviceManager;
        this.routes = new HashMap<>();
        this.logger = logger;

        // Initialize routes for all devices in NetworkDeviceManager
        for (NetworkDevice device : networkDeviceManager.getDevices()) {
            routes.put(device.getDeviceId(), new HashSet<>());
        }
    }

    public void addDevice(NetworkDevice device) {
        if (!networkDeviceManager.getDevices().contains(device)) {
            throw new IllegalArgumentException("Device not found in NetworkDeviceManager: " + device);
        }
        routes.putIfAbsent(device.getDeviceId(), new HashSet<>());
        logger.logEvent(Level.INFO, "Device added to RouteManager: " + device.getDeviceId());
    }

    public void addRoute(NetworkDevice source, NetworkDevice destination, int weight) {
        if (!networkDeviceManager.getDevices().contains(source) || !networkDeviceManager.getDevices().contains(destination)) {
            throw new IllegalArgumentException("Source or destination not found in NetworkDeviceManager: ");
        }
        // Adds bidirectional route between source and destination
        routes.get(source.getDeviceId()).add(destination.getDeviceId());
        routes.get(destination.getDeviceId()).add(source.getDeviceId());
        logger.logEvent(Level.INFO, "Route added: " + source.getDeviceId() + " <-> " + destination.getDeviceId());
    }
    
    // Retrieves the optimal route between the source and destination devices
    public List<NetworkDevice> getOptimalRoute(NetworkDevice source, NetworkDevice destination) {
        if (!networkDeviceManager.getDevices().contains(source) || !networkDeviceManager.getDevices().contains(destination)) {
            logger.logEvent(Level.WARNING, "Source or destination not found in NetworkDeviceManager");
            return Collections.emptyList();
        }

        List<String> routeIds = calculateOptimalRoute(source.getDeviceId(), destination.getDeviceId());
        if (routeIds.isEmpty()) {
            logger.logEvent(Level.INFO, "No route found between " + source.getDeviceId() + " and " + destination.getDeviceId());
            return Collections.emptyList();
        }

        List<NetworkDevice> route = new ArrayList<>();
        for (String deviceId : routeIds) {
            for (NetworkDevice device : networkDeviceManager.getDevices()) {
                if (device.getDeviceId().equals(deviceId)) {
                    route.add(device);
                    break;
                }
            }
        }

        logger.logEvent(Level.INFO, "Optimal route found between " + source.getDeviceId() + " and " + destination.getDeviceId());
        return route;
    }

     // Calculates the optimal route between the source and destination devices
    private List<String> calculateOptimalRoute(String sourceId, String destinationId) {
        if (!isRegisteredDevice(sourceId) || !isRegisteredDevice(destinationId)) {
            return Collections.emptyList();
        }

        Map<String, String> previousDevice = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(sourceId);
        visited.add(sourceId);

        while(!queue.isEmpty()) {
            String current = queue.poll(); // Dequeues the current device

            if (current.equals(destinationId)) {
                return reconstructPath(previousDevice, sourceId, destinationId);
            }
            // Explore the neighbours of the current device
            for (String neighbour : routes.get(current)) {
                if(!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    previousDevice.put(neighbour, current);
                    queue.add(neighbour);
                }
            }
        }

        return Collections.emptyList();
    }

    // File reader to load the connections from the text file
    public void loadConnectionsFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String source = parts[0].trim();
                    String destination = parts[1].trim();

                    if (isRegisteredDevice(source) && isRegisteredDevice(destination)) {
                        addBidirectionalPath(source, destination);
                        logger.logEvent(Level.INFO, "Connection added: " + source + " <-> " + destination);
                        
                    } else {
                        logger.logEvent(Level.WARNING, "Skipping connection with unregistered device: " + source + " <-> " + destination);
                    }
                }
            }
            logger.logEvent(Level.INFO, "Connections loaded successfully");
        } catch (FileNotFoundException e) {
            throw new IOException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filePath, e);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid connection format: " + e.getMessage());
        }
    }

    // Checks to make sure the device is valid and is stored in NetworkDeviceManager
    private boolean isRegisteredDevice(String deviceId) {
        for (NetworkDevice device : networkDeviceManager.getDevices()) {
            if (device.getDeviceId().equals(deviceId)) {
                return true;
            }
        }
        return false;
    }

    private void addBidirectionalPath(String source, String destination) {
        routes.get(source).add(destination);
        routes.get(destination).add(source);
    }

    // Reconstructs the path from the source to the destination
    private List<String> reconstructPath(Map<String, String> previousDevice, String sourceId, String destinationId) {
        LinkedList<String> path = new LinkedList<>();
        String current = destinationId;
        // Backtracks from the destination to the source
        while (current != null) {
            path.addFirst(current);
            current = previousDevice.get(current);
        }

        return path;
    }
}
