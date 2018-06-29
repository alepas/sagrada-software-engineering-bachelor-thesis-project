package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class FindGameRequest implements Request {
    public final String token;
    public final int numPlayers;
    public final int levelOfDifficulty;

    public FindGameRequest(String token, int numPlayers) {
        this.token = token;
        this.numPlayers = numPlayers;
        this.levelOfDifficulty=0;
    }

    public FindGameRequest(String token, int numPlayers, int levelOfDifficulty) {
        this.token = token;
        this.numPlayers = numPlayers;
        this.levelOfDifficulty = levelOfDifficulty;
    }

    @Override
    public Response handle(RequestHandler handler) { return handler.handle(this); }
}
