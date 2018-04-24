package it.polimi.ingsw.model.WPC;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

public class Cell {
    private Dice cellDice;
    private Color cellColor;
    private int cellNum;
    private Position position;

    Cell(Color color, Position position){
        cellDice = null;
        cellColor = color;
        cellNum = 0;
        this.position = position;
    }

    Cell( int n, Position position){
        cellDice = null;
        cellColor = null;
        cellNum = n;
        this.position = position;
    }

    Cell(Position position){
        cellDice = null;
        cellColor = null;
        cellNum = 0;
        this.position = position;
    }

    public Color getCellColor( ){
        return cellColor;
    }

    public int getCellNumber( ) {
        return cellNum;
    }

    public Dice getCellDice( ){
        return cellDice;
    }

    public Position getCellPosition( ){
        return position;
    }

    public void setDice( Dice addedDice){
        cellDice = addedDice;
    }

    //metodo chiamato nel momento in cui voglio spostare un dado dalla sua cella
    //usato sia quando voglio spostare dado da una cella all'altra sia quando
    //voglio sostiurlo a uno nal roundtrack
    public Dice removeDice ( ){
        Dice removedDice = getCellDice();
        cellDice = null;
        return removedDice;
    }
}
