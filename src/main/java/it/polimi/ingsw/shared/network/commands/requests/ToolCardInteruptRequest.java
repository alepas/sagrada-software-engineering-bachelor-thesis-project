package it.polimi.ingsw.shared.network.commands.requests;

import it.polimi.ingsw.shared.clientInfo.ToolCardInteruptValues;
import it.polimi.ingsw.shared.network.commands.responses.Response;

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
