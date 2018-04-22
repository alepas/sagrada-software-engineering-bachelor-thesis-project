package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Dice;

public class Cell {
    private Dice cellDice;
    private Colour cellColour;
    private int cellNum;
    private Position position;

    private  void Cell( Colour color, Position pos){
        cellDice = null;
        cellColour = color;
        cellNum = 0;
        position = pos;
    }

    private void Cell( int n, Position pos){
        cellDice = null;
        cellColour = null;
        cellNum = n;
        position = pos;
    }

    private void Cell(Position pos){
        cellDice = null;
        cellColour = null;
        cellNum = 0;
        position = pos;
    }

    public Colour getCellColour( ){
        return cellColour;
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
    public Dice removeDice ( ){  //sbagliato mi ritorna null
        Colour color = cellDice.getDiceColour();
        int num = cellDice.getDiceNumber();
        Dice removedDice = new Dice(color, num);
        cellDice = null;
        return removedDice;
    }
}
