package it.polimi.ingsw.control.network.commands.notifications;

public class UserPickedWpcNotification implements Notification {
    public final String username;
    public final String wpcID;

    public UserPickedWpcNotification(String username, String wpcID) {
        this.username = username;
        this.wpcID = wpcID;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
