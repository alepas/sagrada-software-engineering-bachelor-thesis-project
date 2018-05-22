package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.cards.PocDB;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;

import java.util.ArrayList;

public class PocsExtractedNotification implements Notification {
    public final ArrayList<String> pocIDs;
    public final ArrayList<String> pocNames;
    public final ArrayList<String> pocsDescrip;

    public PocsExtractedNotification(ArrayList<String> pocIDs, PocDB db) {
        PublicObjectiveCard temp;
        this.pocNames=new ArrayList<>();
        this.pocsDescrip=new ArrayList<>();
        this.pocIDs = pocIDs;
        for (String id: pocIDs){
            temp=db.getCardByID(id);
            pocNames.add(temp.getName());
            pocsDescrip.add(temp.getDescription());
        }
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
