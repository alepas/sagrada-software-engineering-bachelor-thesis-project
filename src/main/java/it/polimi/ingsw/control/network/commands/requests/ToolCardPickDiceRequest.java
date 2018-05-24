package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;

public class ToolCardPickDiceRequest implements Request {
    public final String userToken;
    public final int diceId;
    public final ClientDiceLocations where;

    public ToolCardPickDiceRequest(String userToken, int diceId, ClientDiceLocations where) {
        this.userToken = userToken;
        this.diceId=diceId;
        this.where=where;
    }


    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
