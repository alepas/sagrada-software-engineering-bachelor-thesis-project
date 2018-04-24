package it.polimi.ingsw.model.WPC;


import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

public class WPC {
    private String wpcId;
    private int favours;
    private Cell[][] schema;


    public WPC( String id) throws ParserConfigurationException {
        var Parser = new DomParser(id);
        wpcId = id;
        favours = 1;
        schema = new Cell[4][5];
        for(int i= 0; i<4; i++) {
            for(int j=0; j<5; j++)
                schema[i][j]=null;
        }
    }

    public void setFavours(int favours) {
        this.favours = favours;
    }

    public static ArrayList<String> getWpcIDs() {
        return null;
    }

    public int getFavours(){ return favours;};
    public static ArrayList<String> getWPCid(){
        return null;
    }
    private void checkCellRestriction(){}
    private void checkAdjacentRestriction(){}
}
