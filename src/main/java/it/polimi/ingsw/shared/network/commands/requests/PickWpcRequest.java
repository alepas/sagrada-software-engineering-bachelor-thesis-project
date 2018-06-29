package it.polimi.ingsw.shared.network.commands.requests;

import it.polimi.ingsw.shared.network.commands.responses.Response;

public class PickWpcRequest implements Request {
    public final String wpcID;
    public final String userToken;

    public PickWpcRequest(String wpcID, String userToken) {
        this.wpcID = wpcID;
        this.userToken = userToken;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
