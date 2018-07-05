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
    public static final String RESET = "\033" + jsonLanguage.get("RESET");

    // Regular Colors
    public static final String BLACK = "\033" + jsonLanguage.get("BLACK");
    public static final String BLACK_BRIGHT = "\033" + jsonLanguage.get("BLACK_BRIGHT");
    public static final String RED = "\033" + jsonLanguage.get("RED");
    public static final String GREEN = "\033" + jsonLanguage.get("GREEN");
    public static final String YELLOW = "\033" + jsonLanguage.get("YELLOW");
    public static final String BLUE = "\033" + jsonLanguage.get("BLUE");
    public static final String PURPLE = "\033" + jsonLanguage.get("PURPLE");

    // Background
    public static final String RED_BACKGROUND = "\033" + jsonLanguage.get("RED_BACKGROUND");
    public static final String GREEN_BACKGROUND = "\033" + jsonLanguage.get("GREEN_BACKGROUND");
    public static final String YELLOW_BACKGROUND = "\033" + jsonLanguage.get("YELLOW_BACKGROUND");
    public static final String BLUE_BACKGROUND = "\033" + jsonLanguage.get("BLUE_BACKGROUND");
    public static final String PURPLE_BACKGROUND = "\033" + jsonLanguage.get("PURPLE_BACKGROUND");
}
