package it.polimi.ingsw.model.WPC;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.xml.sax.SAXException;
public class WPC {
    private String wpcId;
    private int favours;
    private Cell[][] schema;


    public WPC( String id){
        //var Parser = new DomParser(id);
        wpcId = id;
        //favours = 0;
        schema = new Cell[4][5];
        for(int i= 0; i<4; i++) {
            for(int j=0; j<5; j++)
                schema[i][j]=null;
        }
    }

    //ritorna tutti gli id
    public static ArrayList<String> getWpcIDS( ){
        NodeList wpcList = null;
        Node wpcNode = null;
        Element eElement = null;
        ArrayList<String> allWpcIDS = new ArrayList<String>();
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
                eElement = (Element) wpcNode;
                allWpcIDS.add(eElement.getAttribute("wpcId"));
            }
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return allWpcIDS;
    }

    public void FindWPC(String wpcId){

    }

    public void setFavours() {
        getFavours();
        this.favours = favours;
    }

    public int getFavours(){ return favours;};
    public String getWpcID(){
        return null;
    }
    private void checkCellRestriction(){}
    private void checkAdjacentRestriction(){}
}
