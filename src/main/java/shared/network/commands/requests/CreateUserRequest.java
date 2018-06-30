package shared.network.commands.requests;

import shared.network.commands.responses.Response;

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