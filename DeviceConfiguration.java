import java.util.HashMap;
import java.util.Map;

public class DeviceConfiguration {
    private Map<String, String> config;

    public DeviceConfiguration() {
        config = new HashMap<>();
    }
    // Sets the configuration for the device
    public void setConfiguration(String key, String value) {
        config.put(key, value);
    }

    public String getConfigurations(String key) {
        return config.get(key);
    }

    @Override
    public String toString() {
        return config.toString();
    }
}
