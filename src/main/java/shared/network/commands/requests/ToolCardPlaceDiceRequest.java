package shared.network.commands.requests;

import shared.clientinfo.Position;
import shared.network.commands.responses.Response;

public class ToolCardPlaceDiceRequest implements Request {
    public final String userToken;
    public final int diceId;
    public final Position position;


    /**
     * @param userToken is the player's token
     * @param diceId is the id of the chosen dice
     * @param position is the position where the player would like to add the dice
     */
    public ToolCardPlaceDiceRequest(String userToken, int diceId, Position position) {
        this.userToken = userToken;
        this.diceId = diceId;
        this.position = position;
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

