package shared.network.commands.notifications;

import shared.clientInfo.ClientToolCard;

import java.util.ArrayList;

public class ToolcardsExtractedNotification implements Notification {
    public final ArrayList<ClientToolCard> cards;

    public ToolcardsExtractedNotification(ArrayList<ClientToolCard> cards) {
        this.cards = cards;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
