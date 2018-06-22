package it.polimi.ingsw.control.network.commands.notifications;

public class ForceDisconnectionNotification implements Notification {
    public ForceDisconnectionNotification() {
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
