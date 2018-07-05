package server.constants;

import org.json.simple.JSONObject;
import server.model.configLoader.ConfigLoader;

/**
 * This class contains all constants related to the class wpc
 */
public class WpcConstants {
    private static final JSONObject jsonServerObject = ConfigLoader.jsonServerObject;
    private static final JSONObject jsonWpc = (JSONObject) jsonServerObject.get("wpc");

    public static final int ROWS_NUMBER = ((Long) jsonWpc.get("ROWS_NUMBER")).intValue();
    public static final int COLS_NUMBER = ((Long) jsonWpc.get("COLS_NUMBER")).intValue();
}
