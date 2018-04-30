package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

public class CreateUserResponse implements Response {
    public final String username;
    public final String userToken;
    public final String error;

    public CreateUserResponse(String username, String userToken, String error) {
        this.username = username;
        this.userToken = userToken;
        this.error = error;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
