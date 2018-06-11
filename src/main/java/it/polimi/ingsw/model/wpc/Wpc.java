package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.clientModel.ClientCell;
import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientWpc;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.wpcExceptions.NotExistingCellException;

import java.util.ArrayList;

import static it.polimi.ingsw.model.constants.WpcConstants.COLS_NUMBER;
import static it.polimi.ingsw.model.constants.WpcConstants.ROWS_NUMBER;


public class Wpc {
    private  String id;
    private  int favours;
    public ArrayList<Cell> schema = new ArrayList<>();
    boolean firstDicePutted=false;
    boolean onlyFirstDice=false;


    Wpc(String id, int favours, ArrayList<Cell> schema) {
        this.id = id;
        this.favours = favours;
        for(Cell cell: schema)
            this.schema.add(new Cell(cell));
    }


    public Wpc copyWpc(){
        return new Wpc(id,favours,schema);
    }



    public ClientWpc getClientWpc(){
        ArrayList<ClientCell> cells = new ArrayList<>();
        for(Cell cell : schema){
            Dice dice = cell.getDice();
            ClientDice clientDice;
            if (dice != null) clientDice = dice.getClientDice();
            else clientDice = null;

            cells.add(new ClientCell(clientDice, Color.getClientColor(cell.getColor()),
                    cell.getNumber(), cell.getCellPosition()));
        }
        return new ClientWpc(id, favours, cells);
    }

    public int getFavours(){ return favours; }

    public ArrayList<Cell> getSchema() { return schema; }

    public String getId(){ return id; }


