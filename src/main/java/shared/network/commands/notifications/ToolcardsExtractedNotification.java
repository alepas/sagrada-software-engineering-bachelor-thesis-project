package shared.network.commands.notifications;

import shared.clientInfo.ClientToolCard;

import java.util.ArrayList;

public class ToolcardsExtractedNotification implements Notification {
    public final ArrayList<ClientToolCard> cards;

    /**
     * Constructor of this
     * @param cards is the arraylist formed by all toolcards
     */
    public ToolcardsExtractedNotification(ArrayList<ClientToolCard> cards) {
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
