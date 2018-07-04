package shared.exceptions.loadConfig;

import static shared.constants.ExceptionConstants.DATABASE_FILE_ERROR_0;
import static shared.constants.ExceptionConstants.DATABASE_FILE_ERROR_1;

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
