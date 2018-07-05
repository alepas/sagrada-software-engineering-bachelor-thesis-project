package shared.network.commands.requests;

import shared.clientinfo.ToolCardInterruptValues;
import shared.network.commands.responses.Response;

public class ToolCardInteruptRequest implements Request {
    public final String userToken;
    public final ToolCardInterruptValues value;

    /**
     * Constructor of this.
     *
     * @param userToken is the player's token
     * @param value can be OK, YES, NO
     */
    public ToolCardInteruptRequest(String userToken, ToolCardInterruptValues value) {
        this.userToken = userToken;
        this.value = value;
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
