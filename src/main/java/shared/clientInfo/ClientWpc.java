package shared.clientInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientWpc implements Serializable {
    private final String wpcID;
    private final String name;
    private final int favours;
    private final ArrayList<ClientCell> schema;

    public ClientWpc(String wpcID,String name, int favours, ArrayList<ClientCell> schema) {
        this.wpcID = wpcID;
        this.name = name;
        this.favours = favours;
        this.schema = schema;
    }

    public int getFavours(){ return favours; }

    public ArrayList<ClientCell> getSchema() { return schema; }

    public String getWpcID(){ return wpcID; }

    public String getName(){return name;}
}
