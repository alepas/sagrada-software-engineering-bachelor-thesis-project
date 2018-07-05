package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class ToolCardPickNumberRequest implements Request {
    public final String userToken;
    public final int number;


    /**
     * Constructor of this
     *
     * @param userToken is the player's token
     * @param number is the number chosen by the player
     */
    public ToolCardPickNumberRequest(String userToken, int number) {
        this.userToken = userToken;
        this.number=number;
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
