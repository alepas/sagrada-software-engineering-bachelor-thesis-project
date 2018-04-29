package it.polimi.ingsw.control.network.commands.requests;

import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.RequestHandler;
import it.polimi.ingsw.control.network.commands.Response;

public class LoginRequest implements Request {
    public final String username;
    public final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

