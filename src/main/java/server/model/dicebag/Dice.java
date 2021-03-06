package server.model.dicebag;

import shared.clientinfo.ClientDice;
import shared.exceptions.dicebagexceptions.IncorrectNumberException;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {
    private Color color;
    private int number;
    private int id;


    /**
     * Creates the new dice object, color and id are given by the parameters, number is chosen in a random way.
     *
     * @param color is the color of the new object
     * @param diceId is the new object id
     */
    public Dice(Color color, int diceId) {
        this.color = color;
        number = rollDice();
        this.id = diceId;
    }

    /**
     * this constructor has been made because of a tool card which wants the user choose the the number of the new dice
     * @param color is the new object's color
     * @param number is the new object's number
     * @param id is the new object's id
     */
    Dice(Color color, int number, int id) {
        this.color = color;
        this.number = number;
        this.id = id;
    }


    /**
     * @return the number of the new object
     */
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

    /**
     * Sets the number of the dice.
     *
     * @param number is the new object's number
     * @throws IncorrectNumberException if the number is not in the range in should be in
     */
    public void setNumber(int number) throws IncorrectNumberException {
        if (number < 0 || number > 6 )
            throw new IncorrectNumberException(number);
        this.number = number;
    }

    /**
     * @return a clientDice equals to this
     */
    public ClientDice getClientDice(){
        return new ClientDice(Color.getClientColor(color), number, id);
    }


    /**
     * @return a copy of this
     */
    public Dice copyDice(){ return new Dice(color,number,id); }


    /**
     * Changes the dice's number as if the player is turning the dice. If dice number is 1 it will be turn in 6,
     * if it is 2 it will be turn in 5, etc etc
     */
    public void turnDiceOppositeSide(){
        switch (this.number){
            case 1:
                this.number = 6;
                break;
            case 2:
                this.number = 5;
                break;
            case 3:
                this.number = 4;
                break;
            case 4:
                this.number = 3;
                break;
            case 5:
                this.number = 2;
                break;
            case 6:
                this.number = 1;
                break;
        }
    }

}
