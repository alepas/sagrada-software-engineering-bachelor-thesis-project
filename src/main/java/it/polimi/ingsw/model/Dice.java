package it.polimi.ingsw.model;
import java.util.Random;

public class Dice {
    private Colour colour;
    private int num;

    private void Dice( Colour col){
        colour = col;
        num = rollDice();
    }

    //quando sposto un dado
    public int rollDice( ){
        Random random = new Random();
        num = random.nextInt(5) + 1;
        return num;
    }

    public Colour getDiceColour( ){
        return colour;
    }

    public int getDiceNumber( ){
        return num;
    }


}
