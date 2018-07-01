package server.constants;

/**
 * This class contains all constants related to the UserDB class
 */
public class UserDBConstants {
    private UserDBConstants(){}

    private static final String PATH_DB_FILE = "./store.db";

    public static String getPathDbFile() {
        return PATH_DB_FILE;
    }
}
