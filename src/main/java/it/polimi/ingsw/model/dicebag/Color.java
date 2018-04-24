package it.polimi.ingsw.model.dicebag;
import java.util.Random;

public enum Color {
    VIOLET, BLUE, RED, YELLOW, GREEN;
    //violet = 0; blue = 1; red = 2; yellow = 3; green = 4

    public static Color randomColor(){
        Random randomColor = new Random ();
        return values()[randomColor.nextInt(4)];
    }
}