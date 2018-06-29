package it.polimi.ingsw.shared.network.commands.responses;

public class FindGameResponse implements Response {
    public final String gameID;
    public final int actualPlayers;
    public final int numPlayers;
    public final Exception exception;

    public FindGameResponse(String gameID, int actualPlayers, int numPlayers, Exception exception) {
        this.gameID = gameID;
        this.actualPlayers = actualPlayers;
        this.numPlayers = numPlayers;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}