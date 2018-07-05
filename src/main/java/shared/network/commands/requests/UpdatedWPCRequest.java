package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class UpdatedWPCRequest implements Request {
    public final String userToken;
    public final String username;

    /**
     * Constructor of this.
     *
     * @param userToken is the token related to the player
     * @param usernameforWPC is the player's username
     */
    public UpdatedWPCRequest(String userToken, String usernameforWPC) {
        this.userToken = userToken;
        this.username = usernameforWPC;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}