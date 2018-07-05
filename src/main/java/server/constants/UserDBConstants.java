package server.constants;

import org.json.simple.JSONObject;
import server.model.configLoader.ConfigLoader;

/**
 * This class contains all constants related to the UserDB class
 */
public class UserDBConstants {
    private static final JSONObject jsonServerObject = ConfigLoader.jsonServerObject;
    private static final JSONObject jsonSettings = (JSONObject) jsonServerObject.get("settings");

    private static final String PATH_DB_FILE = (String) jsonSettings.get("userdb_path");

    public static String getPathDbFile() {
        return PATH_DB_FILE;
    }
}
