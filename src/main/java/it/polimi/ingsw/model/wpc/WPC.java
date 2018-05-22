package it.polimi.ingsw.model.wpc;
import java.util.ArrayList;

import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

import it.polimi.ingsw.model.exceptions.wpcExceptions.NotExistingCellException;

import static it.polimi.ingsw.model.constants.WpcConstants.COLS_NUMBER;
import static it.polimi.ingsw.model.constants.WpcConstants.ROWS_NUMBER;


public class WPC {
    private  String wpcID;
    private  int favours;
    public ArrayList<Cell> schema = new ArrayList<>();


    WPC(String wpcID, int favours, ArrayList<Cell> schema) {
        this.wpcID = wpcID;
        this.favours = favours;
        for(Cell cell: schema)
            this.schema.add(new Cell(cell));
    }


    public WPC copyWpc(){
        return new WPC(wpcID,favours,schema);
    }

    public ClientWpc getClientWpc(){
        ArrayList<ClientCell> cells = new ArrayList<>();
        for(Cell cell : schema){
            Dice dice = cell.getCellDice();
            ClientDice clientDice;
            if (dice != null) clientDice = dice.getClientDice();
            else clientDice = null;

            cells.add(new ClientCell(clientDice, Color.getClientColor(cell.getCellColor()),
                    cell.getCellNumber(), cell.getCellPosition().getClientPosition()));
        }
        return new ClientWpc(wpcID, favours, cells);
    }

    public int getFavours(){ return favours; }

    public ArrayList<Cell> getSchema() { return schema; }

    public String getWpcID(){ return wpcID; }

    public boolean addDiceWithAllRestrictions(Dice dice, Position pos, int globalTurn) {
        Cell cell=getCellFromPosition(pos);
        if(cell.getCellDice()!=null)
            return false;
        if (globalTurn == 1) {
            if (checkFirstTurnRestriction(cell)&&checkAdjacentRestriction(cell,dice)&&checkCellRestriction(cell,dice))
                return true;
        }
        else if (checkCellRestriction(cell, dice) && checkAdjacentRestriction(cell, dice) && isThereAtLeastADiceNear(cell))
        {
            cell.setDice(dice);
            return true;
        }
        return false;
    }

    public boolean addDicePersonalizedRestrictions(Dice dice, Position pos, int globalTurn, boolean ColorRestr, boolean ValueRestr, boolean PlacementRestr, boolean atLeastADiceNear){
        Cell cell=getCellFromPosition(pos);
        boolean condition=true;

        if (ColorRestr==true){
            condition&=checkOnlyColorCellRestriction(cell,dice);
            if (condition==false)
                return false;}
        if (ValueRestr==true){
            condition&=checkOnlyNumberCellRestriction(cell,dice);
        if (condition==false)
            return false;}
        if (PlacementRestr==true){
            if (globalTurn == 1) {
                if (checkFirstTurnRestriction(cell)==false)
                    return false;
            }
            condition&= (checkAdjacentRestriction(cell, dice));
            if (condition==false)
                return false;
        }

        if (atLeastADiceNear==true){
            condition&=isThereAtLeastADiceNear(cell);
            if (condition==false)
                return false;
        }
        cell.setDice(dice);
        return true;
    }


    public Dice removeDice(Position position){
        Dice dice=null;
        Cell cell=getCellFromPosition(position);
        dice=cell.getCellDice();
        cell.removeDice();
        return dice;
    }


    public Dice getDiceFromPosition(Position pos){
        return getCellFromPosition(pos).getCellDice();
    }

    public Position getPositionFromDice(int diceID){
        for(Cell cell: schema) {
            if (cell.getCellDice().getDiceID() == diceID)
                return cell.getCellPosition();
        }
        return null;
    }

    private Cell getCellFromPosition(Position pos){
        return schema.get(pos.getRow()*WpcConstants.COLS_NUMBER+pos.getColumn());
    }

    private void checkCellExistence(Cell cell) throws NotExistingCellException {
        //controllo se la cella esiste
        for (Cell schemaCell : schema){
            if (schemaCell.equals(cell)) return;
        }

        throw new NotExistingCellException(cell);
    }


    private boolean checkFirstTurnRestriction(Cell cell) {
        //controllo che, durante il primo turno, il dado sia posizionato solo sul bordo della wpc
        int row = cell.getCellPosition().getRow();
        int column = cell.getCellPosition().getColumn();
        return row == 0 || column == 0 || row == ROWS_NUMBER - 1 || column == COLS_NUMBER - 1;
    }


