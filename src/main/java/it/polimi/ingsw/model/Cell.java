package it.polimi.ingsw.model;

public class Cell {
    private Dice dice;
    private Colour colour;
    private int num;
    private Position position;

    private  void Cell( Colour color, Position pos){
        dice = null;
        colour = color;
        num = 0;
        position = pos;
    }

    private void Cell( int n, Position pos){
        dice = null;
        colour = null;
        num = n;
        position = pos;
    }

    private void Cell(Position pos){
        dice = null;
        colour = null;
        num = 0;
        position = pos;
    }

    public Colour getCellColour( ){
        return colour;
    }

    public int getCellNumber( ) {
        return num;
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

    public Dice removeDice ( ){  //sbagliato mi ritorna null
        dice = null;
        return dice;
    }
}
