package it.polimi.ingsw.model.WPC;


public class WPC {
    private int wpcId;
    private int favour;
    private Cell[][] schema;

    public void WPC( String id) {
        //var Parser = new DomParser(id)
        wpcId = Integer.parseInt(id);
        favour = 1;
        schema = new Cell[4][5];
        for(int i= 0; i<4; i++) {
            for(int j=0; j<5; j++)
                schema[i][j]=null;
        }
    }

    private void checkCellRestriction(){}
    private void checkAdjacentRestriction(){}
}
