package it.polimi.ingsw.shared.network.commands.notifications;

import it.polimi.ingsw.shared.clientInfo.ClientPoc;

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
