package it.polimi.ingsw.shared.network.commands.responses;

public class PassTurnResponse implements Response {
    public final Exception exception;

    public PassTurnResponse(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler){
        handler.handle(this);
    }
}