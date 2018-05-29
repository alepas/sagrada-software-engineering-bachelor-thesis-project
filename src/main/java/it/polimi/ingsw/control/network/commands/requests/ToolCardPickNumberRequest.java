package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.responses.Response;

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
