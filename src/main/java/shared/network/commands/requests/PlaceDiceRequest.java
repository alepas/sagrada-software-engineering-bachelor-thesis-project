package shared.network.commands.requests;

import shared.clientinfo.Position;
import shared.network.commands.responses.Response;

public class PlaceDiceRequest implements Request {
    public final String userToken;
    public final Position position;
    public final int diceId;


    /**
     * Constructor of this
     *
     * @param userToken is the player's token
     * @param diceId is the id of the chosen dice
     * @param position is the position where the player wants to add the dice
     */
    public PlaceDiceRequest(String userToken, int diceId, Position position) {
        this.userToken = userToken;
        this.position=position;
        this.diceId=diceId;
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

