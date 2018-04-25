package it.polimi.ingsw.model.WPC;
import java.util.ArrayList;
//HO UN ARRAY base di wpc, quando giocatore sceglie una wpc mi viene passato l'id, faccio ricerca nel mio array e poi
//copio la wpc corrispondente

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
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
    private static WPC personalWPC ;
    private String wpcID;
    private int favours;
    private ArrayList<Cell> schema = new ArrayList<>();
    private static ArrayList<WPC> WPCs;
    private static ArrayList<String> allWpcIDS = new ArrayList<String>();

    private WPC(WPC WPC){
        personalWPC = WPC;
    }


    private WPC(String wpcID, int favours, ArrayList<Cell> schema) {
        this.wpcID = wpcID;
        this.favours = favours;
        this.schema.addAll(schema);
    }

    public static void loadWPC(){
        NodeList wpcList = null;
        Node wpcNode = null;
        NodeList wpcRow = null;
        Node wpcRowNode = null;
        NodeList wpcColumn = null;
        Element eElementID = null;
        Element eElementRow = null;
        Node wpcColumnNode = null;
        Element eElementColumn = null;
        NodeList wpcCellColor = null;
        Node wpcCellColorNode = null;
        Element eElementCellColor= null;
        NodeList wpcCellNumber = null;
        Node wpcCellNumberNode = null;
        Element eElementCellNumber = null;
        NodeList wpcFavours = null;
        Node wpcFavoursNode = null;
        Element eElementFavours = null;
        int number = 0;
        Color color = null;
        Position position;
        int favours = 0;
        String wpcID = null;
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
    public static WPC getWpcByID(String ID){
        for (int i = 0; i<WPCs.size(); i++){
            if(WPCs.get(i).wpcID.equals(ID))
                personalWPC = new WPC(WPCs.get(i));
        }
        return personalWPC;
    }

    public static ArrayList<String> getWpcIDs( ) {
        return allWpcIDS;
    }

    public int getFavours(){ return personalWPC.favours;};

    public String getWpcID(){
        return personalWPC.wpcID;
    }

    private void checkCellRestriction(){}
    private void checkAdjacentRestriction(){}





    //AGGIUNTI DA DAVIDE
    public int numOfRows(){
        //Restituisce il numero di righe della wpc
        return 0;
    }

    public int numOfCols(){
        //Restituisce il numero di colonne della wpc
        return 0;
    }

    public Dice[] getRowDices(int row){
        //Restituisce i dadi presenti nella riga passata
        return null;
    }

    public Dice[] getColDices(int col){
        //Restituisce i dadi presenti nella colonna passata
        return null;
    }

    public Dice[] getWpcDices(){
        //Restituisce tutti i dadi presenti nella wpc
        return null;
    }

    public int numOfDicesOfShade(int shade){
        int count = 0;

        for (Dice dice : getWpcDices()){
            if (dice.getDiceNumber() == shade) count++;
        }

        return count;
    }
}
