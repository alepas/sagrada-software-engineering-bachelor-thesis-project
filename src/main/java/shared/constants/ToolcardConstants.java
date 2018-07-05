package shared.constants;


import org.json.simple.JSONObject;
import shared.configLoader.SharedConfigLoader;

/**
 * This class contains all constants related to the tool cards
 */
public class ToolcardConstants {
    private static final JSONObject jsonLanguage = SharedConfigLoader.jsonSharedLanguage;

    //ToolCard1
    public static final String TOOL1_NAME = (String) jsonLanguage.get("TOOL1_NAME");;
    public static final String TOOL1_DESCRIPTION = (String) jsonLanguage.get("TOOL1_DESCRIPTION");;

    //ToolCard2
    public static final String TOOL2_NAME = (String) jsonLanguage.get("TOOL2_NAME");;
    public static final String TOOL2_DESCRIPTION = (String) jsonLanguage.get("TOOL2_DESCRIPTION");;

    //ToolCard3
    public static final String TOOL3_NAME = (String) jsonLanguage.get("TOOL3_NAME");;
    public static final String TOOL3_DESCRIPTION = (String) jsonLanguage.get("TOOL3_DESCRIPTION");;

    //ToolCard4
    public static final String TOOL4_NAME = (String) jsonLanguage.get("TOOL4_NAME");;
    public static final String TOOL4_DESCRIPTION = (String) jsonLanguage.get("TOOL4_DESCRIPTION");;

    //ToolCard5
    public static final String TOOL5_NAME = (String) jsonLanguage.get("TOOL5_NAME");;
    public static final String TOOL5_DESCRIPTION = (String) jsonLanguage.get("TOOL5_DESCRIPTION");;

    //ToolCard6
    public static final String TOOL6_NAME = (String) jsonLanguage.get("TOOL6_NAME");;
    public static final String TOOL6_DESCRIPTION = (String) jsonLanguage.get("TOOL6_DESCRIPTION");;

    //ToolCard7
    public static final String TOOL7_NAME = (String) jsonLanguage.get("TOOL7_NAME");;
    public static final String TOOL7_DESCRIPTION = (String) jsonLanguage.get("TOOL7_DESCRIPTION");;

    //ToolCard8
    public static final String TOOL8_NAME = (String) jsonLanguage.get("TOOL8_NAME");
    public static final String TOOL8_DESCRIPTION = (String) jsonLanguage.get("TOOL8_DESCRIPTION");

    //ToolCard9
    public static final String TOOL9_NAME = (String) jsonLanguage.get("TOOL9_NAME");
    public static final String TOOL9_DESCRIPTION = (String) jsonLanguage.get("TOOL9_DESCRIPTION");

    //ToolCard10
    public static final String TOOL10_NAME = (String) jsonLanguage.get("TOOL10_NAME");
    public static final String TOOL10_DESCRIPTION = (String) jsonLanguage.get("TOOL10_DESCRIPTION");

    //ToolCard11
    public static final String TOOL11_NAME = (String) jsonLanguage.get("TOOL11_NAME");
    public static final String TOOL11_DESCRIPTION = (String) jsonLanguage.get("TOOL11_DESCRIPTION");

    //ToolCard12
    public static final String TOOL12_NAME = (String) jsonLanguage.get("TOOL12_NAME");
    public static final String TOOL12_DESCRIPTION = (String) jsonLanguage.get("TOOL12_DESCRIPTION");
    
    public static final String[] TOOL_NAMES = {TOOL1_NAME, TOOL2_NAME, TOOL3_NAME, TOOL4_NAME, TOOL5_NAME, TOOL6_NAME,
            TOOL7_NAME, TOOL8_NAME, TOOL1_NAME, TOOL9_NAME, TOOL10_NAME, TOOL11_NAME, TOOL12_NAME };
    public static final String[] TOOL_DESCRIPTIONS = {TOOL1_DESCRIPTION, TOOL2_DESCRIPTION, TOOL3_DESCRIPTION, TOOL4_DESCRIPTION, TOOL5_DESCRIPTION, TOOL6_DESCRIPTION,
            TOOL7_DESCRIPTION, TOOL8_DESCRIPTION, TOOL1_DESCRIPTION, TOOL9_DESCRIPTION, TOOL10_DESCRIPTION, TOOL11_DESCRIPTION, TOOL12_DESCRIPTION };

}
