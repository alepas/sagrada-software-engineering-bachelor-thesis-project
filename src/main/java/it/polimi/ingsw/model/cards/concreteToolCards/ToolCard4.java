package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
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
import it.polimi.ingsw.model.wpc.DiceAndPosition;
import it.polimi.ingsw.model.wpc.Wpc;

import java.util.ArrayList;

public class ToolCard4 extends ToolCard {
    private DiceAndPosition firstDiceInitial;
    private Position firstDiceFinalPos;
    private DiceAndPosition secondDiceInitial;


    public ToolCard4() {
        this.id = ToolCardConstants.TOOLCARD4_ID;
        this.name = ToolCardConstants.TOOL4_NAME;
        this.description = ToolCardConstants.TOOL4_DESCRIPTION;
        this.colorForDiceSingleUser = Color.YELLOW;
        this.allowPlaceDiceAfterCard = true;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = false;
        this.used = false;
        defaultClean();
        firstDiceInitial = null;
        secondDiceInitial = null;
        firstDiceFinalPos = null;

    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard4();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player,true,false,NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC);
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

        if (currentStatus == 1) {
            firstDiceInitial = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.WPC);
            Dice tempDice = firstDiceInitial.getDice();
            if (pos == null) {
                throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
            }
            currentPlayer.getWPC().removeDice(firstDiceInitial.getPosition());
            if (!currentPlayer.getWPC().addDiceWithAllRestrictions(tempDice, pos)) {
                currentPlayer.getWPC().addDicePersonalizedRestrictions(tempDice, firstDiceInitial.getPosition(), false, false, false, false, false);
                throw new CannotPickPositionException(username, pos);
            }
            firstDiceFinalPos = pos;
            this.currentStatus = 2;
            updateClientWPC();
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, tempClientWpc, null, null, null, null);

        }

        if (currentStatus == 2) {
            secondDiceInitial = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.WPC);
            Dice tempDice = secondDiceInitial.getDice();
            if (pos == null) {
                throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
            }
            currentPlayer.getWPC().removeDice(secondDiceInitial.getPosition());
            if (!currentPlayer.getWPC().addDiceWithAllRestrictions(tempDice, pos)) {
                currentPlayer.getWPC().addDicePersonalizedRestrictions(tempDice, secondDiceInitial.getPosition(), false, false, false, false, false);
                throw new CannotPickPositionException(username, pos);
            }
            this.used = true;
            updateClientWPC();
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempExtractedDices, null));
            ClientWpc tempWpc = tempClientWpc;
            cleanCard();
            return new MoveData(true, tempWpc, null, null);
        } else throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);


    }


    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0: return cancelStatusZero();
            case 1: return cancelStatusOne();

            case 2: {
                Wpc tempWpc = currentPlayer.getWPC();
                tempWpc.removeDice(firstDiceFinalPos);
                tempWpc.addDicePersonalizedRestrictions(firstDiceInitial.getDice(), firstDiceInitial.getPosition(), false, false, false, false, false);
                updateClientWPC();
                firstDiceInitial = null;
                firstDiceFinalPos = null;
                this.currentStatus = 1;
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, null, tempExtractedDices, null, null, null);
            }
        }
        throw new CannotCancelActionException(username, id, 1);

    }

    @Override
    protected void cleanCard() {
        defaultClean();
        firstDiceInitial = null;
        secondDiceInitial = null;
        firstDiceFinalPos = null;
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:
                return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, null, tempExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, tempClientWpc, tempExtractedDices, null, null, null);

        }
        return null;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }

}
