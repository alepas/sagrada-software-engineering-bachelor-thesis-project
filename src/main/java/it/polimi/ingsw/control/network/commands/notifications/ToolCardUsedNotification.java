package it.polimi.ingsw.control.network.commands.notifications;

import it.polimi.ingsw.model.clientModel.ClientToolCard;

import java.util.ArrayList;

public class ToolCardUsedNotification implements Notification {
    public final String username;
    public final ClientToolCard toolCard;
    public final ArrayList<Notification> movesNotifications;

    public ToolCardUsedNotification(String username, ClientToolCard toolCard, ArrayList<Notification> movesNotifications) {
        this.username = username;
        this.toolCard = toolCard;
        this.movesNotifications = movesNotifications;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
