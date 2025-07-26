import java.util.logging.*;
import java.io.IOException;

/**
 * This class does the logging of the actions performed by the
 * system. 
 */

public class LoggingManager {
    private Logger logger;

    public LoggingManager() {
        logger = Logger.getLogger(getClass().getName());
        logger.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
    }

    public void logEvent(Level level, String message) {
        logger.log(level, message);
    }
    // Creates a file handler for the logger to log messages to a file
    public void setLogFile(String fileName) throws IOException {
        FileHandler fileHandler = new FileHandler(fileName, true);
        fileHandler.setLevel(Level.ALL);
        logger.addHandler(fileHandler);
    }
}
