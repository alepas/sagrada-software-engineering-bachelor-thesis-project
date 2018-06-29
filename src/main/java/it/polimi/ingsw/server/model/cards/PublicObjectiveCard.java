package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.wpc.Wpc;
import it.polimi.ingsw.shared.clientInfo.ClientPoc;

import java.util.ArrayList;

public abstract class PublicObjectiveCard {
    protected String id;
    protected String name;
    protected String description;

    public static ArrayList<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();

    public String getID(){ return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public abstract int calculateScore(Wpc wpc);

    public ClientPoc getClientPoc(){
        return new ClientPoc(id, name, description);
    }
}
