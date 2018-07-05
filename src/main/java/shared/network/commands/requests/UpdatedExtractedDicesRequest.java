package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class UpdatedExtractedDicesRequest implements Request {
    public final String userToken;

    /**
     * Constructor of this.
     *
     * @param userToken is the token related to the player
     */
    public UpdatedExtractedDicesRequest(String userToken) {
        this.userToken = userToken;
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