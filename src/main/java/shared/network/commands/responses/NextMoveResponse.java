package shared.network.commands.responses;

import shared.clientinfo.*;

import java.util.ArrayList;

public class NextMoveResponse implements Response {
    public final NextAction nextAction;
    public final ClientDiceLocations wherePickNewDice;
    public final ClientDiceLocations wherePutNewDice;
    public final ArrayList<Integer> numbersToChoose;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final ClientDice diceChosen;
    public final Exception exception;


    /**
     * Constructor of this.
     *
     * @param nextAction is the action that the player has to do
     * @param wherePickNewDice is the location from where the player must take the dice
     * @param wherePutNewDice is the location where the player must put the dice
     * @param numbersToChoose is an arraylist with all possible number that the player can choose from
     * @param wpc is the player's schema
     * @param extractedDices is the arraylist composed by all extracted dices
     * @param roundTrack is the matrix formed by all dices not used during the game
     * @param diceChosen is the chosen dice
     * @param exception is the exception that could be thrown
     */
    public NextMoveResponse(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice, ArrayList<Integer> numbersToChoose, ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientDice diceChosen, Exception exception) {
        this.nextAction = nextAction;
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.numbersToChoose = numbersToChoose;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.exception = exception;
    }

    /**
     * Constructor of this with an only attribute different from null
     *
     * @param exception is the exception that could be thrown
     */
    public NextMoveResponse(Exception exception) {
        this.exception = exception;
        nextAction = null;
        wherePickNewDice = null;
        numbersToChoose=null;
        wpc=null;
        extractedDices=null;
        roundTrack=null;
        diceChosen = null;
        wherePutNewDice=null;
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