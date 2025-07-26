public class NetworkDevice {
    private String deviceId;
    private String deviceType;
    private DeviceConfiguration configuration;

    public NetworkDevice(String deviceId, String deviceType) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public NetworkDevice(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public DeviceConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new DeviceConfiguration();
        }
        return configuration;
    }

    public void setConfiguration(DeviceConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return String.format("Device[id=%s, type=%s, config=%s]", 
        deviceId, deviceType, configuration != null ? configuration.toString() : "none");
    }
}
