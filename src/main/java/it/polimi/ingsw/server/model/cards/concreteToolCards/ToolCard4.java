package it.polimi.ingsw.server.model.cards.concreteToolCards;

import it.polimi.ingsw.server.constants.ToolCardConstants;
import it.polimi.ingsw.server.model.cards.ToolCard;
import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.users.MoveData;
import it.polimi.ingsw.server.model.users.PlayerInGame;
import it.polimi.ingsw.server.model.wpc.DiceAndPosition;
import it.polimi.ingsw.shared.clientInfo.*;
import it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.shared.network.commands.notifications.ToolCardDicePlacedNotification;
import it.polimi.ingsw.shared.network.commands.notifications.ToolCardUsedNotification;

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
        return setCardDefault(player, true, false, 0, ClientDiceLocations.WPC, ClientDiceLocations.WPC, NextAction.PLACE_DICE_TOOLCARD);
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
                throw new CannotPerformThisMoveException(username, 2, false);
            }
            cardWpc.removeDice(firstDiceInitial.getPosition());
            if (!cardWpc.addDiceWithAllRestrictions(tempDice, pos)) {
                cardWpc.addDicePersonalizedRestrictions(tempDice, firstDiceInitial.getPosition(), false, false, false, false, false);
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
                throw new CannotPerformThisMoveException(username, 2, false);
            }
            cardWpc.removeDice(secondDiceInitial.getPosition());
            if (!cardWpc.addDiceWithAllRestrictions(tempDice, pos)) {
                cardWpc.addDicePersonalizedRestrictions(tempDice, secondDiceInitial.getPosition(), false, false, false, false, false);
                throw new CannotPickPositionException(username, pos);
            }
            this.used = true;
            updateAndCopyToGameData(true, true, false);
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempClientExtractedDices, null, currentPlayer.getFavours()));
            ClientWpc tempWpc = tempClientWpc;
            cleanCard();
            return new MoveData(true, tempWpc, null, null);
        } else throw new CannotPerformThisMoveException(username, 2, false);


    }


    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        if (!all) {
            switch (currentStatus) {
                case 2: {
                    cardWpc.removeDice(firstDiceFinalPos);
                    cardWpc.addDicePersonalizedRestrictions(firstDiceInitial.getDice(), firstDiceInitial.getPosition(), false, false, false, false, false);
                    updateClientWPC();
                    firstDiceInitial = null;
                    firstDiceFinalPos = null;
                    this.currentStatus = 1;
                    movesNotifications.remove(movesNotifications.size() - 1);
                    return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, tempClientWpc, null, null, null, null);
                }
                case 1:
                    return cancelStatusOne();
                case 0:
                    return cancelStatusZero();
            }
            throw new CannotCancelActionException(username, id, 1);
        }
        return cancelCardFinalAction();
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
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, tempClientWpc, tempClientExtractedDices, null, null, null);

        }
        return null;
    }

    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }

}
