package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class UpdatedGameRequest implements Request {
    public final String userToken;

    public UpdatedGameRequest(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}