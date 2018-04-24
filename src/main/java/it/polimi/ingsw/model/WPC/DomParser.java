package it.polimi.ingsw.model.WPC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DomParser {
    private int favour;
    private NodeList wpcList;
    private ArrayList<Node>[] wpcNode;

    private DomParser() throws ParserConfigurationException {
        wpcNode = new ArrayList[24];
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //inizializzo la factory per processare il flusso di dati
            DocumentBuilder builder = factory.newDocumentBuilder(); //inzializzo documento
            Document document = null;
            document = builder.parse(new File("C:\\Users\\User\\Documents\\GitHub\\ing-sw-2018-zorzenon-pasini-piovani\\src\\main\\resources\\WPC store\\wpc_schema")); //pathname del file con tutte le wpc
            document.getDocumentElement().normalize();
            wpcList = document.getElementsByTagName("wpc");
            for (int i = 0; i < wpcList.getLength(); i++)

                wpcNode[i] = wpcList.item();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> WPcLecture(String wpcId) throws ParserConfigurationException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //inizializzo la factory per processare il flusso di dati
            DocumentBuilder builder = factory.newDocumentBuilder(); //inzializzo documento
            Document document = null;
            document = builder.parse(new File("C:\\Users\\User\\Documents\\GitHub\\ing-sw-2018-zorzenon-pasini-piovani\\src\\main\\resources\\WPC store\\wpc_schema")); //pathname del file con tutte le wpc
            document.getDocumentElement().normalize();
            NodeList wpcList = document.getElementsByTagName("wpc");

            for (int i = 0; i < wpcList.getLength(); i++) {
                Node wpcNode = wpcList.item(i);
                if (wpcNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) wpcNode;
                    String Id = eElement.getAttribute("wpcId");
                    if (Id.equals(wpcId)){
                        //setFavours( Integer.parseInt(eElement.getElementsByTagName("favour").item(0).getTextContent()));


                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}