package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class UpdatedWPCRequest implements Request {
    public final String userToken;
    public final String username;


    public UpdatedWPCRequest(String userToken, String usernameforWPC) {
        this.userToken = userToken;
        this.username = usernameforWPC;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}