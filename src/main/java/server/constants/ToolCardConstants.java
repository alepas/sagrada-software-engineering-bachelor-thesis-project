package server.constants;

import org.json.simple.JSONObject;
import server.model.configLoader.ConfigLoader;

/**
 * This class contains all constants related to the toolcards;
 * for each card there are four constants:
 * - the id which is a numeric string
 * - the name given by the game
 * - the description which is used to explain to the player what can be done by activating it
 */
public class ToolCardConstants {
    private static final JSONObject jsonServerObject = ConfigLoader.jsonServerObject;
    private static final JSONObject jsonToolcard = (JSONObject) jsonServerObject.get("toolcard");

    //ToolCard1
    public static final String TOOLCARD1_ID = (String) jsonToolcard.get("TOOLCARD1_ID");

    //ToolCard2
    public static final String TOOLCARD2_ID = (String) jsonToolcard.get("TOOLCARD2_ID");

    //ToolCard3
    public static final String TOOLCARD3_ID = (String) jsonToolcard.get("TOOLCARD3_ID");

    //ToolCard4
    public static final String TOOLCARD4_ID = (String) jsonToolcard.get("TOOLCARD4_ID");

    //ToolCard5
    public static final String TOOLCARD5_ID = (String) jsonToolcard.get("TOOLCARD5_ID");

    //ToolCard6
    public static final String TOOLCARD6_ID = (String) jsonToolcard.get("TOOLCARD6_ID");

    //ToolCard7
    public static final String TOOLCARD7_ID = (String) jsonToolcard.get("TOOLCARD7_ID");

    //ToolCard8
    public static final String TOOLCARD8_ID = (String) jsonToolcard.get("TOOLCARD8_ID");

    //ToolCard9
    public static final String TOOLCARD9_ID = (String) jsonToolcard.get("TOOLCARD9_ID");

    //ToolCard10
    public static final String TOOLCARD10_ID = (String) jsonToolcard.get("TOOLCARD10_ID");

    //ToolCard11
    public static final String TOOLCARD11_ID = (String) jsonToolcard.get("TOOLCARD11_ID");

    //ToolCard12
    public static final String TOOLCARD12_ID = (String) jsonToolcard.get("TOOLCARD12_ID");
}
