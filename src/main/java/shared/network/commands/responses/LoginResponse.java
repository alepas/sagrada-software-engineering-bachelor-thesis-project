package shared.network.commands.responses;

public class LoginResponse implements Response {
    public final String username;
    public final String userToken;
    public final Exception exception;

    /**
     * @param username is the username of the player that would like to login
     * @param userToken is the token given to the player in the ServerController class
     * @param exception is the possible exception that could be thrown
     */
    public LoginResponse(String username, String userToken, Exception exception) {
        this.username = username;
        this.userToken = userToken;
        this.exception = exception;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(ResponseHandler handler){
        handler.handle(this);
    }
}
