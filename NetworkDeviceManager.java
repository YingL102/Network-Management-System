import java.util.*;
import java.io.*;
import java.util.logging.Level;

/**
 * This class acts as a manager class through which
 * the devices represented by NetworkDevice shall be
 * maintained. This class shall allow adding and
 * removing of devices, and also set configuration
 * for each device added to it. Any other class should use
 * this class to get the latest set of devices maintained
 * by the system.
 * Note: The list of devices should not be held in any
 * other class.
 */

public class NetworkDeviceManager {

    private final Map<String, NetworkDevice> devices;
    private final RouteManager routeManager;
    private final LoggingManager logger;

    public NetworkDeviceManager(LoggingManager logger) {
        devices = new HashMap<>();
        this.logger = logger;
        this.routeManager = new RouteManager(this, logger);
    }

    public void addDevice(NetworkDevice device) {
        if (device == null || device.getDeviceId() == null) {
            throw new IllegalArgumentException("Device or device id cannot be null");
        }
        devices.put(device.getDeviceId(), device);
        logger.logEvent(Level.INFO, "Device added:" + device);
        routeManager.addDevice(device); // Stores the device in RouteManager
    }

    public void removeDevice(String deviceId) {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device id cannot be null");
        }
        if (devices.isEmpty()) {
            throw new IllegalArgumentException("No devices to remove");
        }
        NetworkDevice device = devices.remove(deviceId); // Removes the device from the map
        logger.logEvent(Level.INFO, "Device removed:" + device);
    }

    public void configureDevice(String deviceId, DeviceConfiguration config) {
        if (deviceId == null || config == null) {
            throw new IllegalArgumentException("Device id or configuration cannot be null");
        }
        NetworkDevice device = devices.get(deviceId);
        if (device == null) {
            throw new IllegalArgumentException("Device not found:" + deviceId);
        }
        device.setConfiguration(config);
        logger.logEvent(Level.INFO, "Device configured:" + device);
    }

    // Parses the configuration as a string into a DeviceConfiguration object
    private DeviceConfiguration parseConfiguration (String configuration) {
        DeviceConfiguration config = new DeviceConfiguration();
        
        if (!configuration.isEmpty()) {
            // Removing the "Config:{" and "}" from the configuration string and splitting the entries
            String configEntries = configuration.replace("Config:{", "").replace("}", "").trim();
            String[] entries = configEntries.split(";");

            for (String entry : entries) {
                String[] parts = entry.split("=", 2);
                if (parts.length == 2) {
                    config.setConfiguration(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return config;
    }

    // File reader to load the devices from the text file
    public void loadDevicesFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid device format: " + line);
                }

                String deviceId = parts[0].trim();
                String deviceType = parts[1].trim();
                String configuration = parts.length == 3 ? parts[2].trim() : "";

                NetworkDevice device = new NetworkDevice(deviceId, deviceType);
                DeviceConfiguration config = parseConfiguration(configuration);

                device.setConfiguration(config);
                addDevice(device);
            }
        logger.logEvent(Level.INFO, "Devices loaded from file:" + filePath);
        } catch (FileNotFoundException e) {
            throw new IOException("File not found:" + filePath, e);
        } catch (IOException e) {
            throw new IOException("Error reading file:" + filePath, e);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid device format:" + e.getMessage());
        }
    }
    
     // Retrieves the list of devices
     public List<NetworkDevice> getDevices() {
        return new ArrayList<>(devices.values());
    }

    public RouteManager getRouteManager() {
        return routeManager;
    }
}