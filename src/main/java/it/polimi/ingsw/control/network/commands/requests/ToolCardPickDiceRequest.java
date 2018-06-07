package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;

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
