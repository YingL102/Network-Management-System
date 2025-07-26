# NMS: Network Management System

## Overview
This project implements a simple Network Management System (NMS) that manages network devices, their configurations, and routes between them. The system uses Java’s I/O and core data structures alongside Java’s logging facilities. It is designed to:

- Load device information from a file
- Parse device configurations
- Load network connections from a file
- Calculate the optimal route between two devices
- Log system events for debugging and evaluation

## Project Structure
```
/src
  ├── NMS.java                  # Main class that launches the system
  ├── NetworkDeviceManager.java # Manages network devices (load, add, remove, configure)
  ├── RouteManager.java         # Manages device routes and computes optimal route
  ├── NetworkDevice.java        # Represents a network device with id, type, and configuration
  ├── DeviceConfiguration.java  # Stores configuration properties for a device
  └── LoggingManager.java       # Handles logging to console and optionally to file
```

## Prerequisites
- Java JDK (version 8 or above)
- A text editor or an IDE (e.g., Visual Studio Code)

## How to Compile
Open a terminal, navigate to the `src` folder, and run:
```sh
javac *.java
```

## Input Files
### devices.txt
Each line should have the following format:
```
<deviceId>,<deviceType>,Config:{key1=value1;key2=value2;...}
```
**Example:**
```
PC1,Computer,Config:{OS=Windows;RAM=16GB}
R1,Router,Config:{Model=Cisco}
SW1,Switch,Config:{Ports=24}
```

### connections.txt
Each line specifies a connection (bidirectional) between two devices:
```
<sourceDeviceId>, <destinationDeviceId>
```
**Example:**
```
PC1,R1
R1,SW1
SW1,PC2
```

## How to Run
After compiling, execute the project with the following command:
```sh
java -cp . NMS <devicesFilePath> <connectionsFilePath> <sourceDeviceId> <destinationDeviceId>
```

**Example:**
```sh
java -cp . NMS /tmp/devices.txt /tmp/connections.txt PC1 PC2
```

The system will load the devices and connections from the provided files, compute the optimal route according to the connections, and print the route to standard output.

## Logging
The `LoggingManager` logs events using Java’s built-in logging framework. By default, log messages are printed to the console. To enable file logging, call the `setLogFile` method with a file path.

## Additional Notes
- **Error Handling:** The program will exit if an invalid file format is encountered or if required files are missing.
- **Route Calculation:** The optimal route is computed based on the connectivity defined in `connections.txt`. Adjust the routing algorithm if needed.
- **Extensibility:** Further enhancements may include more complex configuration parsing, better route optimization, or a GUI interface.

## Contact
For further queries or contributions, please contact the developer or file an issue.
