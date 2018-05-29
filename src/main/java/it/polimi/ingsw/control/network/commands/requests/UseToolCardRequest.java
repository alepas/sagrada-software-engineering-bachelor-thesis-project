package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

public class UseToolCardRequest implements Request {
    public final String userToken;
    public final String toolCardId;


    public UseToolCardRequest(String userToken, String toolCardId) {
        this.userToken = userToken;
        this.toolCardId = toolCardId;

    }




    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
