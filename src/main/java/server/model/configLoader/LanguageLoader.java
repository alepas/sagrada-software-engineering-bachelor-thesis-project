package server.model.configLoader;

import org.json.simple.JSONObject;

import static server.constants.POCConstants.*;
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
        
        //POC
        String part1; String part2;
        
        POC1_NAME = (String) jsonLanguage.get("POC1_NAME");
        part1 = (String) jsonLanguage.get("POC1_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC1_DESCRIPTION_P2");
        POC1_DESCRIPTION = part1 + POC1_SCORE + part2;

        POC2_NAME = (String) jsonLanguage.get("POC2_NAME");
        part1 = (String) jsonLanguage.get("POC2_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC2_DESCRIPTION_P2");
        POC2_DESCRIPTION = part1 + POC2_SCORE + part2;

        POC3_NAME = (String) jsonLanguage.get("POC3_NAME");
        part1 = (String) jsonLanguage.get("POC3_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC3_DESCRIPTION_P2");
        POC3_DESCRIPTION = part1 + POC3_SCORE + part2;

        POC4_NAME = (String) jsonLanguage.get("POC4_NAME");
        part1 = (String) jsonLanguage.get("POC4_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC4_DESCRIPTION_P2");
        POC4_DESCRIPTION = part1 + POC4_SCORE + part2;

        POC5_NAME = (String) jsonLanguage.get("POC5_NAME");
        part1 = (String) jsonLanguage.get("POC5_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC5_DESCRIPTION_P2");
        POC5_DESCRIPTION = part1 + POC5_SCORE + part2;

        POC6_NAME = (String) jsonLanguage.get("POC6_NAME");
        part1 = (String) jsonLanguage.get("POC6_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC6_DESCRIPTION_P2");
        POC6_DESCRIPTION = part1 + POC6_SCORE + part2;

        POC7_NAME = (String) jsonLanguage.get("POC7_NAME");
        part1 = (String) jsonLanguage.get("POC7_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC7_DESCRIPTION_P2");
        POC7_DESCRIPTION = part1 + POC7_SCORE + part2;

        POC8_NAME = (String) jsonLanguage.get("POC8_NAME");
        part1 = (String) jsonLanguage.get("POC8_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC8_DESCRIPTION_P2");
        POC8_DESCRIPTION = part1 + POC8_SCORE + part2;

        POC9_NAME = (String) jsonLanguage.get("POC9_NAME");
        POC9_DESCRIPTION = (String) jsonLanguage.get("POC9_DESCRIPTION");

        POC10_NAME = (String) jsonLanguage.get("POC10_NAME");
        part1 = (String) jsonLanguage.get("POC10_DESCRIPTION_P1");
        part2 = (String) jsonLanguage.get("POC10_DESCRIPTION_P2");
        POC10_DESCRIPTION = part1 + POC10_SCORE + part2;
    }
}
