package it.polimi.ingsw.shared.network.commands.responses;

import it.polimi.ingsw.shared.clientInfo.*;

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

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}