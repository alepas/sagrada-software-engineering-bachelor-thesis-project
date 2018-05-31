package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.clientModel.ClientWpc;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Color;
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
import java.util.HashMap;

public class WpcDB {
    private static WpcDB instance;
    private HashMap<String, Wpc> map;

    public static WpcDB getInstance(){
        if (instance==null) instance = new WpcDB("src/main/resources/wpc/wpc_schema");
        return instance;

    }

    public static WpcDB getInstance(String pathOfWpcFile){
        if (instance==null) instance = new WpcDB(pathOfWpcFile);
        return instance;
    }

    private WpcDB(String pathFile){
        map = new HashMap<>();

        NodeList wpcList;
        NodeList wpcRow;
        NodeList wpcColumn;
        NodeList wpcFavours;
        NodeList wpcCellColor;
        NodeList wpcCellNumber;

        Node wpcCellColorNode;
        Node wpcNode;
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
        Wpc wpcTemp;
        int number = 0;
        int favours = 0;
        String wpcID;
        Color color = null;
        ArrayList<Cell> schema = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //inizializzo la factory per processare il flusso di dati
            DocumentBuilder builder = factory.newDocumentBuilder(); //inzializzo documento
            Document document;
            document = builder.parse(new File(pathFile)); //pathname del file con tutte le wpc
            document.getDocumentElement().normalize();
            wpcList = document.getElementsByTagName("wpc");

            //scorro tutti i nodi con tagname wpc
            for (int i = 0; i < wpcList.getLength(); i++) {
                wpcNode = wpcList.item(i);

                if (wpcNode.getNodeType() == Node.ELEMENT_NODE) {
                    eElementID = (Element) wpcNode;
                    wpcID = eElementID.getAttribute("id");

                    wpcRow = eElementID.getElementsByTagName("row");
                    //scorro tutti i nodi con tagname row
                    for (int j = 0; j < wpcRow.getLength(); j++) {
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

                    wpcTemp = new Wpc(wpcID, favours, schema);
                    map.put(wpcID,wpcTemp);
                    schema.clear();
                }
            }
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public Wpc getWpcByID(String ID){
        //dato l'id selezionato dal giocatore si chiama il costruttore che genera una copia della wpc
        Wpc originalWpc=map.get(ID);
       /* for(Cell cell: originalWpc.schema){
            System.out.println("riga: "+ cell.getCellPosition().getRow());
            System.out.println("colonna: "+ cell.getCellPosition().getColumn());
            System.out.println("colore: "+ cell.getColor());
            System.out.println("numero: "+ cell.getNumber());
            System.out.println("---------");
        }*/

        return originalWpc.copyWpc();
    }

    public ClientWpc getClientWpcByID(String id){
        return getWpcByID(id).getClientWpc();
    }

    public ArrayList<String> getWpcIDs() {
        ArrayList<String> ids= new ArrayList(map.keySet());
       /* for(String s: a)
            System.out.println(s);*/
        return ids;
    }

}
