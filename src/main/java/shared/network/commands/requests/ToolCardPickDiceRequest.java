package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class ToolCardPickDiceRequest implements Request {
    public final String userToken;
    public final int diceId;

    public ToolCardPickDiceRequest(String userToken, int diceId) {
        this.userToken = userToken;
        this.diceId = diceId;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
