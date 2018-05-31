package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.Position;

public class ToolCardPlaceDiceRequest implements Request {
    public final String userToken;
    public final Position position;
    public final int diceId;
    public final ClientDiceLocations diceFrom;
    public final ClientDiceLocations diceDestination;


    public ToolCardPlaceDiceRequest(String userToken, int diceId, ClientDiceLocations diceFrom, ClientDiceLocations diceDestination, Position position) {
        this.userToken = userToken;
        this.diceDestination = diceDestination;
        this.position=position;
        this.diceId=diceId;
        this.diceFrom=diceFrom;

    }




    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