    private boolean checkCellRestriction(Cell cell, Dice dice){
        //controllo che il dado possa essere inserito nella cella selezionata secondo le restrizioni di questa
        if (cell.getCellDice()== null) {
            if (cell.getCellNumber() == 0 && cell.getCellColor()!=null)
                return (cell.getCellColor().equals(dice.getDiceColor()));
            else if (cell.getCellColor()== null && cell.getCellNumber()== 0)
                return true;
            else
                return (cell.getCellNumber() == dice.getDiceNumber());
        }
        else
            return false;
    }


    private boolean checkOnlyNumberCellRestriction(Cell cell, Dice dice){
        //pennello per eglomise: impone che sia considerata sulla cella solo la restrizione di numero e non quella di colore
        if (cell.getCellDice()!= null)
            return false;

        return cell.getCellNumber() == dice.getDiceNumber();
    }


    private boolean checkOnlyColorCellRestriction(Cell cell, Dice dice){
        //Alesatore per lamine di rame: impone che sia considerata sulla cella solo la restrizione di colore e non quella di numero
        if (cell.getCellDice()!= null)
            return false;

        return cell.getCellColor().equals(dice.getDiceColor());
    }


    private boolean checkAdjacentRestriction(Cell cell, Dice dice){
        //controllo se le celle adiacenti hanno dadi con numero o colore uguali a quelli del dado che si desidera inserire
        int row= cell.getCellPosition().getRow();
        int column= cell.getCellPosition().getColumn();

        for(Cell schemaCell: this.schema) {
            if (isAnAdjacentCell(schemaCell, row, column) && cell.getCellDice()!= null)
                return checkDiceEquivalence(schemaCell.getCellDice(), dice);
        }
        return true;
    }


    private boolean isAnAdjacentCell(Cell cell, int row, int column){
        //verifico se la cella è adiacente a quella sotto esame
        int adjacentRow = cell.getCellPosition().getRow();
        int adjacentColumn = cell.getCellPosition().getColumn();
        return (adjacentRow == row && (adjacentColumn == (column + 1) || adjacentColumn == (column - 1)))|| (adjacentColumn == column && (adjacentRow== row-1 || adjacentRow == row+1));

    }

    //manca il controllo che ci sia almeno un dado attorno alla cella selezionata
    private boolean isThereAtLeastADiceNear(Cell cell) {
        int row= cell.getCellPosition().getRow();
        int column= cell.getCellPosition().getColumn();

        for(Cell schemaCell: this.schema) {
            if (isAnAdjacentCell(schemaCell, row, column) && cell.getCellDice()!= null)
               return true;
        }
        return false;
    }


    private boolean checkDiceEquivalence(Dice cellDice, Dice dice){
        //controlla se il dado della cella adiacente ha colore o numero uguale  a quello del dado che si desidera inserire
        return dice.getDiceNumber() == cellDice.getDiceNumber() || dice.getDiceColor().equals(cellDice.getDiceColor());
    }


    public ArrayList<Dice> getRowDices(int row){
        //restituisce tutti i dadi presenti in una riga
        ArrayList<Dice> rowDices = new ArrayList<>();

        for(Cell cell: schema){
            if (cell.getCellPosition().getRow() == row && cell.getCellDice() != null)
                rowDices.add(cell.getCellDice());
        }

        return rowDices;
    }


    public ArrayList<Dice> getColDices(int column){
        //restituisce tutti i dadi presenti in una colonna
        ArrayList<Dice> columnDices = new ArrayList<>();

        for(Cell cell: schema) {
            if (cell.getCellPosition().getColumn() == column && cell.getCellDice() != null)
                columnDices.add(cell.getCellDice());
        }

        return columnDices;
    }


    public ArrayList<Dice> getWpcDices(){
        //restituisce tutti i dadi presenti sulla wpc
        ArrayList<Dice>  WPCDices = new ArrayList<>();

        for(Cell cell: schema){
            if(cell.getCellDice()!= null) WPCDices.add(cell.getCellDice());
        }

        return WPCDices;
    }


    public int numDicesOfShade(int shade){
        //Restituisce il numero di dadi sulla wpc che hanno il numero uguale a shade
        int count = 0;

        for (Dice dice : getWpcDices()){
            if (dice.getDiceNumber() == shade) count++;
        }

        return count;
    }


    public int numDicesOfColor(Color color){
        //Restituisce il numero di dadi sulla wpc che hanno il colore uguale a color
        int count = 0;

        for (Dice dice : getWpcDices()){
            if (dice.getDiceColor().equals(color)) count++;
        }

        return count;
    }


    public int getNumFreeCells(){
        //restituisce il numero di celle vuote sulla wpc
        int count = 0;

        for (Cell cell : schema){
            if (cell.getCellDice() == null) count++;
        }

        return count;
    }
}
