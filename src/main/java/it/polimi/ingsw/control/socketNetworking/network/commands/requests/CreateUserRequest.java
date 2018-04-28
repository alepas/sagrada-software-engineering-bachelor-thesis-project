package it.polimi.ingsw.control.socketNetworking.network.commands.requests;

import it.polimi.ingsw.control.socketNetworking.network.commands.Request;
import it.polimi.ingsw.control.socketNetworking.network.commands.RequestHandler;
import it.polimi.ingsw.control.socketNetworking.network.commands.Response;

public class CreateUserRequest implements Request {
    public final String username;
    public final String password;

    public CreateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
