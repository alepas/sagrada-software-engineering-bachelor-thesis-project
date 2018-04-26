package it.polimi.ingsw.model.wpcEdited;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.wpcExceptions.NotExistingCellException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WpcDBEdited {
    private static WpcDBEdited instance;
    private static String pathFile;
    private static HashMap<String,WpcEdited> map;

    public static WpcDBEdited getInstance(){
        if (instance==null) {

            instance = new WpcDBEdited();
            pathFile="src/main/resources/wpc/wpc_schema";
            map = new HashMap<String, WpcEdited>();

        }
        return instance;

    }

    public static WpcDBEdited getInstance(String pathOfWpcFile){
        if (instance==null) {

            instance = new WpcDBEdited();
            pathFile=pathOfWpcFile;
            map = new HashMap<String, WpcEdited>();

        }
        return instance;

    }

    private WpcDBEdited(){

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

        PositionEdited position;
        WpcEdited wpcTemp=null;
        int number = 0;
        int favours = 0;
        String wpcID = null;
        Color color = null;
        ArrayList<CellEdited> schema = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //inizializzo la factory per processare il flusso di dati
            DocumentBuilder builder = factory.newDocumentBuilder(); //inzializzo documento
            Document document = null;
            document = builder.parse(new File(pathFile)); //pathname del file con tutte le wpc
            document.getDocumentElement().normalize();
            wpcList = document.getElementsByTagName("wpc");
            for (int i = 0; i < wpcList.getLength(); i++)
                wpcNode = wpcList.item(i);
            if (wpcNode.getNodeType() == Node.ELEMENT_NODE) {
                eElementID = (Element) wpcNode;
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
                                position = new PositionEdited(row, column);
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
                                schema.add(new CellEdited(position, color, number));
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
            wpcTemp= new WpcEdited( wpcID, favours, schema );
            map.put(wpcID,wpcTemp);
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static WpcEdited getWpcByID(String ID){
        //dato l'id selezionato dal giocatore si chiama il costruttore che genera una copia della wpc
        WpcEdited originalWpc=map.get(ID);
        return originalWpc.copyWpc();
    }

    public static ArrayList<String> getWpcIDs() {
        return new ArrayList<String>(map.keySet());
    }

}
