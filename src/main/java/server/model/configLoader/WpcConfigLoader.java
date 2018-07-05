package server.model.configLoader;

import org.json.simple.JSONObject;

import static server.constants.WpcConstants.COLS_NUMBER;
import static server.constants.WpcConstants.ROWS_NUMBER;

public class WpcConfigLoader {

    public static void loadConfig(JSONObject jsonWpc) {
        ROWS_NUMBER = ((Long) jsonWpc.get("ROWS_NUMBER")).intValue();
        COLS_NUMBER = ((Long) jsonWpc.get("COLS_NUMBER")).intValue();
    }
}
