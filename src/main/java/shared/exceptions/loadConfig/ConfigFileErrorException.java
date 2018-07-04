package shared.exceptions.loadConfig;

public class ConfigFileErrorException extends Exception{
    private int cause;

    public ConfigFileErrorException(int errorID) {
        cause=errorID;
    }

    @Override
    public String getMessage() {
        if (cause == 1) return "Cannot get resource from Jar file.";
        return "Cannot copy out of the jar the current resource.";
    }

    public int getErrorId() {
        return cause;
    }
}
