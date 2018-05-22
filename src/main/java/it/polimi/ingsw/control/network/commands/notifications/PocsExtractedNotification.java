package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.model.clientModel.ClientPoc;

import java.util.ArrayList;

public class PocsExtractedNotification implements Notification {
    public final ArrayList<ClientPoc> cards;

    public PocsExtractedNotification(ArrayList<ClientPoc> cards) {
        this.cards = cards;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