    public boolean isDicePlaceable(Dice dice) {
        Position pos;
        for (Cell cell : schema) {
            if (cell.getDice() == null) {
                pos = cell.getCellPosition();
                if (!firstDicePutted) {
                    if (checkFirstTurnRestriction(cell) && checkCellRestriction(cell, dice)) {
                        return true;
                    }
                } else {
                    ArrayList<Cell> orthoCells = getOrthogonallyAdjacentCells(pos);
                    ArrayList<Cell> diagCells = getDiagonallyAdjacentCells(pos);
                    if (checkCellRestriction(cell, dice)
                            && checkAdjacentDiceRestriction(orthoCells, dice)
                            && isThereAtLeastADiceNear(orthoCells, diagCells)) {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public boolean addDiceWithAllRestrictions(Dice dice, Position pos) {
        Cell cell=getCellFromPosition(pos);
        if (cell==null)
            return false;
        if(cell.getDice()!=null)
            return false;
        if (!firstDicePutted) {
            if (checkFirstTurnRestriction(cell)&&checkCellRestriction(cell,dice)) {
                cell.setDice(dice);
                firstDicePutted=true;
                onlyFirstDice=true;
                return true;
            }
        } else {
            ArrayList<Cell> orthoCells=getOrthogonallyAdjacentCells(pos);
            ArrayList<Cell> diagCells=getDiagonallyAdjacentCells(pos);

            if (checkCellRestriction(cell, dice)
                    && checkAdjacentDiceRestriction(orthoCells, dice)
                    && isThereAtLeastADiceNear(orthoCells,diagCells)) {
                cell.setDice(dice);
                onlyFirstDice=false;
                return true;
            }
        }
        return false;
    }


    public boolean addDicePersonalizedRestrictions(Dice dice, Position pos, boolean ColorRestr, boolean ValueRestr, boolean PlacementRestr, boolean atLeastADiceNear, boolean noDicesNear){
        Cell cell=getCellFromPosition(pos);
        boolean condition=true;

        if (!firstDicePutted) {
            if (!checkFirstTurnRestriction(cell)) {
                return false;
            }
        }

        if ((ColorRestr)&&(!checkOnlyColorCellRestriction(cell, dice)))
            return false;

        if ((ValueRestr)&&(!checkOnlyNumberCellRestriction(cell,dice)))
            return false;

        ArrayList<Cell> orthoCells=getOrthogonallyAdjacentCells(pos);
        ArrayList<Cell> diagCells=getDiagonallyAdjacentCells(pos);

        if ((PlacementRestr)&&(!checkAdjacentDiceRestriction(orthoCells, dice)))
                return false;

        if (atLeastADiceNear){
            if (firstDicePutted&&!isThereAtLeastADiceNear(orthoCells,diagCells))
                return false;
        }

        if ((noDicesNear)&&(isThereAtLeastADiceNear(orthoCells,diagCells)))
                return false;

        cell.setDice(dice);
        if (onlyFirstDice==true)
            onlyFirstDice=false;
        if (!firstDicePutted) {
            firstDicePutted = true;
            onlyFirstDice=true;
        }

        return true;
    }


    public Dice removeDice(Position position){
        Dice dice=null;
        Cell cell=getCellFromPosition(position);
        dice=cell.getDice();
        cell.removeDice();
        if (onlyFirstDice){
            firstDicePutted=false;
            onlyFirstDice=false;
        }
        return dice;
    }


    public Dice getDiceFromPosition(Position pos){
        return getCellFromPosition(pos).getDice();
    }

    public DiceAndPosition getDiceAndPosition(int diceId){
        Dice tempDice;
        for(Cell cell: schema) {
            if ((tempDice=cell.getDice())!=null){
                if (tempDice.getId() == diceId)
                return new DiceAndPosition(cell.getDice(),cell.getCellPosition());
            }

        }
        return null;
    }

    public Position getPositionFromDice(int diceID){
        for(Cell cell: schema) {
            if (cell.getDice().getId() == diceID)
                return cell.getCellPosition();
        }
        return null;
    }

    private Cell getCellFromPosition(Position pos){
        if (pos.getColumn()>=COLS_NUMBER)
            return null;
        if (pos.getRow()>=ROWS_NUMBER)
            return null;
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
        if (cell.getDice()== null) {
            if (cell.getNumber() == 0 && cell.getColor()!=null)
                return (cell.getColor().equals(dice.getDiceColor()));
            else if (cell.getColor()== null && cell.getNumber()== 0)
                return true;
            else
                return (cell.getNumber() == dice.getDiceNumber());
        }
        else
            return false;
    }


    private boolean checkOnlyNumberCellRestriction(Cell cell, Dice dice){
        //pennello per eglomise: impone che sia considerata sulla cella solo la restrizione di numero e non quella di colore
        if (cell.getDice()!= null)
            return false;
        if (cell.getNumber()==0)
            return true;
        return cell.getNumber() == dice.getDiceNumber();
    }


    private boolean checkOnlyColorCellRestriction(Cell cell, Dice dice){
        //Alesatore per lamine di rame: impone che sia considerata sulla cella solo la restrizione di colore e non quella di numero
        if (cell.getDice()!= null)
            return false;
        if (cell.getColor()==null)
            return true;

        return cell.getColor().equals(dice.getDiceColor());
    }


    private boolean checkAdjacentDiceRestriction(ArrayList<Cell> orthoCell, Dice dice){
        boolean condition=true;
        for(Cell cell: orthoCell) {
            if (cell.getDice()!= null)
                condition&=!checkDiceEquivalence(cell.getDice(), dice);
        }
        return condition;
    }


    private boolean isOrthogonallyAdjacentCell(Cell cell, int row, int column){
        //verifico se la cella è ortogonalmente adiacente a quella sotto esame
        int adjacentRow = cell.getCellPosition().getRow();
        int adjacentColumn = cell.getCellPosition().getColumn();
        return ((adjacentRow == row && (adjacentColumn == (column + 1) || adjacentColumn == (column - 1)))
                || (adjacentColumn == column && (adjacentRow== row-1 || adjacentRow == row+1)));
    }

    private boolean isDiagonallyAdjacentCell(Cell cell, int row, int column){
        int adjacentRow = cell.getCellPosition().getRow();
        int adjacentColumn = cell.getCellPosition().getColumn();
        return  (((adjacentRow==row-1)&&((adjacentColumn==column-1)||(adjacentColumn==column+1)))
                ||((adjacentRow==row+1)&&((adjacentColumn==column-1)||(adjacentColumn==column+1))));

    }

    private ArrayList<Cell> getOrthogonallyAdjacentCells(Position position){
        ArrayList<Cell> orthoCells=new ArrayList<>();
        for(Cell schemaCell: this.schema) {
            if (isOrthogonallyAdjacentCell(schemaCell, position.getRow(), position.getColumn()))
                orthoCells.add(schemaCell);
        }
        return orthoCells;
    }

    private ArrayList<Cell> getDiagonallyAdjacentCells(Position position){
        ArrayList<Cell> diagCells=new ArrayList<>();
        for(Cell schemaCell: this.schema) {
            if (isDiagonallyAdjacentCell(schemaCell, position.getRow(), position.getColumn()))
                diagCells.add(schemaCell);
        }
        return diagCells;
    }
    
    private boolean isThereAtLeastADiceNear(ArrayList<Cell> orthoCells,ArrayList<Cell> diagCells) {
        for(Cell cell: orthoCells) {
            if (cell.getDice()!= null)
                return true;
        }
        for (Cell cell: diagCells){
            if (cell.getDice()!=null)
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
            if (cell.getCellPosition().getRow() == row && cell.getDice() != null)
                rowDices.add(cell.getDice());
        }

        return rowDices;
    }

    public ArrayList<Dice>getRows(int row){
        ArrayList<Dice> rowArray = new ArrayList<>();
        for(Cell cell: schema){
            if (cell.getCellPosition().getRow() == row)
                rowArray.add(cell.getDice());
        }
        return rowArray;
    }


    public ArrayList<Dice> getColDices(int column){
        //restituisce tutti i dadi presenti in una colonna
        ArrayList<Dice> columnDices = new ArrayList<>();

        for(Cell cell: schema) {
            if (cell.getCellPosition().getColumn() == column && cell.getDice() != null)
                columnDices.add(cell.getDice());
        }

        return columnDices;
    }



    public ArrayList<Dice> getWpcDices(){
        //restituisce tutti i dadi presenti sulla wpc
        ArrayList<Dice>  WPCDices = new ArrayList<>();

        for(Cell cell: schema){
            if(cell.getDice()!= null) WPCDices.add(cell.getDice());
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

    public int getNumOfDices(){
        //Restituisce il numero di dadi sulla wpc
        int count = 0;
        for(Cell cell: schema){
            if(cell.getDice()!= null) count++;
        }
        return count;
    }

    /**
     * Counts the cells having dice = null.
     *
     * @return an integer which represent the number of cells with dice= null
     */
    public int getNumFreeCells(){

        int count = 0;
        for (Cell cell : schema) if (cell.getDice() == null) count++;
        return count;
    }

}
