package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.*;

import java.util.ArrayList;

public class MoveResponse implements Response {
    public final ClientMoveModifiedThings modifiedElement;      //Eliminare
    public final ClientNextActions nextAction;
    public final ClientDiceLocations wherePickNewDices;
    public final String otherUserForNextAction;                 //Eliminare
    public final String otherUserForWpcModified;                //Eliminare
    public final ClientWpc wpc;
    public final ArrayList<ClientDice> extractedDices;          //Da valutare
    public final ClientRoundTrack roundTrack;                   //Da valutare
    public final ClientDice diceChosen;
    public final ArrayList<ClientColor> colorsToChoose;         //Eliminare
    public final ArrayList<Integer> numbersToChoose;
    public final ClientToolCardStatus cardStatus;               //Eliminare
    public final boolean toolCardInUseForNextMove;              //Eliminare
    public final boolean stoppableCard;                         //Eliminare: aggiungere STOPTOOLCARD a nextactione
    public final Exception exception;

    public MoveResponse(MoveResponse moveResponse, ClientNextActions newNextAction){
        this.modifiedElement = moveResponse.modifiedElement;
        this.nextAction = newNextAction;
        this.wherePickNewDices = moveResponse.wherePickNewDices;
        this.otherUserForNextAction = moveResponse.otherUserForNextAction;
        this.otherUserForWpcModified= moveResponse.otherUserForWpcModified;
        this.wpc=moveResponse.wpc;
        this.extractedDices = moveResponse.extractedDices;
        this.roundTrack = moveResponse.roundTrack;
        this.diceChosen = moveResponse.diceChosen;
        this.colorsToChoose = moveResponse.colorsToChoose;
        this.numbersToChoose = moveResponse.numbersToChoose;
        this.toolCardInUseForNextMove = moveResponse.toolCardInUseForNextMove;
        this.exception = null;
        this.stoppableCard = moveResponse.stoppableCard;
        this.cardStatus=moveResponse.cardStatus;

    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, String otherUserForWpcModified, ClientWpc wpc, ArrayList<ClientDice> extractedDices, ClientRoundTrack roundTrack, ArrayList<ClientColor> colorsToChoose, ArrayList<Integer> numbersToChoose, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.otherUserForWpcModified= otherUserForWpcModified;
        this.wpc=wpc;
        this.extractedDices = extractedDices;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.colorsToChoose = colorsToChoose;
        this.numbersToChoose = numbersToChoose;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.exception = null;
        this.stoppableCard = stoppableCard;
        this.cardStatus=cardStatus;
    }



    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, String otherUserForWpcModified, ClientWpc wpc,  boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.otherUserForWpcModified= otherUserForWpcModified;
        this.wpc=wpc;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        this.exception=null;
        this.cardStatus=cardStatus;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, ArrayList<ClientDice> extractedDices, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.extractedDices = extractedDices;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.stoppableCard = stoppableCard;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        this.cardStatus=cardStatus;

    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, ClientRoundTrack roundTrack, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.roundTrack = roundTrack;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction,  boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.roundTrack = null;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices,  boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.otherUserForNextAction = null;
        this.roundTrack = null;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, ArrayList<ClientDice> extractedDices, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.otherUserForNextAction = null;
        this.roundTrack = null;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=extractedDices;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, String otherUserForWpcModified, ClientWpc wpc,  boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.otherUserForWpcModified= otherUserForWpcModified;
        this.wpc=wpc;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        this.exception=null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientDice> extractedDices, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = extractedDices;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
        this.roundTrack = null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientRoundTrack roundTrack,  boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.roundTrack = roundTrack;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }



    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientColor> colorsToChoose, ArrayList<Integer> numbersToChoose, String otherUserForWpcModified, ClientWpc wpc,  boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.otherUserForWpcModified= otherUserForWpcModified;
        this.wpc=wpc;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.numbersToChoose =numbersToChoose;
        this.colorsToChoose =colorsToChoose;
        this.roundTrack = null;
        this.exception=null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientColor> colorsToChoose, ArrayList<Integer> numbersToChoose, ArrayList<ClientDice> extractedDices, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = extractedDices;
        this.stoppableCard = stoppableCard;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =numbersToChoose;
        this.exception=null;
        this.colorsToChoose =colorsToChoose;
        this.roundTrack = null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientColor> colorsToChoose, ArrayList<Integer> numbersToChoose, ClientRoundTrack roundTrack,  boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = null;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.wherePickNewDices = null ;
        this.otherUserForNextAction = null;
        this.roundTrack = roundTrack;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =numbersToChoose;
        this.exception=null;
        this.colorsToChoose =colorsToChoose;
    }


    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, String otherUserForWpcModified, ClientWpc wpc, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.otherUserForWpcModified= otherUserForWpcModified;
        this.wpc=wpc;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        this.exception=null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, ArrayList<ClientDice> extractedDices, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.extractedDices = extractedDices;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
        this.roundTrack = null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, ClientRoundTrack roundTrack, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.roundTrack = roundTrack;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, String otherUserForNextAction, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = otherUserForNextAction;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.roundTrack = null;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.otherUserForNextAction = null;
        this.roundTrack = null;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices, ArrayList<ClientDice> extractedDices, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.otherUserForNextAction = null;
        this.roundTrack = null;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=extractedDices;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, String otherUserForWpcModified, ClientWpc wpc, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.otherUserForWpcModified= otherUserForWpcModified;
        this.wpc=wpc;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        this.exception=null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientDice> extractedDices, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = extractedDices;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
        this.roundTrack = null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientRoundTrack roundTrack, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.roundTrack = roundTrack;
        this.cardStatus=cardStatus;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.exception=null;
        this.colorsToChoose =null;
    }



    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientColor> colorsToChoose, ArrayList<Integer> numbersToChoose, String otherUserForWpcModified, ClientWpc wpc, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.otherUserForWpcModified= otherUserForWpcModified;
        this.wpc=wpc;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.numbersToChoose =numbersToChoose;
        this.colorsToChoose =colorsToChoose;
        this.roundTrack = null;
        this.exception=null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientColor> colorsToChoose, ArrayList<Integer> numbersToChoose, ArrayList<ClientDice> extractedDices, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = extractedDices;
        this.stoppableCard = stoppableCard;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =numbersToChoose;
        this.exception=null;
        this.colorsToChoose =colorsToChoose;
        this.roundTrack = null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientColor> colorsToChoose, ArrayList<Integer> numbersToChoose, ClientRoundTrack roundTrack, ClientDice diceChosen, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.diceChosen = diceChosen;
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus=cardStatus;
        this.wherePickNewDices = null ;
        this.otherUserForNextAction = null;
        this.roundTrack = roundTrack;
        this.stoppableCard = stoppableCard;
        this.extractedDices=null;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =numbersToChoose;
        this.exception=null;
        this.colorsToChoose =colorsToChoose;
    }


    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, boolean toolCardInUseForNextMove, ClientToolCardStatus cardStatus, boolean stoppableCard) {
        this.toolCardInUseForNextMove = toolCardInUseForNextMove;
        this.cardStatus = null;
        this.exception = null;
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = null;
        this.stoppableCard = stoppableCard;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        this.diceChosen = null;
    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction) {
        this.cardStatus = null;
        this.exception = null;
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = null;
        this.stoppableCard = false;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        toolCardInUseForNextMove=false;
        this.diceChosen = null;

    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDice diceChosen) {
        this.cardStatus = null;
        this.exception = null;
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = null;
        this.stoppableCard = false;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        toolCardInUseForNextMove=false;
        this.diceChosen = diceChosen;

    }




    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ArrayList<ClientDice> extractedDices, ClientDice diceChosen) {
        this.cardStatus = null;
        this.exception = null;
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = extractedDices;
        this.stoppableCard = false;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        toolCardInUseForNextMove=false;
        this.diceChosen = diceChosen;

    }


    //ClientMoveModifiedThings.WINDOW,ClientNextActions.MOVEFINISHED,clientWpc


    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientWpc wpc) {
        this.cardStatus = null;
        this.exception = null;
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = null;
        this.stoppableCard = false;
        this.otherUserForWpcModified=null;
        this.wpc=wpc;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        toolCardInUseForNextMove=false;
        this.diceChosen = null;

    }

    public MoveResponse(ClientMoveModifiedThings modifiedElement, ClientNextActions nextAction, ClientDiceLocations wherePickNewDices) {
        this.cardStatus = null;
        this.exception = null;
        this.modifiedElement = modifiedElement;
        this.nextAction = nextAction;
        this.wherePickNewDices = wherePickNewDices;
        this.otherUserForNextAction = null;
        this.extractedDices = null;
        this.stoppableCard = false;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        toolCardInUseForNextMove=false;
        this.diceChosen = null;

    }

    public MoveResponse(Exception exception) {
        this.exception = exception;
        this.modifiedElement = null;
        this.nextAction = null;
        this.wherePickNewDices = null;
        this.otherUserForNextAction = null;
        this.extractedDices = null;
        this.stoppableCard = false;
        this.otherUserForWpcModified=null;
        this.wpc=null;
        this.numbersToChoose =null;
        this.colorsToChoose =null;
        this.roundTrack = null;
        this.cardStatus=null;
        this.toolCardInUseForNextMove=false;
        this.diceChosen = null;

    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}