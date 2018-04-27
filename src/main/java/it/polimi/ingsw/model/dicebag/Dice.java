package it.polimi.ingsw.model.dicebag;

import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;

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

    public void setNumber(int number) throws IncorrectNumberException {
        if (number < 0 || number > 6 )
            throw new IncorrectNumberException(number);
        this.number = number;
    }

}
