package it.polimi.ingsw.control.network.commands.responses.notifications;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

public class PlayersChangedNotification implements Response {
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
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
