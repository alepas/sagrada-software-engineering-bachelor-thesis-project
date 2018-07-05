package server.constants;

import org.json.simple.JSONObject;
import server.model.configLoader.ConfigLoader;

/**
 * This class contains all constants related to the public objective cards;
 * for each card there are four constants:
 * - the id which is a numeric string
 * - the amount of points that will be assigned to the player each time he/she will be able to reach the goal
 * - the name
 * - the description which is used to explain to the player what to do to gain the POC points
 */
public class POCConstants {
    private static final JSONObject jsonServerObject = ConfigLoader.jsonServerObject;
    private static final JSONObject jsonPoc = (JSONObject) jsonServerObject.get("poc");

    //PublicObjectiveCard1
    public static final String POC1_ID = (String) jsonPoc.get("POC1_ID");
    public static final int POC1_SCORE = ((Long) jsonPoc.get("POC1_SCORE")).intValue();

    //PublicObjectiveCard2
    public static final String POC2_ID = (String) jsonPoc.get("POC2_ID");
    public static final int POC2_SCORE = ((Long) jsonPoc.get("POC2_SCORE")).intValue();

    //PublicObjectiveCard3
    public static final String POC3_ID = (String) jsonPoc.get("POC3_ID");
    public static final int POC3_SCORE = ((Long) jsonPoc.get("POC3_SCORE")).intValue();

    //PublicObjectiveCard4
    public static final String POC4_ID = (String) jsonPoc.get("POC4_ID");
    public static final int POC4_SCORE = ((Long) jsonPoc.get("POC4_SCORE")).intValue();

    //PublicObjectiveCard5
    public static final String POC5_ID = (String) jsonPoc.get("POC5_ID");
    public static final int POC5_SCORE = ((Long) jsonPoc.get("POC5_SCORE")).intValue();

    //PublicObjectiveCard6
    public static final String POC6_ID = (String) jsonPoc.get("POC6_ID");
    public static final int POC6_SCORE = ((Long) jsonPoc.get("POC6_SCORE")).intValue();

    //PublicObjectiveCard7
    public static final String POC7_ID = (String) jsonPoc.get("POC7_ID");
    public static final int POC7_SCORE = ((Long) jsonPoc.get("POC7_SCORE")).intValue();

    //PublicObjectiveCard8
    public static final String POC8_ID = (String) jsonPoc.get("POC8_ID");
    public static final int POC8_SCORE = ((Long) jsonPoc.get("POC8_SCORE")).intValue();

    //PublicObjectiveCard9
    public static final String POC9_ID = (String) jsonPoc.get("POC9_ID");
    public static final int POC9_SCORE = ((Long) jsonPoc.get("POC9_SCORE")).intValue();

    //PublicObjectiveCard10
    public static final String POC10_ID = (String) jsonPoc.get("POC10_ID");
    public static final int POC10_SCORE = ((Long) jsonPoc.get("POC10_SCORE")).intValue();
}
