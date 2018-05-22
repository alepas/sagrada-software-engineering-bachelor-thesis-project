package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.cards.ToolCardDB;

import java.util.ArrayList;

public class ToolcardsExtractedNotification implements Notification {
    public final ArrayList<String> toolcardsIDs;
    public final ArrayList<String> toolcardsNames;
    public final ArrayList<String> toolcardsDescrip;

    public ToolcardsExtractedNotification(ArrayList<String> toolcardsIDs, ToolCardDB db) {
        ToolCard temp;
        toolcardsNames=new ArrayList<>();
        toolcardsDescrip=new ArrayList<>();
        this.toolcardsIDs = toolcardsIDs;
        for (String id: toolcardsIDs){
            temp=db.getCardByID(id);
            toolcardsNames.add(temp.getName());
            toolcardsDescrip.add(temp.getDescription());
        }
    }


    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
