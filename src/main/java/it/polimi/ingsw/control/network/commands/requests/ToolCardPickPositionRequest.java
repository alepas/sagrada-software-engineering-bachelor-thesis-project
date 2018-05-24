package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;

public class ToolCardPickPositionRequest implements Request {
    public final String userToken;
    public final ClientPosition position;



    public ToolCardPickPositionRequest(String userToken, ClientPosition position) {
        this.userToken = userToken;
        this.position=position;

    }




    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

