package it.polimi.ingsw.model.WPC;
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
    //private Cell[][] Schema;
    private  ArrayList<Cell> schema = new ArrayList<>();
    static  ArrayList<WPC> WPCs;  //chiamato da un metodo static quindi, per non generare errore devono essere static
    static ArrayList<String> allWpcIDS = new ArrayList<String>(); //uguale a sopra

    private WPC(String wpcID, int favours, ArrayList<Cell> schema) {
        this.wpcID = wpcID;
        this.favours = favours;
        this.schema.addAll(schema);
    }

    public static void loadWPCs(){
        NodeList wpcList;
        NodeList wpcRow;
        NodeList wpcColumn;
        NodeList wpcFavours;
        NodeList wpcCellColor;
        NodeList wpcCellNumber;

        Node wpcCellColorNode;
        Node wpcNode = null;
        Node wpcRowNode;
        Node wpcColumnNode;
        Node wpcFavoursNode;
        Node wpcCellNumberNode;

        Element eElementID;
        Element eElementRow;
        Element eElementColumn;
        Element eElementCellColor;
        Element eElementCellNumber;
        Element eElementFavours;

        Position position;
        int number = 0;
        int favours = 0;
        String wpcID = null;
        Color color = null;
        ArrayList<Cell> schema = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //inizializzo la factory per processare il flusso di dati
            DocumentBuilder builder = factory.newDocumentBuilder(); //inzializzo documento
            Document document = null;
            document = builder.parse(new File("C:\\Users\\User\\Documents\\GitHub\\ing-sw-2018-zorzenon-pasini-piovani\\src\\main\\resources\\WPC store\\wpc_schema")); //pathname del file con tutte le wpc
            document.getDocumentElement().normalize();
            wpcList = document.getElementsByTagName("wpc");
            for (int i = 0; i < wpcList.getLength(); i++)
                wpcNode = wpcList.item(i);
            if (wpcNode.getNodeType() == Node.ELEMENT_NODE) {
                eElementID = (Element) wpcNode;
                allWpcIDS.add(eElementID.getAttribute("wpcID"));
                wpcID = eElementID.getAttribute("wpcID");
                wpcRow= eElementID.getElementsByTagName("row");
                for (int j=0; j< wpcRow.getLength(); j++) {
                    wpcRowNode = wpcRow.item(j);
                    if (wpcRowNode.getNodeType() == Node.ELEMENT_NODE) {
                        eElementRow = (Element) wpcRowNode;
                        int row = Integer.parseInt(eElementRow.getAttribute("x"));
                        wpcColumn = eElementRow.getElementsByTagName("column");
                        for (int w = 0; w < wpcColumn.getLength(); w++) {
                            wpcColumnNode = wpcColumn.item(w);
                            if (wpcColumnNode.getNodeType() == Node.ELEMENT_NODE) {
                                eElementColumn = (Element) wpcColumnNode;
                                int column = Integer.parseInt(eElementColumn.getAttribute("y"));
                                position = new Position(row, column);
                                wpcCellColor = eElementColumn.getElementsByTagName("color");
                                for (int z = 0; z < wpcCellColor.getLength(); z++) {
                                    wpcCellColorNode = wpcCellColor.item(z);
                                    if (wpcCellColorNode.getNodeType() == Node.ELEMENT_NODE) {
                                        eElementCellColor = (Element) wpcCellColorNode;
                                        color = Color.parseColor(eElementCellColor.getTextContent());
                                    }
                                }
                                wpcCellNumber = eElementColumn.getElementsByTagName("number");
                                for (int s = 0; s < wpcCellNumber.getLength(); s++) {
                                    wpcCellNumberNode = wpcCellNumber.item(s);
                                    if (wpcCellNumberNode.getNodeType() == Node.ELEMENT_NODE) {
                                        eElementCellNumber = (Element) wpcCellNumberNode;
                                        number = Integer.parseInt(eElementCellNumber.getTextContent());
                                    }
                                }
                                schema.add(new Cell(position, color, number));
                            }
                        }
                        wpcFavours = eElementID.getElementsByTagName("favour");
                        for (int s = 0; s < wpcFavours.getLength(); s++) {
                            wpcFavoursNode = wpcFavours.item(s);
                            if (wpcFavoursNode.getNodeType() == Node.ELEMENT_NODE) {
                                eElementFavours = (Element) wpcFavoursNode;
                                favours = Integer.parseInt(eElementFavours.getTextContent());
                            }
                        }
                    }
                }
            }
            WPCs.add(new WPC( wpcID, favours, schema ));
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public WPC getWpcByID(String ID){
        //dato l'id selezionato dal giocatore si chiama il costruttore che genera una copia della wpc
        WPC wpc = null;

        for (WPC WPC: WPCs) {
            if (WPC.wpcID.equals(ID)) wpc = new WPC(WPC.wpcID, WPC.favours, WPC.schema);
        }

        return wpc;
    }

    public static ArrayList<String> getWpcIDs() { return allWpcIDS; }

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

    //gli arraylist sono così comodi perchè usare un array?
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

    private ArrayList<Dice> getWpcDices(){
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
            if (dice.getDiceColor() == color) count++;
        }

        return count;
    }


    public int numFreeCells(){
        //restituisce il numero di celle vuote sulla wpc
        int count = 0;

        for (Cell cell : schema){
            if (cell.getCellDice() == null) count++;
        }

        return count;
    }
}
