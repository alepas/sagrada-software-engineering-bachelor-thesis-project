package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientWpc implements Serializable {
    private  String wpcID;
    private  int favours;
    private ArrayList<ClientCell> schema;


    public ClientWpc(String wpcID, int favours, ArrayList<ClientCell> schema) {
        this.wpcID = wpcID;
        this.favours = favours;
        this.schema = schema;
    }

    public int getFavours(){ return favours; }

    public ArrayList<ClientCell> getSchema() { return schema; }

    public String getWpcID(){ return wpcID; }
}
