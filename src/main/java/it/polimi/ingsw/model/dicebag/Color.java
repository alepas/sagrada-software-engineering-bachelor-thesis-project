package it.polimi.ingsw.model.dicebag;
import java.util.Random;

public enum Color {
    VIOLET, BLUE, RED, YELLOW, GREEN;
    //violet = 0; blue = 1; red = 2; yellow = 3; green = 4

    public static Color randomColor(){
        Random randomColor = new Random ();
        return values()[randomColor.nextInt(4)];
    }

    public static Color parseColor(String stringColor) {
        Color color = null;
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
            case "null":
                break;
        }
        return color;
    }

}
