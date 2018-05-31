package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class ToolCardUseRequest implements Request {
    public final String userToken;
    public final String toolCardId;


    public ToolCardUseRequest(String userToken, String toolCardId) {
        this.userToken = userToken;
        this.toolCardId = toolCardId;

    }




    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
