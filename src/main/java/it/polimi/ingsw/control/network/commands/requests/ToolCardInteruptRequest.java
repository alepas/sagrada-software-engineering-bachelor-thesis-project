package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.clientModel.ToolCardInteruptValues;

public class ToolCardInteruptRequest implements Request {
    public final String userToken;
    public final ToolCardInteruptValues value;

    public ToolCardInteruptRequest(String userToken, ToolCardInteruptValues value) {
        this.userToken = userToken;
        this.value = value;
    }



    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
