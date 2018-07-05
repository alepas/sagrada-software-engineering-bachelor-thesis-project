package shared.constants;

import org.json.simple.JSONObject;
import shared.configLoader.SharedConfigLoader;

/**
 * This class contains all constants related to the public obiective cards
 */
public class PocConstants {
    private static final JSONObject jsonLanguage = SharedConfigLoader.jsonSharedLanguage;

    public static final String POC1_NAME = (String) jsonLanguage.get("POC1_NAME");
    public static final String POC1_DESCRIPTION = (String) jsonLanguage.get("POC1_DESCRIPTION");

    public static final String POC2_NAME = (String) jsonLanguage.get("POC2_NAME");
    public static final String POC2_DESCRIPTION = (String) jsonLanguage.get("POC2_DESCRIPTION");

    public static final String POC3_NAME = (String) jsonLanguage.get("POC3_NAME");
    public static final String POC3_DESCRIPTION = (String) jsonLanguage.get("POC3_DESCRIPTION");

    public static final String POC4_NAME = (String) jsonLanguage.get("POC4_NAME");
    public static final String POC4_DESCRIPTION = (String) jsonLanguage.get("POC4_DESCRIPTION");

    public static final String POC5_NAME = (String) jsonLanguage.get("POC5_NAME");
    public static final String POC5_DESCRIPTION = (String) jsonLanguage.get("POC5_DESCRIPTION");

    public static final String POC6_NAME = (String) jsonLanguage.get("POC6_NAME");
    public static final String POC6_DESCRIPTION = (String) jsonLanguage.get("POC6_DESCRIPTION");

    public static final String POC7_NAME = (String) jsonLanguage.get("POC7_NAME");
    public static final String POC7_DESCRIPTION = (String) jsonLanguage.get("POC7_DESCRIPTION");

    public static final String POC8_NAME = (String) jsonLanguage.get("POC8_NAME");
    public static final String POC8_DESCRIPTION = (String) jsonLanguage.get("POC8_DESCRIPTION");

    public static final String POC9_NAME = (String) jsonLanguage.get("POC9_NAME");
    public static final String POC9_DESCRIPTION = (String) jsonLanguage.get("POC9_DESCRIPTION");

    public static final String POC10_NAME = (String) jsonLanguage.get("POC10_NAME");
    public static final String POC10_DESCRIPTION = (String) jsonLanguage.get("POC10_DESCRIPTION");
}
