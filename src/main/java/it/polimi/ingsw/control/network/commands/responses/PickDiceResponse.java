package it.polimi.ingsw.control.network.commands.responses;

public class PickDiceResponse implements Response {
    public final Exception exception;

    public PickDiceResponse(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
