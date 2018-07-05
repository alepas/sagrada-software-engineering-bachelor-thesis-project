package server.model.dicebag;
import shared.clientinfo.ClientColor;

import java.io.Serializable;
import java.util.Random;

public enum Color implements Serializable {
    VIOLET, BLUE, RED, YELLOW, GREEN;

    /**
     * Chooses in a randomize way a color.
     *
     * @return a color
     */
    public static Color randomColor(){
        Random randomColor = new Random ();
        return values()[randomColor.nextInt(5)];
    }

    /**
     * swtich case constructor that associate to the string an enum's color.
     *
     * @param stringColor is the string coming from the the XML parser
     * @return the enum's color associated to the XML string
     */
    public static Color parseColor(String stringColor) {
        Color color;
        switch (stringColor){
            case "violet":
                color = VIOLET;
                break;
            case "blue":
                color = BLUE;
                break;
            case "green":
                color = GREEN;
                break;
            case "red":
                color = RED;
                break;
            case "yellow":
                color = YELLOW;
                break;
            default:
                color = null;
                break;
        }
        return color;
    }

    /**
     * @param color is the color of an element
     * @return the client color associated to the color
     */
    public static ClientColor getClientColor(Color color){
        if (color == null) return null;
        switch (color){
            case VIOLET:
                return ClientColor.VIOLET;
            case BLUE:
                return ClientColor.BLUE;
            case YELLOW:
                return ClientColor.YELLOW;
            case RED:
                return ClientColor.RED;
            case GREEN:
                return ClientColor.GREEN;
            default:
                return null;
        }
    }

    /**
     * @param color is the client color
     * @return the color associated to the client one
     */
    public static Color getColorFromClientColor(ClientColor color){
        if (color == null) return null;
        switch (color){
            case VIOLET:
                return Color.VIOLET;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case RED:
                return Color.RED;
            case GREEN:
                return Color.GREEN;
            default:
                return null;
        }
    }
}
