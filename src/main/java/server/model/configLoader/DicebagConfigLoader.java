package server.model.configLoader;

import org.json.simple.JSONObject;
import static server.constants.DiceBagCostants.*;

public class DicebagConfigLoader {

    public static void loadConfig(JSONObject jsonDiceBag) {
        INITIAL_DICE_NUMBER = ((Long) jsonDiceBag.get("INITIAL_DICE_NUMBER")).intValue();
        SOLO_PLAYER_DICES = ((Long) jsonDiceBag.get("SOLO_PLAYER_DICES")).intValue();
    }
}
