package shared.network.commands.responses;

public class CreateUserResponse implements Response {
    public final String username;
    public final String userToken;
    public final Exception exception;

    /**
     * Constructor of this.
     *
     * @param username is the username of the new player
     * @param userToken is the token assigned to the new player in the server controller method
     * @param exception is the possible exception that could be thrown in case of problems
     */
    public CreateUserResponse(String username, String userToken, Exception exception) {
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
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
