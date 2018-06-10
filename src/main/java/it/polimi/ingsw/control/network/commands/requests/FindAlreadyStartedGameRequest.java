package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class FindAlreadyStartedGameRequest implements Request {
    public final String token;

    public FindAlreadyStartedGameRequest(String token) {
        this.token = token;
    }

    @Override
    public Response handle(RequestHandler handler) { return handler.handle(this); }
}
