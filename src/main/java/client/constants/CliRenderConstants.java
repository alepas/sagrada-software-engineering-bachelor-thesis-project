package client.constants;

import client.configLoader.ClientConfigLoader;
import org.json.simple.JSONObject;

/**
 * COntains all constants related to the cliRender class
 */
public class CliRenderConstants {
    private static final JSONObject jsonClientObject = ClientConfigLoader.jsonClientObject;
    private static final JSONObject jsonLanguage = ClientConfigLoader.jsonClientLanguage;

    public static final int CELL_HEIGHT = ((Long) jsonClientObject.get("CELL_HEIGHT")).intValue();
    public static final int DICE_DISTANCE = ((Long) jsonClientObject.get("DICE_DISTANCE")).intValue();
    public static final int DICE_LENGHT = ((Long) jsonClientObject.get("DICE_LENGHT")).intValue();

    public static final String FAVOURS = (String) jsonLanguage.get("FAVOURS");

    //Colors
    // Reset
    public static final String RESET = "\u001b" + jsonLanguage.get("RESET");

    // Regular Colors
    public static final String BLACK = "\u001b" + jsonLanguage.get("BLACK");
    public static final String BLACK_BRIGHT = "\u001b" + jsonLanguage.get("BLACK_BRIGHT");
    public static final String RED = "\u001b" + jsonLanguage.get("RED");
    public static final String GREEN = "\u001b" + jsonLanguage.get("GREEN");
    public static final String YELLOW = "\u001b" + jsonLanguage.get("YELLOW");
    public static final String BLUE = "\u001b" + jsonLanguage.get("BLUE");
    public static final String PURPLE = "\u001b" + jsonLanguage.get("PURPLE");

    // Background
    public static final String RED_BACKGROUND = "\u001b" + jsonLanguage.get("RED_BACKGROUND");
    public static final String GREEN_BACKGROUND = "\u001b" + jsonLanguage.get("GREEN_BACKGROUND");
    public static final String YELLOW_BACKGROUND = "\u001b" + jsonLanguage.get("YELLOW_BACKGROUND");
    public static final String BLUE_BACKGROUND = "\u001b" + jsonLanguage.get("BLUE_BACKGROUND");
    public static final String PURPLE_BACKGROUND = "\u001b" + jsonLanguage.get("PURPLE_BACKGROUND");
}
