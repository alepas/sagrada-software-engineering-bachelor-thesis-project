package server.model.configLoader;

import org.json.simple.JSONObject;
import static server.constants.ToolCardConstants.*;

public class ToolcardLoader {

    public static void loadConfig(JSONObject jsonToolcard) {
        TOOLCARD1_ID = (String) jsonToolcard.get("TOOLCARD1_ID");
        TOOLCARD2_ID = (String) jsonToolcard.get("TOOLCARD2_ID");
        TOOLCARD3_ID = (String) jsonToolcard.get("TOOLCARD3_ID");
        TOOLCARD4_ID = (String) jsonToolcard.get("TOOLCARD4_ID");
        TOOLCARD5_ID = (String) jsonToolcard.get("TOOLCARD5_ID");
        TOOLCARD6_ID = (String) jsonToolcard.get("TOOLCARD6_ID");
        TOOLCARD7_ID = (String) jsonToolcard.get("TOOLCARD7_ID");
        TOOLCARD8_ID = (String) jsonToolcard.get("TOOLCARD8_ID");
        TOOLCARD9_ID = (String) jsonToolcard.get("TOOLCARD9_ID");
        TOOLCARD10_ID = (String) jsonToolcard.get("TOOLCARD10_ID");
        TOOLCARD11_ID = (String) jsonToolcard.get("TOOLCARD11_ID");
        TOOLCARD12_ID = (String) jsonToolcard.get("TOOLCARD12_ID");
    }
}
