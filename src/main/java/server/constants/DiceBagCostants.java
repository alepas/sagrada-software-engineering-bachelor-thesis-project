package server.constants;

import org.json.simple.JSONObject;
import server.model.configLoader.ConfigLoader;

/**
 * This class contains all constants related to the class DiceBag
 */
public class DiceBagCostants {
    private static final JSONObject jsonServerObject = ConfigLoader.jsonServerObject;
    private static final JSONObject jsonDiceBag = (JSONObject) jsonServerObject.get("dicebag");

    public static final int INITIAL_DICE_NUMBER = ((Long) jsonDiceBag.get("INITIAL_DICE_NUMBER")).intValue();
    public static final int SOLO_PLAYER_DICES = ((Long) jsonDiceBag.get("SOLO_PLAYER_DICES")).intValue();
}
