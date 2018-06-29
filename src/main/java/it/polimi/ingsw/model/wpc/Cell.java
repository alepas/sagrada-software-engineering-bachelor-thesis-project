package it.polimi.ingsw.model.wpc;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

public class Cell {
    private Dice dice;
    private Color color;
    private int number;
    private Position position;

    /**
     * Sets all attributes of the new object
     * @param position is the position of the new object cell
     * @param color is the color related to the new object
     * @param number is the number related to the new object
     */
    Cell(Position position, Color color, int number){
        dice = null;
        this.color = color;
        this.number = number;
        this.position = position;
    }

    Cell(Cell cell){
        dice = cell.dice;
        this.color = cell.getColor();
        this.number = cell.getNumber();
        this.position = cell.getCellPosition();
    }

    public Color getColor( ){
        return color;
    }

    public int getNumber( ) {
        return number;
    }

    public Dice getDice( ){ return dice; }

    public Position getCellPosition( ){
        return position;
    }

    public void setDice( Dice addedDice){
        dice = addedDice;
    }

    /**
     * removes the dice from the object.
     *
     * @return the dice removed in a position
     */
    Dice removeDice(){
        Dice removedDice = this.getDice();
        this.dice = null;
        return removedDice;
    }
}
