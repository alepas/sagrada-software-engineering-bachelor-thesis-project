package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.Position;

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

