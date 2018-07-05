package shared.network.commands.notifications;

import shared.clientinfo.ClientWpc;

public class UserPickedWpcNotification implements Notification {
    public final String username;
    public final ClientWpc wpc;

    /**
     * Costructor of this.
     *
     * @param username is the player's username
     * @param wpc is the player's schema
     */
    public UserPickedWpcNotification(String username, ClientWpc wpc) {
        this.username = username;
        this.wpc = wpc;
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
