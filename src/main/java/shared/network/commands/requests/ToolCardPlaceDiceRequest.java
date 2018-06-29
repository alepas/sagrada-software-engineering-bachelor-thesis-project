package shared.network.commands.requests;

import shared.clientInfo.Position;
import shared.network.commands.responses.Response;

public class ToolCardPlaceDiceRequest implements Request {
    public final String userToken;
    public final int diceId;
    public final Position position;


    public ToolCardPlaceDiceRequest(String userToken, int diceId, Position position) {
        this.userToken = userToken;
        this.diceId = diceId;
        this.position = position;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

