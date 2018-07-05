package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class ToolCardUseRequest implements Request {
    public final String userToken;
    public final String toolCardId;


    /**
     * @param userToken is the player's token
     * @param toolCardId is the id of the tool card
     */
    public ToolCardUseRequest(String userToken, String toolCardId) {
        this.userToken = userToken;
        this.toolCardId = toolCardId;

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
