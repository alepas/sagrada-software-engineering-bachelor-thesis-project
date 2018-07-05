package shared.network.commands.requests;

import shared.network.commands.responses.Response;

public class LoginRequest implements Request {
    public final String username;
    public final String password;

    /**
     * Constructor of this.
     *
     * @param username is the account's username
     * @param password is the account's password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}

