package shared.network.commands.responses;

import shared.clientInfo.ClientDice;
import shared.clientInfo.ClientRoundTrack;
import shared.clientInfo.ClientWpc;
import shared.clientInfo.NextAction;

import java.util.ArrayList;

public class PlaceDiceResponse implements Response {
    public final NextAction nextAction;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final Exception exception;


    /**
     * Constructor of this which contains all attributes.
     *
     * @param nextAction is the action done by the player
     * @param wpc is the player schema
     * @param extractedDices is the arraylist composed by the dices extracted from the dicebag
     * @param roundTrack is the matrix containing all dices not used by players during rounds
     * @param exception is the exception that could be thrown
     */
    public PlaceDiceResponse(NextAction nextAction, ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, Exception exception) {
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.exception = exception;
    }

    /**
     * Constructor of this which contains only the exception attribute
     *
     * @param exception  is the exception that could be thrown
     */
    public PlaceDiceResponse(Exception exception) {
        this.exception = exception;
        nextAction = null;
        wpc = null;
        extractedDices = null;
        roundTrack = null;
    }

    /**
     * throws the handler of this.
     *
     * @param handler is the handler related to this response
     */
    @Override
    public void handle(ResponseHandler handler) { handler.handle(this); }
}
