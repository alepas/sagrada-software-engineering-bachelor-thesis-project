/*
package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardUsedNotification;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public class ToolCard7 extends ToolCard {
    private ArrayList<ClientDice> tempExtractedDices;


    public ToolCard7() {
        this.id = ToolCardConstants.TOOLCARD7_ID;
        this.name = ToolCardConstants.TOOL7_NAME;
        this.description = ToolCardConstants.TOOL7_DESCRIPTION;
        this.colorForDiceSingleUser = Color.BLUE;
        this.allowPlaceDiceAfterCard = true;
        this.cardBlocksNextTurn = false;
        this.maxCancelStatus = 0;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard7();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 0);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);
        if (player.getTurnForRound()!=2)
            throw new CannotUseToolCardException(id, 2);
        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            this.currentStatus = 1;
            for (Dice extDice:currentGame.getExtractedDices()){
                extDice.rollDice();
            }
            updateClientExtractedDices();

            this.used = true;

            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, id));
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED);

        }
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            if (diceId.getDiceColor() != colorForDiceSingleUser)
                throw new CannotPickDiceException(username, diceId.getDiceNumber(), diceId.getDiceColor(), location, 1);
            this.currentStatus = 1;
            this.diceForSingleUser = diceId;
            currentGame.getExtractedDices().remove(diceId);
            updateClientExtractedDices();
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null);
        }
        if (currentStatus != 1)
            throw new CannotPerformThisMoveException(username, 2, false);
        currentStatus = 2;
        this.dice = diceId;
        diceId.rollDice();
        updateClientExtractedDices();
        if (currentPlayer.getWPC().isDicePlaceable(diceId))
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, this.dice.getId());
        else {
            ArrayList<ClientDice> tempExtracted = tempExtractedDices;
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);
        }

    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 2)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (finishLocation != ClientDiceLocations.WPC)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (diceId.getId() != this.dice.getId())
            throw new CannotPickDiceException(username, diceId.getId(), ClientDiceLocations.EXTRACTED, 3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(diceId, pos))
            throw new CannotPickPositionException(username, pos);
        currentStatus = 3;
        currentGame.getExtractedDices().remove(diceId);
        updateClientWPC();
        updateClientExtractedDices();
        currentPlayer.getGame().changeAndNotifyObservers(new DicePlacedNotification(username, diceId.getClientDice(), pos, tempClientWpc, tempExtractedDices, null));
        currentPlayer.setToolCardUsedInTurn(true);
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempExtractedDices;
        cleanCard();
        return new MoveData(true, tempWpc, tempExtracted, null);

    }


    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame) {
                    cleanCard();
                    return new MoveData(true, true, null, null, null, null, null);
                }
                return null;
            }
            case 1: {
                if (!singlePlayerGame) {
                    cleanCard();
                    return new MoveData(true, true, null, null, null, null, null);

                }
                currentGame.getExtractedDices().add(diceForSingleUser);
                updateClientExtractedDices();
                diceForSingleUser = null;
                return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null);
            }
        }
        return null;

    }

    private void updateClientExtractedDices() {
        for (Dice tempdice : currentPlayer.getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
    }


    private void updateClientWPC() {
        tempClientWpc = currentPlayer.getWPC().getClientWpc();
    }


    @Override
    protected void cleanCard() {
        currentPlayer.setToolCardInUse(null);
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
        this.singlePlayerGame = false;
        this.tempClientWpc = null;
        this.tempExtractedDices = null;
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
                else return null;
            }
            case 1:
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, this.dice.getId());
        }
        return null;
    }
}
*/
