package it.polimi.ingsw.shared.network.commands.notifications;

public class ForceDisconnectionNotification implements Notification {
    public final boolean lostConnection;

    public ForceDisconnectionNotification(boolean lostConnection) {
        this.lostConnection = lostConnection;
    }


    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
