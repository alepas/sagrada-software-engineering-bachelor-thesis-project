package it.polimi.ingsw.shared.network.commands.responses;

import it.polimi.ingsw.shared.clientInfo.ClientDice;

import java.util.ArrayList;

public class UpdatedExtractedDicesResponse implements Response {
    public final ArrayList<ClientDice> extractedDices;
    public final Exception exception;

    public UpdatedExtractedDicesResponse(ArrayList<ClientDice> extractedDices) {
        this.extractedDices = extractedDices;
        this.exception=null;

    }

    public UpdatedExtractedDicesResponse(Exception exception) {
        this.exception = exception;
        this.extractedDices=null;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}