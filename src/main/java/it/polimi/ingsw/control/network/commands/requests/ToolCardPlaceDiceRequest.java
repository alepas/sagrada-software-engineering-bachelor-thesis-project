package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.*;

public class ToolCardPlaceDiceRequest implements Request {
    public final String userToken;
    public final ClientPosition position;
    public final int diceId;
    public final ClientDiceLocations diceFrom;


    public ToolCardPlaceDiceRequest(String userToken, int diceId, ClientDiceLocations diceFrom, ClientPosition position) {
        this.userToken = userToken;
        this.position=position;
        this.diceId=diceId;
        this.diceFrom=diceFrom;

    }




    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

