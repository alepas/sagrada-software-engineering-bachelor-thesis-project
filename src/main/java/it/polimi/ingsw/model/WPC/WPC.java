package it.polimi.ingsw.model.WPC;


import java.util.ArrayList;

public class WPC {
    private int wpcId;
    private int favours;
    private Cell[][] schema;


    public WPC( String id) {
        //var Parser = new DomParser(id)
        wpcId = Integer.parseInt(id);
        favours = 1;
        schema = new Cell[4][5];
        for(int i= 0; i<4; i++) {
            for(int j=0; j<5; j++)
                schema[i][j]=null;
        }
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
