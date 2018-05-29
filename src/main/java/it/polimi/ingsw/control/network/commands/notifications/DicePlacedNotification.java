package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientWpc;

public class DicePlacedNotification implements Notification {
    public final ClientPosition position;
    public final boolean usingToolCard;
    public final String username;
    public final ClientWpc wpc;

    public DicePlacedNotification(String username, ClientPosition position, boolean usingToolCard, ClientWpc wpc) {
        this.position = position;
        this.username = username;
        this.usingToolCard = usingToolCard;
        this.wpc = wpc;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
