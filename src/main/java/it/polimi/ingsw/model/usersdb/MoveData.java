package it.polimi.ingsw.model.usersdb;
import it.polimi.ingsw.model.clientModel.*;

import java.util.ArrayList;

public class MoveData {
    public NextAction nextAction=null;
    public ClientDiceLocations wherePickNewDice=null;
    public ClientDiceLocations wherePutNewDice=null;
    public ClientWpc wpc=null;
    public ArrayList<ClientDice> extractedDices=null;
    public ClientRoundTrack roundTrack=null;
    public Integer diceChosenId =0;
    public ArrayList<Integer> numbersToChoose=null;
    public boolean moveFinished=false;
    public boolean canceledToolCard =false;
    public Exception exception=null;

    public MoveData(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, Integer diceChosenId,
                    ArrayList<Integer> numbersToChoose, boolean moveFinished, boolean canceledToolCard, Exception exception) {
        this.nextAction = nextAction;
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
        this.canceledToolCard = canceledToolCard;
        this.exception = exception;
    }


    public MoveData(Exception exception) {
        this.exception = exception;
    }


    public MoveData(NextAction nextAction, boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, Integer diceChosenId){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.moveFinished=moveFinished;
    }

    public MoveData(NextAction nextAction, boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, Integer diceChosenId, ArrayList<Integer> numbersToChoose){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }


    public MoveData(NextAction nextAction, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, Integer diceChosenId, boolean canceledToolCard){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.canceledToolCard = canceledToolCard;
    }

    public MoveData(NextAction nextAction, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, Integer diceChosenId, ArrayList<Integer> numbersToChoose, boolean canceledToolCard){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
        this.canceledToolCard = canceledToolCard;
    }

    public MoveData(NextAction nextAction, boolean moveFinished, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, Integer diceChosenId,
                    ArrayList<Integer> numbersToChoose) {
        this.nextAction = nextAction;
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData(boolean moveFinished, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, Integer diceChosenId,
                    ArrayList<Integer> numbersToChoose) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData(boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, Integer diceChosenId){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.moveFinished=moveFinished;
    }

    public MoveData(boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, Integer diceChosenId, ArrayList<Integer> numbersToChoose){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData(boolean moveFinished, boolean canceledToolCard,ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, Integer diceChosenId, ArrayList<Integer> numbersToChoose){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
        this.canceledToolCard=canceledToolCard;
    }

    public MoveData(boolean moveFinished, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack,
                    ArrayList<Integer> numbersToChoose) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData( boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                     ClientRoundTrack roundTrack){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.moveFinished=moveFinished;
    }

    public MoveData( boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                     ClientRoundTrack roundTrack, ArrayList<Integer> numbersToChoose){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData( boolean moveFinished){
        this.moveFinished=moveFinished;
    }

    public MoveData(NextAction nextAction){
        this.nextAction=nextAction;
    }

    public MoveData(NextAction nextAction, ClientDiceLocations wherePickNewDice){
        this.nextAction=nextAction;
        this.wherePickNewDice=wherePickNewDice;
    }

    public void setNextAction(NextAction nextAction) {
        this.nextAction = nextAction;
    }

    public MoveData(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, Integer diceChosenId) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosenId = diceChosenId;
        this.numbersToChoose = numbersToChoose;
     this.nextAction=nextAction;
    }
}