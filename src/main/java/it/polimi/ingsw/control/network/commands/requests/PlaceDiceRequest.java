package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientPosition;

public class PlaceDiceRequest implements Request {
    public final String userToken;
    public final ClientPosition position;
    public final int diceId;


    public PlaceDiceRequest(String userToken, int diceId, ClientPosition position) {
        this.userToken = userToken;
        this.position=position;
        this.diceId=diceId;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

