import java.util.*;
import java.util.logging.*;
import java.io.*;

/**
 * This is the primary class of the system.
 * It will be used to launch the system and perform
 * the necessary actions, like adding devices,
 * removing devices, getting optimal route between
 * devices, filtering and searching for devices,
 * creating alerts etc. 
 * NOTE: DO NOT MOVE THIS CLASS TO ANY PACKAGE.
 *
 */

public class NMS{

    public static void main(String[] args){

        LoggingManager loggingManager = new LoggingManager();
        NetworkDeviceManager deviceManager = new NetworkDeviceManager(loggingManager);
       
        // Check if there are 4 arguments
        if (args.length != 4) {
            System.err.println("Enter in the format: NMS <devicesFilePath> <connectionsFilePath> <sourceId> <destinationId>");
            loggingManager.logEvent(Level.SEVERE, "Invalid number of arguments");
            System.exit(1); // Exit with error code 1
        }
        // Parsing each argument inputted
        String devicesFilePath = args[0];
        String connectionsFilePath = args[1];
        String startDeviceId = args[2];
        String endDeviceId = args[3];

        

        try {
            // Loading the devices and connections from the files
            deviceManager.loadDevicesFromFile(devicesFilePath);
            deviceManager.getRouteManager().loadConnectionsFromFile(connectionsFilePath);
            loggingManager.logEvent(Level.INFO, "Devices and connections loaded successfully");
        } catch (IOException e) {
            // Logs the error if file loading fails
            loggingManager.logEvent(Level.SEVERE, "Error loading files: " + e.getMessage());
            System.exit(1);
        }
        // Initializing the devices
        NetworkDevice startDevice = null;
        NetworkDevice endDevice = null;

        for (NetworkDevice device : deviceManager.getDevices()) {
            if (device.getDeviceId().equals(startDeviceId)) {
                startDevice = device;
                loggingManager.logEvent(Level.INFO, "Start device found: " + startDevice);
                break;
            }
        }
        for (NetworkDevice device : deviceManager.getDevices()) {
            if (device.getDeviceId().equals(endDeviceId)) {
                endDevice = device;
                loggingManager.logEvent(Level.INFO, "End device found: " + endDevice);
                break;
            }
        }

        if (startDevice == null || endDevice == null) {
            throw new IllegalArgumentException("Start or end device not found");
        }
        // Calculates the optimal route between the devices
        List<NetworkDevice> optimalRoute = deviceManager.getRouteManager().getOptimalRoute(startDevice, endDevice);
        
        // Checks if the route is empty
        if (optimalRoute.isEmpty()) {
            System.out.println("No route found from " + startDeviceId + " to " + endDeviceId);
        } else {
            System.out.println("Optimal route from " + startDeviceId + " to " + endDeviceId + ":");
            
            // Builds the route for output 
            StringBuilder route = new StringBuilder();
            for (int i = 0; i < optimalRoute.size(); i++) {
                route.append(optimalRoute.get(i).getDeviceId());
                if (i < optimalRoute.size() - 1) {
                    route.append(" <-> ");
                }
            }
            System.out.println(route.toString());
            loggingManager.logEvent(Level.INFO, "Optimal route found");
        }
    }
}