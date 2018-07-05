package shared.network.commands.responses;

public class PickWpcResponse implements Response {
    public final Exception exception;

    /**Constructor of this
     *
     * @param exception is the exception that could be thrown
     */
    public PickWpcResponse(Exception exception) {
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
