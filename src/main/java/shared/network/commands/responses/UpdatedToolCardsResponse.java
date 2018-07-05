package shared.network.commands.responses;

import shared.clientinfo.ClientToolCard;

import java.util.ArrayList;

public class UpdatedToolCardsResponse implements Response {
    public final ArrayList<ClientToolCard> toolCards;
    public final Exception exception;

    /**
     * Constructor of this.
     *
     * @param toolCards is the arraylist composed by the public objective cards
     */
    public UpdatedToolCardsResponse(ArrayList<ClientToolCard> toolCards) {
        this.toolCards = toolCards;
        this.exception=null;
    }

    /**
     * Constructor of this.
     *
     * @param exception is the exception that could be thrown
     */
    public UpdatedToolCardsResponse( Exception exception) {
        this.toolCards = null;
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