package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class FindGameRequest implements Request {
    public final String token;
    public final int numPlayers;
    public final int levelOfDifficulty;

    public FindGameRequest(String token, int numPlayers) {
        this.token = token;
        this.numPlayers = numPlayers;
        this.levelOfDifficulty=0;
    }

    /**
     * @param token is the player's tokem
     * @param numPlayers is the number of players inside the game
     * @param levelOfDifficulty os the difficulty related to the single player game
     */
    public FindGameRequest(String token, int numPlayers, int levelOfDifficulty) {
        this.token = token;
        this.numPlayers = numPlayers;
        this.levelOfDifficulty = levelOfDifficulty;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public Response handle(RequestHandler handler) { return handler.handle(this); }
}
