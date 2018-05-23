package it.polimi.ingsw.control.network.commands.responses;

public class CreateUserResponse implements Response {
    public final String username;
    public final String userToken;
    public final Exception exception;

    public CreateUserResponse(String username, String userToken, Exception exception) {
        this.username = username;
        this.userToken = userToken;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
