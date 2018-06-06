package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class GetUserStatRequest implements Request {
    public final String userToken;

    public GetUserStatRequest(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
