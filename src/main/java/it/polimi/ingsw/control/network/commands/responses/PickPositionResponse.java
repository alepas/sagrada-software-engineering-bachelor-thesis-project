package it.polimi.ingsw.control.network.commands.responses;

public class PickPositionResponse implements Response {
    public final Exception exception;

    public PickPositionResponse(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
