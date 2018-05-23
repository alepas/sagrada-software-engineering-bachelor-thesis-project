package it.polimi.ingsw.control.network.commands.notifications;

public class NextTurnNotification implements Notification {
    public final int turnNumber;
    public final String activeUser;

    public NextTurnNotification(int turnNumber, String activeUser) {
        this.turnNumber = turnNumber;
        this.activeUser = activeUser;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
