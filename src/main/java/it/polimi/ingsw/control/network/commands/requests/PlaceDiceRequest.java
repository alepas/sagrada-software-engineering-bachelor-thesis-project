package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.Position;

public class PlaceDiceRequest implements Request {
    public final String userToken;
    public final Position position;
    public final int diceId;


    public PlaceDiceRequest(String userToken, int diceId, Position position) {
        this.userToken = userToken;
        this.position=position;
        this.diceId=diceId;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

