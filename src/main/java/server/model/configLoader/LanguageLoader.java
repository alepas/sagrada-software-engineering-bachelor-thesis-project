package server.model.configLoader;

import org.json.simple.JSONObject;
import static server.constants.ToolCardConstants.*;

public class LanguageLoader {

    public static void loadConfig(JSONObject jsonLanguage){
        //TOOLCARDS
        TOOL1_NAME = (String) jsonLanguage.get("TOOL1_NAME");
        TOOL1_DESCRIPTION = (String) jsonLanguage.get("TOOL1_DESCRIPTION");
        TOOL2_NAME = (String) jsonLanguage.get("TOOL2_NAME");
        TOOL2_DESCRIPTION = (String) jsonLanguage.get("TOOL2_DESCRIPTION");
        TOOL3_NAME = (String) jsonLanguage.get("TOOL3_NAME");
        TOOL3_DESCRIPTION = (String) jsonLanguage.get("TOOL3_DESCRIPTION");
        TOOL4_NAME = (String) jsonLanguage.get("TOOL4_NAME");
        TOOL4_DESCRIPTION = (String) jsonLanguage.get("TOOL4_DESCRIPTION");
        TOOL5_NAME = (String) jsonLanguage.get("TOOL5_NAME");
        TOOL5_DESCRIPTION = (String) jsonLanguage.get("TOOL5_DESCRIPTION");
        TOOL6_NAME = (String) jsonLanguage.get("TOOL6_NAME");
        TOOL6_DESCRIPTION = (String) jsonLanguage.get("TOOL6_DESCRIPTION");
        TOOL7_NAME = (String) jsonLanguage.get("TOOL7_NAME");
        TOOL7_DESCRIPTION = (String) jsonLanguage.get("TOOL7_DESCRIPTION");
        TOOL8_NAME = (String) jsonLanguage.get("TOOL8_NAME");
        TOOL8_DESCRIPTION = (String) jsonLanguage.get("TOOL8_DESCRIPTION");
        TOOL9_NAME = (String) jsonLanguage.get("TOOL9_NAME");
        TOOL9_DESCRIPTION = (String) jsonLanguage.get("TOOL9_DESCRIPTION");
        TOOL10_NAME = (String) jsonLanguage.get("TOOL10_NAME");
        TOOL10_DESCRIPTION = (String) jsonLanguage.get("TOOL10_DESCRIPTION");
        TOOL11_NAME = (String) jsonLanguage.get("TOOL11_NAME");
        TOOL11_DESCRIPTION = (String) jsonLanguage.get("TOOL11_DESCRIPTION");
        TOOL12_NAME = (String) jsonLanguage.get("TOOL12_NAME");
        TOOL12_DESCRIPTION = (String) jsonLanguage.get("TOOL12_DESCRIPTION");
    }
}
