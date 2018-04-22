package it.polimi.ingsw.model;
import java.util.Random;

public class Dice {
    private Colour colour;
    private int num;

    public Dice(Colour color) {
        colour = color;
        num = rollDice();
    }


    //metodo invocato sia dal costruttore per assegnare un numero tra 1 e 6
    // ma anche nel caso in cui possa ritirare il dado
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
