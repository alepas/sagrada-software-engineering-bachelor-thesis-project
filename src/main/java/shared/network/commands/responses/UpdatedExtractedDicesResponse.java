package shared.network.commands.responses;

import shared.clientinfo.ClientDice;

import java.util.ArrayList;

public class UpdatedExtractedDicesResponse implements Response {
    public final ArrayList<ClientDice> extractedDices;
    public final Exception exception;

    /**
     * Constructor of this.
     *
     * @param extractedDices are the extracted dices
     */
    public UpdatedExtractedDicesResponse(ArrayList<ClientDice> extractedDices) {
        this.extractedDices = extractedDices;
        this.exception=null;

    }

    /**
     * @param exception is the exception that could be thrown
     */
    public UpdatedExtractedDicesResponse(Exception exception) {
        this.exception = exception;
        this.extractedDices=null;
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