package shared.network.commands.responses;

public class FindGameResponse implements Response {
    public final String gameID;
    public final int actualPlayers;
    public final int numPlayers;
    public final Integer timeToCompleteTask;
    public final Exception exception;

    /**
     * Constructor of this.
     *
     * @param gameID is the game id
     * @param actualPlayers is the number of players that have joined the game
     * @param numPlayers is the max bound of players that must be inside the game
     * @param timeToCompleteTask is how much time is left to enter in the game
     * @param exception is the exception that could be thrown
     */
    private FindGameResponse(String gameID, int actualPlayers, int numPlayers, Integer timeToCompleteTask, Exception exception) {
        this.gameID = gameID;
        this.actualPlayers = actualPlayers;
        this.numPlayers = numPlayers;
        this.timeToCompleteTask = timeToCompleteTask;
        this.exception = exception;
    }

    /**
     * Modifies all values of this.
     *
     * @param gameID is the game id
     * @param actualPlayers is the number of players that have joined the game
     * @param numPlayers is the max bound of players that must be inside the game
     * @param timeToCompleteTask is how much time is left to enter in the game
     */
    public FindGameResponse(String gameID, int actualPlayers, int numPlayers, Integer timeToCompleteTask) {
        this(gameID, actualPlayers, numPlayers, timeToCompleteTask, null);
    }

    public FindGameResponse(Exception exception) {
        this(null, 0, 0, null, exception);
    }


    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}