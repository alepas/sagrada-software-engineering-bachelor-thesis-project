package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class FindGameRequest implements Request {
    public final String token;
    public final int numPlayers;

    public FindGameRequest(String token, int numPlayers) {
        this.token = token;
        this.numPlayers = numPlayers;
    }

    @Override
    public Response handle(RequestHandler handler) { return handler.handle(this); }
}
