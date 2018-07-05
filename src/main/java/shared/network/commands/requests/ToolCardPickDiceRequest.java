package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class ToolCardPickDiceRequest implements Request {
    public final String userToken;
    public final int diceId;

    /**
     * Constructor of this.
     *
     * @param userToken is the player's token
     * @param diceId is the id of the chosen dice
     */
    public ToolCardPickDiceRequest(String userToken, int diceId) {
        this.userToken = userToken;
        this.diceId = diceId;
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
