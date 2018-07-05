package shared.network.commands.notifications;

import shared.clientInfo.ClientPoc;

import java.util.ArrayList;

public class PocsExtractedNotification implements Notification {
    public final ArrayList<ClientPoc> cards;

    /**
     * Constructor of this.
     *
     * @param cards is the array formed by all public objective cards
     */
    public PocsExtractedNotification(ArrayList<ClientPoc> cards) {
        this.cards = cards;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
