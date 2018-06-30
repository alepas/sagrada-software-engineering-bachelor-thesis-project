package shared.network.commands.responses;

import shared.clientInfo.ClientToolCard;

import java.util.ArrayList;

public class UpdatedToolCardsResponse implements Response {
    public final ArrayList<ClientToolCard> toolCards;
    public final Exception exception;

    public UpdatedToolCardsResponse(ArrayList<ClientToolCard> toolCards) {
        this.toolCards = toolCards;
        this.exception=null;
    }

    public UpdatedToolCardsResponse( Exception exception) {
        this.toolCards = null;
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}