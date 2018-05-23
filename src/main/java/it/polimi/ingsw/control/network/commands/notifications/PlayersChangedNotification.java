package it.polimi.ingsw.control.network.commands.notifications;

public class PlayersChangedNotification implements Notification {
    public final String username;
    public final boolean joined;
    public final int actualPlayers;
    public final int numPlayers;

    public PlayersChangedNotification(String username, boolean joined, int actualPlayers, int numPlayers) {
        this.username = username;
        this.joined = joined;
        this.actualPlayers = actualPlayers;
        this.numPlayers = numPlayers;
    }

    @Override
    public void handle(NotificationHandler handler) {
        handler.handle(this);
    }
}
