package it.polimi.ingsw.shared.network.commands.requests;

import it.polimi.ingsw.shared.clientInfo.Position;
import it.polimi.ingsw.shared.network.commands.responses.Response;

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

