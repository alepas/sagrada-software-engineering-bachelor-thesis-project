package shared.network.commands.responses;

public class FindGameResponse implements Response {
    public final String gameID;
    public final int actualPlayers;
    public final int numPlayers;
    public final Integer timeToCompleteTask;
    public final Exception exception;

    private FindGameResponse(String gameID, int actualPlayers, int numPlayers, Integer timeToCompleteTask, Exception exception) {
        this.gameID = gameID;
        this.actualPlayers = actualPlayers;
        this.numPlayers = numPlayers;
        this.timeToCompleteTask = timeToCompleteTask;
        this.exception = exception;
    }

    public FindGameResponse(String gameID, int actualPlayers, int numPlayers, Integer timeToCompleteTask) {
        this(gameID, actualPlayers, numPlayers, timeToCompleteTask, null);
    }

    public FindGameResponse(Exception exception) {
        this(null, 0, 0, null, exception);
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}