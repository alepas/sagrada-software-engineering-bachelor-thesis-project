package it.polimi.ingsw.model.dicebag;

import java.util.Random;

public class Dice {
    private Color color;
    private int number;

    public Dice(Color color) {
        this.color = color;
        number = rollDice();
    }

    //metodo invocato sia dal costruttore per assegnare un numero tra 1 e 6
    // ma anche nel caso in cui possa ritirare il dado
    int rollDice( ){
        Random random = new Random();
        number = random.nextInt(5) + 1;
        return number;
    }

    public Color getDiceColor( ){
        return color;
    }

    public int getDiceNumber( ){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }

}
