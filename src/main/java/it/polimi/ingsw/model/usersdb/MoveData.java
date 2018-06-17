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
    public ClientDice diceChosen =null;
    public ClientDiceLocations diceChosenLocation=null;
    public ArrayList<Integer> numbersToChoose=null;
    public boolean moveFinished=false;
    public boolean canceledToolCard =false;
    public Exception exception=null;
    public String messageForStop=null;
    public boolean bothYesAndNo=false;
    public boolean showBackButton=false;

    public MoveData(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientDice diceChosen,
                    ClientDiceLocations diceChosenLocation, ArrayList<Integer> numbersToChoose, boolean moveFinished, boolean canceledToolCard, Exception exception) {
        this.nextAction = nextAction;
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
        this.canceledToolCard = canceledToolCard;
        this.exception = exception;
    }


    public MoveData(Exception exception) {
        this.exception = exception;
    }


    public MoveData(NextAction nextAction, boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.moveFinished=moveFinished;
        this.diceChosenLocation = diceChosenLocation;
    }

    public MoveData(NextAction nextAction, boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation, ArrayList<Integer> numbersToChoose){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData(NextAction nextAction, String messageForStop, boolean bothYesAndNo, boolean showBackButton) {
        this.nextAction = nextAction;
        this.messageForStop = messageForStop;
        this.bothYesAndNo = bothYesAndNo;
        this.showBackButton = showBackButton;
    }

    public MoveData(NextAction nextAction, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChooenLocation, boolean canceledToolCard){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChooenLocation;
        this.canceledToolCard = canceledToolCard;
    }

    public MoveData(NextAction nextAction, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChooenLocation, ArrayList<Integer> numbersToChoose, boolean canceledToolCard){
        this.nextAction = nextAction;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChooenLocation;
        this.numbersToChoose = numbersToChoose;
        this.canceledToolCard = canceledToolCard;
    }

    public MoveData(NextAction nextAction, boolean moveFinished, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientDice diceChosen,
                    ArrayList<Integer> numbersToChoose) {
        this.nextAction = nextAction;
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData(boolean moveFinished, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientDice diceChosen,
                    ClientDiceLocations diceChosenLocation, ArrayList<Integer> numbersToChoose) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData(boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.moveFinished=moveFinished;
        this.diceChosenLocation = diceChosenLocation;
    }

    public MoveData(boolean moveFinished, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation, ArrayList<Integer> numbersToChoose){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
    }

    public MoveData(boolean moveFinished, boolean canceledToolCard, ClientWpc wpc, ArrayList<ClientDice> extractedDices,
                    ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation, ArrayList<Integer> numbersToChoose){
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.numbersToChoose = numbersToChoose;
        this.moveFinished=moveFinished;
        this.canceledToolCard=canceledToolCard;
    }

    public MoveData(boolean moveFinished, boolean canceledToolCard){
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

    public MoveData(NextAction nextAction, ClientDiceLocations wherePickNewDice, ArrayList<ClientDice> extractedDices){
        this.nextAction=nextAction;
        this.wherePickNewDice=wherePickNewDice;
        this.extractedDices=extractedDices;
    }


    public void setNextAction(NextAction nextAction) {
        this.nextAction = nextAction;
    }

    public MoveData(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice,
                    ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ClientDice diceChosen, ClientDiceLocations diceChosenLocation) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.wpc = wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.diceChosenLocation = diceChosenLocation;
        this.numbersToChoose = numbersToChoose;
     this.nextAction=nextAction;
    }

    public MoveData(NextAction nextAction, ClientDiceLocations wherePickNewDice, ClientDiceLocations wherePutNewDice) {
        this.wherePickNewDice = wherePickNewDice;
        this.wherePutNewDice = wherePutNewDice;
        this.nextAction=nextAction;
    }

}