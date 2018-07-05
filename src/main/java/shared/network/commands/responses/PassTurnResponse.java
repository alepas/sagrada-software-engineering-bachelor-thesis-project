package shared.network.commands.responses;

public class PassTurnResponse implements Response {
    public final Exception exception;

    /**
     * @param exception is the possible exception that could be thrown
     */
    public PassTurnResponse(Exception exception) {
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