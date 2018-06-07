package it.polimi.ingsw.control.network.commands.responses;

import it.polimi.ingsw.model.clientModel.*;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdatedGameResponse implements Response {
    public String gameID;
    public int gameActualPlayers;
    public int gameNumPlayers;
    public ClientColor[] privateObjectives;
    public HashMap<String, ClientWpc> wpcByUsername;
    public ArrayList<ClientToolCard> gameToolCards;
    public ArrayList<ClientPoc> gamePublicObjectiveCards;
    public int currentRound;
    public ArrayList<ClientDice> extractedDices;
    public int currentTurn;
    public boolean active = false;
    public int favour;
    public final Exception exception;
    public final ClientRoundTrack roundTrack;
    public final NextAction nextAction;
    public final ToolCardClientNextActionInfo nextActionInfo;

    public UpdatedGameResponse(Exception exception) {
        this.exception = null;
        this.gameID = null;
        this.gameActualPlayers = 0;
        this.gameNumPlayers = 0;
        this.privateObjectives = null;
        this.wpcByUsername = null;
        this.gameToolCards = null;
        this.gamePublicObjectiveCards = null;
        this.currentRound = 0;
        this.extractedDices = null;
        this.currentTurn = 0;
        this.active = false;
        this.favour = 0;
        this.roundTrack=null;
        this.nextAction = null;
        this.nextActionInfo = null;
    }


    public UpdatedGameResponse(String gameID, int gameActualPlayers, int gameNumPlayers, ClientColor[] privateObjectives, HashMap<String, ClientWpc> wpcByUsername, ArrayList<ClientToolCard> gameToolCards, ArrayList<ClientPoc> gamePublicObjectiveCards, int currentRound, ArrayList<ClientDice> extractedDices, int currentTurn, boolean active, int favour, ClientRoundTrack roundTrack, NextAction nextAction, ToolCardClientNextActionInfo nextActionInfo) {
        this.gameID = gameID;
        this.gameActualPlayers = gameActualPlayers;
        this.gameNumPlayers = gameNumPlayers;
        this.privateObjectives = privateObjectives;
        this.wpcByUsername = wpcByUsername;
        this.gameToolCards = gameToolCards;
        this.gamePublicObjectiveCards = gamePublicObjectiveCards;
        this.currentRound = currentRound;
        this.extractedDices = extractedDices;
        this.currentTurn = currentTurn;
        this.active = active;
        this.favour = favour;
        this.exception = null;
        this.roundTrack = roundTrack;
        this.nextAction = nextAction;
        this.nextActionInfo = nextActionInfo;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}