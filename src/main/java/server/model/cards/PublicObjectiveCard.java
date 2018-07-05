package server.model.cards;

import server.model.wpc.Wpc;
import shared.clientinfo.ClientPoc;


public abstract class PublicObjectiveCard {
    protected String id;
    protected String name;
    protected String description;

    public String getID(){ return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public abstract int calculateScore(Wpc wpc);

    public ClientPoc getClientPoc(){
        return new ClientPoc(id, name, description);
    }
}
