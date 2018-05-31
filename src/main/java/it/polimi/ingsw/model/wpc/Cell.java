package it.polimi.ingsw.model.wpc;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

public class Cell {
    private Dice dice;
    private Color color;
    private int number;
    private Position position;

    Cell(Position position, Color color, int number){
        dice = null;
        this.color = color;
        this.number = number;
        this.position = position;
    }

    Cell(Cell cell){
        dice = null;
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

    public Dice getDice( ){
        return dice;
    }

    public Position getCellPosition( ){
        return position;
    }

    public void setDice( Dice addedDice){
        dice = addedDice;
    }

    //metodo chiamato nel momento in cui voglio spostare un dado dalla sua cella
    //usato sia quando voglio spostare dado da una cella all'altra sia quando
    //voglio sostiurlo a uno nal roundtrack
    public Dice removeDice ( ){
        Dice removedDice = getDice();
        dice = null;
        return removedDice;
    }
}
