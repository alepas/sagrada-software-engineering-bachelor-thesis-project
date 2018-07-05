package shared.clientinfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * It's a copy of the wpc object in the server model, it doesn't contains any logic
 */
public class ClientWpc implements Serializable {
    private final String wpcID;
    private final String name;
    private final int favours;
    private final ArrayList<ClientCell> schema;

    /**
     * @param wpcID is the wpc id
     * @param name is the wpc's name
     * @param favours is the number of favours related to the the wpc
     * @param schema is the wpc's schema
     */
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
