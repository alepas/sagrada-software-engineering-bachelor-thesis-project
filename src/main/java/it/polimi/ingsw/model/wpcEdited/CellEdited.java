package it.polimi.ingsw.model.wpcEdited;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

public class CellEdited {
    private Dice cellDice;
    private Color cellColor;
    private int cellNumber;
    private PositionEdited position;

    CellEdited(PositionEdited position, Color cellColor, int cellNumber){
        cellDice = null;
        this.cellColor = cellColor;
        this.cellNumber = cellNumber;
        this.position = position;
    }

    public Color getCellColor( ){
        return cellColor;
    }

    public int getCellNumber( ) {
        return cellNumber;
    }

    public Dice getCellDice( ){
        return cellDice;
    }

    public PositionEdited getCellPosition( ){
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
