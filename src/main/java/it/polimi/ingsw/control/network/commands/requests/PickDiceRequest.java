package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class PickDiceRequest implements Request {
    public final int diceId;
    public final String userToken;

    public PickDiceRequest(String userToken, int diceId) {
        this.diceId = diceId;
        this.userToken = userToken;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
