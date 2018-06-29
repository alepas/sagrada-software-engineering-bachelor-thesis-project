package it.polimi.ingsw.shared.network.commands.notifications;

import it.polimi.ingsw.shared.clientInfo.ClientWpc;

public class UserPickedWpcNotification implements Notification {
    public final String username;
    public final ClientWpc wpc;

    public UserPickedWpcNotification(String username, ClientWpc wpc) {
        this.username = username;
        this.wpc = wpc;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
