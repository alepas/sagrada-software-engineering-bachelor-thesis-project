package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.NextAction;
import it.polimi.ingsw.model.clientModel.ClientRoundTrack;
import it.polimi.ingsw.model.clientModel.ClientWpc;

import java.util.ArrayList;

public class PlaceDiceResponse implements Response {
    public final NextAction nextAction;
    public final ClientWpc wpc;                             //Da eliminare
    public final ArrayList<ClientDice> extractedDices;      //Da eliminare
    public final ClientRoundTrack roundTrack;               //Da eliminare
    public final Exception exception;


    public PlaceDiceResponse(NextAction nextAction, ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, Exception exception) {
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.exception = exception;
    }

    public PlaceDiceResponse(Exception exception) {
        this.exception = exception;
        nextAction = null;
        wpc = null;
        extractedDices = null;
        roundTrack = null;
    }

    @Override
    public void handle(ResponseHandler handler) { handler.handle(this); }
}
