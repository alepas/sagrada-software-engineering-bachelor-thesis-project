package shared.network.commands.responses;

import shared.clientInfo.*;

import java.util.ArrayList;

public class ToolCardResponse implements Response {
    public final NextAction nextAction;
    public final ClientDiceLocations wherePickNewDice;
    public final ClientDiceLocations wherePutNewDice;
    public final ArrayList<Integer> numbersToChoose;
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> extractedDices;
    public final ClientRoundTrack roundTrack;
    public final ClientDice diceChosen;
    public final ClientDiceLocations diceChosenLocation;
    public final Exception exception;
    public final String stringForStopToolCard;
    public final boolean bothYesAndNo;
    public final boolean showBackButton;


    /**
     * @param nextAction is the action done by the player
     * @param wherePickNewDice is the location where the payer has picked the chosen dice
     * @param wherePutNewDice is the location where the the player has to put the dice
     * @param numbersToChoose is the array containing all possible numbers that could modify the dice number
     * @param wpc is the player schema
     * @param extractedDices contains all extracted dices
     * @param roundTrack is the matrix containing all dices not used by players during rounds
     * @param diceChosen is the dice chosen by the player
     * @param diceChosenLocation is the location from where the player has chosen the dice
     * @param exception is the exception that could be thrown
     * @param stringForStopToolCard is the text that could be shown in some exception
     * @param bothYesAndNo is a boolean which raise up the reuqest to the player to continue or not to use the toolcard
     * @param showBackButton is a boolean which can raise up the back button while usig a toolcard
     */
    public ToolCardResponse(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice, ArrayList<Integer> numbersToChoose, ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation, Exception exception, String stringForStopToolCard, boolean bothYesAndNo, boolean showBackButton) {
        this.nextAction = nextAction;
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.numbersToChoose = numbersToChoose;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.exception = exception;
        this.stringForStopToolCard = stringForStopToolCard;
        this.bothYesAndNo = bothYesAndNo;
        this.showBackButton = showBackButton;
    }

    /**
     * Constructor with only the exception
     *
     * @param exception is the exception that could be thrown
     */
    public ToolCardResponse(Exception exception) {
        this.exception = exception;
        nextAction = null;
        wherePickNewDice = null;
        numbersToChoose=null;
        wpc=null;
        extractedDices=null;
        roundTrack=null;
        diceChosen = null;
        wherePutNewDice=null;
        diceChosenLocation=null;
        stringForStopToolCard = null;
        bothYesAndNo = false;
        showBackButton = false;
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