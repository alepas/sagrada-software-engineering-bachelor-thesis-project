package server.model.configLoader;

import org.json.simple.JSONObject;
import static shared.constants.WpcConstants.*;

public class WpcConfigLoader {

    public static void loadConfig(JSONObject jsonWpc) {
        ROWS_NUMBER = ((Long) jsonWpc.get("ROWS_NUMBER")).intValue();
        COLS_NUMBER = ((Long) jsonWpc.get("COLS_NUMBER")).intValue();
    }
}
