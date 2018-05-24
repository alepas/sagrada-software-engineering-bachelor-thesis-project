package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;

public class ToolCardPickColorRequest implements Request {
    public final String userToken;
    public final ClientColor color;



    public ToolCardPickColorRequest(String userToken, ClientColor color) {
        this.userToken = userToken;
        this.color=color;
    }



    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
