package it.polimi.ingsw.model.wpc;
import java.util.ArrayList;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;


import it.polimi.ingsw.model.exceptions.wpcExceptions.NotExistingCellException;

import  org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.IOException;

public class WPC {
    private  String wpcID;
    private  int favours;
    private  ArrayList<Cell> schema = new ArrayList<>();


    WPC(String wpcID, int favours, ArrayList<Cell> schema) {
        this.wpcID = wpcID;
        this.favours = favours;
        this.schema.addAll(schema);
    }

    public WPC copyWpc(){
        return new WPC(wpcID,favours,schema);
    }


    public int getFavours(){ return favours; }


    public String getWpcID(){ return wpcID; }


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
        return row == 0 || column == 0;
    }


    //controllo che il dado possa essere inserito nella cella selezionata secondo le restrizioni di questa
    private boolean checkCellRestriction(Cell cell, Dice dice){
        if (cell.getCellDice()!= null)
           return false;
        switch (cell.getCellNumber()){
            case 0:
                if (cell.getCellColor().equals(dice.getDiceColor())) break;
                else
                    return false;
            default:
                if (cell.getCellNumber() == dice.getDiceNumber()) break;
                else
                    return false;
        }
        return true;
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
        int i= 1;
        int row= cell.getCellPosition().getRow();
        int column= cell.getCellPosition().getColumn();

        for(Cell schemaCell: schema){
            if(schemaCell.getCellPosition().getRow() == row) {
                if (schemaCell.getCellPosition().getColumn() == column + i || schemaCell.getCellPosition().getColumn() == column - i){
                    if (schemaCell.getCellDice() != null)
                        return checkEquivalence(schemaCell.getCellDice(), dice);
                }
            }
            if(schemaCell.getCellPosition().getColumn() == column) {
                if (schemaCell.getCellPosition().getColumn() == row + i || schemaCell.getCellPosition().getColumn() == row - i) {
                    if (schemaCell.getCellDice() != null)
                        return checkEquivalence(schemaCell.getCellDice(), dice);
                }
            }
        }
        return true;
    }

    //manca il controllo che ci sia almeno un dado attorno alla cella selezionata
    private void IsThereAtLeastADice(Cell cell) {
    }


    private boolean checkEquivalence(Dice cellDice, Dice dice){
        //controlla se il dado in una cella adiacente ha un dado
        //e se questo ha numero o colore uguali a quello del dado che si desidera inserire
            return dice.getDiceNumber() != cellDice.getDiceNumber() && !dice.getDiceColor().equals(cellDice.getDiceColor());
    }


    public ArrayList<Dice> getRowDices(int row){
        //restituisce tutti i dadi presenti in una riga
        ArrayList<Dice> rowDices = new ArrayList<>();

        for(Cell cell: schema){
            if(cell.getCellPosition().getRow() == row)
                if(cell.getCellDice()!= null)
                    rowDices.add(cell.getCellDice());
        }

        return rowDices;
    }


    public ArrayList<Dice> getColDices(int column){
        //restituisce tutti i dadi presenti in una colonna
        ArrayList<Dice> columnDices = new ArrayList<>();

        for(Cell cell: schema) {
            if (cell.getCellPosition().getColumn() == column)
                if (cell.getCellDice() != null)
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


    public int countTotalPrivateObjective(Color color){
        int score = 0;

        for (Dice dice : getWpcDices()){
            if ( dice.getDiceColor().equals(color)) score++;
        }

        return score;
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