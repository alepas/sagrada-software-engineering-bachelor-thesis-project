package it.polimi.ingsw.model.dicebag;
import it.polimi.ingsw.model.clientModel.ClientColor;

import java.io.Serializable;
import java.util.Random;

public enum Color implements Serializable {
    VIOLET, BLUE, RED, YELLOW, GREEN;
    //violet = 0; blue = 1; red = 2; yellow = 3; green = 4

    public static Color randomColor(){
        Random randomColor = new Random ();
        return values()[randomColor.nextInt(4)];
    }

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
        }
        return null;
    }

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
        }
        return null;
    }
}
