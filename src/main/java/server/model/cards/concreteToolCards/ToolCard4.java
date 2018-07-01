package server.model.cards.concreteToolCards;

import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.DiceAndPosition;
import shared.clientInfo.*;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.ToolCardDicePlacedNotification;
import shared.network.commands.notifications.ToolCardUsedNotification;

public class ToolCard4 extends ToolCard {
    private DiceAndPosition firstDiceInitial;
    private Position firstDiceFinalPos;
    private DiceAndPosition secondDiceInitial;

    /**
     * Object constructor: creates a new concrete tool card 4 by setting all its parameters
     */
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

    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard4();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, true, false, 0, ClientDiceLocations.WPC, ClientDiceLocations.WPC, NextAction.PLACE_DICE_TOOLCARD);
    }


    /**
     * In this ToolCard this method should be called only when the game is a single player game; the
     * first action that a single player must to do use a ToolCard is to pick a dice of the give color
     *
     * @param diceId is the id of the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     *         by the call of this method
     * @throws CannotPickDiceException if the chosen dice isn't available, for example it is not located where the
     *                                 player has to pick the dice
     * @throws CannotPerformThisMoveException if the game in a multi player game
     */
    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }

    /**
     * @param number is a chosen number
     * @return all the information related to the next action that the player will have to do and all new parameters
     * created by the call of this method
     * @throws CannotPerformThisMoveException every time that this method is called because it is not possible to pick
     * a number while this card is used
     */
    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    /**
     * if none of the exception has been thrown the method removes the chosen dice from his first position, adds it to
     * the player's wpc in the new position and clear the card. this method is called twice because this ToolCard let
     * the player move to dices in his/her schema.
     *
     * @param diceId is the id of the chosen dice
     * @param pos    is the position where the player would like to place the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPerformThisMoveException if the status is different from 1 or if the chosen position is null
     * @throws CannotPickPositionException    if it is not possible to add the chosen dice to the chosen position
     * @throws CannotPickDiceException        if it is not possible to pick the chosen dice
     */
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
