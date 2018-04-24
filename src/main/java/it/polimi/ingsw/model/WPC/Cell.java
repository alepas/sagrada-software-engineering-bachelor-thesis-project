package it.polimi.ingsw.model.WPC;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

public class Cell {
    private Dice cellDice;
    private Color cellColor;
    private int cellNum;
    private Position position;

    private Cell(Color color, Position pos){
        cellDice = null;
        cellColor = color;
        cellNum = 0;
        position = pos;
    }

    private Cell( int n, Position pos){
        cellDice = null;
        cellColor = null;
        cellNum = n;
        position = pos;
    }

    private Cell(Position pos){
        cellDice = null;
        cellColor = null;
        cellNum = 0;
        position = pos;
    }

    public Color getCellColor( ){
        return cellColor;
    }

    public int getCellNumber( ) {
        return cellNum;
    }

    public Dice getDice( ){
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
        Color color = cellDice.getDiceColour();
        int num = cellDice.getDiceNumber();
        Dice removedDice = new Dice(color, num);
        cellDice = null;
        return removedDice;
    }
}
