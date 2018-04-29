package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;

public class LoginResponse implements Response {
    public final String username;
    public final String userToken;

    public LoginResponse(String username, String userToken) {
        this.username = username;
        this.userToken = userToken;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
