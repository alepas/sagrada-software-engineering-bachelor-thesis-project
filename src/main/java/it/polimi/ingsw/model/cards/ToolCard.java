package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.control.network.commands.notifications.Notification;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public abstract class ToolCard implements Cloneable {
    protected String id;
    protected String name;
    protected String description;
    protected Color colorForDiceSingleUser;
    protected boolean allowPlaceDiceAfterCard;
    protected boolean cardBlocksNextTurn;
    protected boolean cardOnlyInFirstMove;
    protected Boolean used;
    protected Dice diceForSingleUser;
    protected int currentStatus;
    protected PlayerInGame currentPlayer = null;
    protected Game currentGame = null;
    protected boolean singlePlayerGame = false;
    protected String username = null;
    protected ArrayList<Notification> movesNotifications;
    protected ArrayList<ClientDice> tempExtractedDices;
    protected ClientWpc tempClientWpc;
    protected ClientRoundTrack tempRoundTrack;


    public abstract ToolCard getToolCardCopy();

    public int getCurrentStatus() {
        return currentStatus;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean hasBeenUsed() {
        return used;
    }

    public abstract MoveData setCard(PlayerInGame player) throws CannotUseToolCardException;

    public abstract MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException;

    public abstract MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException;

    public abstract MoveData pickNumber(int number) throws CannotPickNumberException, CannotPerformThisMoveException;


    public ClientToolCard getClientToolcard() {
        return new ClientToolCard(id, name, description, used);
    }

    public abstract MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException;


    public abstract MoveData cancelAction() throws CannotCancelActionException;

    protected abstract void cleanCard();

    public abstract MoveData getNextMove();


    protected void updateClientExtractedDices() {
        tempExtractedDices.clear();
        for (Dice tempdice : currentPlayer.getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
    }

    protected void updateClientWPC() {
        tempClientWpc = currentPlayer.getWPC().getClientWpc();
    }

    protected void updateClientRoundTrack() {
        tempRoundTrack = currentGame.getRoundTrack().getClientRoundTrack();
    }

    protected MoveData cancelStatusOne() {
        if (!singlePlayerGame) {
            cleanCard();
            return new MoveData(true, true);
        }
        currentGame.getExtractedDices().add(diceForSingleUser);
        updateClientExtractedDices();
        diceForSingleUser = null;
        this.currentStatus = 0;
        return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);
    }


    protected MoveData cancelStatusZero() throws CannotCancelActionException {
        if (singlePlayerGame) {
            cleanCard();
            return new MoveData(true, true);
        }
        throw new CannotCancelActionException(username, id, 2);
    }


    protected MoveData pickDiceInitializeSingleUserToolCard(int diceId, NextAction nextAction, ClientDiceLocations initial, ClientDiceLocations finish) throws CannotPickDiceException {
        Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
        if (tempDice.getDiceColor() != colorForDiceSingleUser)
            throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.EXTRACTED, 1);
        this.currentStatus = 1;
        this.diceForSingleUser = tempDice;
        currentGame.getExtractedDices().remove(this.diceForSingleUser);
        updateClientExtractedDices();
        return new MoveData(nextAction, initial, finish, null, tempExtractedDices, null, null, null);

    }


    protected MoveData setCardDefault(PlayerInGame player, boolean dicesOnWpc, boolean dicesOnRoundTrack, NextAction nextAction, ClientDiceLocations from, ClientDiceLocations finish) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 9);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);
        if (dicesOnWpc)
            if (player.getWPC().getNumOfDices() == 0)
                throw new CannotUseToolCardException(id, 5);
        if (dicesOnRoundTrack)
            if (player.getUpdatedRoundTrack().getNumberOfDices() == 0)
                throw new CannotUseToolCardException(id, 6);
        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        updateClientExtractedDices();
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            this.currentStatus = 1;
            return new MoveData(nextAction, from, finish);
        }
    }

    protected void defaultClean() {
        if (currentPlayer != null)
            currentPlayer.setToolCardInUse(null);
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.currentGame = null;
        this.username = null;
        this.tempClientWpc = null;
        this.tempRoundTrack = null;
        this.singlePlayerGame = false;
        this.tempExtractedDices = new ArrayList<>();
        this.movesNotifications = new ArrayList<>();
    }


}
