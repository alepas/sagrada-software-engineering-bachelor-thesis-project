package it.polimi.ingsw.shared.network.commands.responses;

public class LoginResponse implements Response {
    public final String username;
    public final String userToken;
    public final Exception exception;

    public LoginResponse(String username, String userToken, Exception exception) {
        this.username = username;
        this.userToken = userToken;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler){
        handler.handle(this);
    }
}
