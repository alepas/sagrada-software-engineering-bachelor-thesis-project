package it.polimi.ingsw.shared.network.commands.requests;

import it.polimi.ingsw.shared.network.commands.responses.Response;

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

