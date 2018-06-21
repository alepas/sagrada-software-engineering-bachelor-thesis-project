/*
package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.ToolCardDicePlacedNotification;
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

public class ToolCard9 extends ToolCard {

    public ToolCard9() {
        this.id = ToolCardConstants.TOOLCARD9_ID;
        this.name = ToolCardConstants.TOOL9_NAME;
        this.description = ToolCardConstants.TOOL9_DESCRIPTION;
        this.colorForDiceSingleUser = Color.YELLOW;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard9();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player,false,false,NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC);
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 1)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
        if (pos == null) {
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        }
        if (!currentPlayer.getWPC().addDicePersonalizedRestrictions(tempDice, pos, true, true, true, false, true)) {
            throw new CannotPickPositionException(username, pos);
        }
        currentGame.getExtractedDices().remove(tempDice);
        this.used = true;
        updateClientWPC();
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
        currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, null, null, favours));
        ArrayList<ClientDice> tempDices = tempExtractedDices;
        ClientWpc tempWpc = tempClientWpc;
        cleanCard();
        return new MoveData(true, tempWpc, tempDices, null);
    }


    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0: return cancelStatusZero();
            case 1: return cancelStatusOne();
        }
        throw new CannotCancelActionException(username, id, 1);

    }


    @Override
    protected void cleanCard() {
        defaultClean();

    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:  return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, null, null);

        }
        return null;
    }


    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }
}*/
