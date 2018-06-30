package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class PassTurnRequest implements Request {
    public final String userToken;

    public PassTurnRequest(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}