package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientToolCardModes;

public class ToolCardPickNumberRequest implements Request {
    public final String userToken;
    public final int number;


    public ToolCardPickNumberRequest(String userToken, int number) {
        this.userToken = userToken;
        this.number=number;
    }


    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
