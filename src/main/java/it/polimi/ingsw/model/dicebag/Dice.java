package it.polimi.ingsw.model.dicebag;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {
    private Color color;
    private int number;
    private int id;


    public Dice(Color color, int diceId) {
        this.color = color;
        number = rollDice();
        this.id = diceId;
    }


    //metodo invocato sia dal costruttore per assegnare un numero tra 1 e 6
    // ma anche nel caso in cui possa ritirare il dado
    public int rollDice( ){
        Random random = new Random();
        number = random.nextInt(6) + 1;
        return number;
    }

    public Color getDiceColor( ){
        return color;
    }

    public int getDiceNumber( ){
        return number;
    }

    public int getId( ){ return id;}

    public void setNumber(int number) throws IncorrectNumberException {
        if (number < 0 || number > 6 )
            throw new IncorrectNumberException(number);
        this.number = number;
    }

    public ClientDice getClientDice(){
        return new ClientDice(Color.getClientColor(color), number, id);
    }

    private Dice(Color color, int number, int id) {
        this.color = color;
        this.number = number;
        this.id = id;
    }

    public Dice copyDice(){
        return new Dice(color,number,id);
    }

}
