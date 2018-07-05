package server.model.cards.concretetoolcards;

import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import shared.clientInfo.*;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.ToolCardDiceChangedNotification;
import shared.network.commands.notifications.ToolCardDicePlacedNotification;
import shared.network.commands.notifications.ToolCardUsedNotification;

import java.util.ArrayList;

public class ToolCard10 extends ToolCard {
    private Dice dice;
    private Dice oldDice;

    /**
     * Object constructor: creates a new concrete tool card 10 by setting all his parameters
     */
    public ToolCard10() {
        this.id = ToolCardConstants.TOOLCARD10_ID;
        this.name = ToolCardConstants.TOOL10_NAME;
        this.description = ToolCardConstants.TOOL10_DESCRIPTION;
        this.colorForDiceSingleUser = Color.GREEN;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
        dice = null;
        oldDice = null;
    }

    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard10();
    }

    /**
     * @param player is the player that wants to use the ToolCard
     * @return the first action that the player has to do with the ToolCard
     * @throws CannotUseToolCardException when it is not possible to use te object
     */
    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, false, false, 0, ClientDiceLocations.EXTRACTED, null, NextAction.SELECT_DICE_TOOLCARD);
    }

    /**
     * Checks if the game is a single player or multi player game: if it's a single player game it calls the related
     * method, if it's multi player and the status is equal to 1 the chosen dice is turn on the opposite side. If the
     * dice's number is 1 it will be turn in 6, if 2, in 5, if 3 in 4 etc etc
     *
     * @param diceId is the ID of the dice chosen by the player
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPickDiceException        every time there are problems in the return of the singlePlayer
     * @throws CannotPerformThisMoveException every time that the current state is different from 1, this means that
     *                                        the player is trying to do an action that can't be done in that moment
     */
    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame))
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null);
        if (currentStatus != 1)
            throw new CannotPerformThisMoveException(username, 2, false);
        this.currentStatus = 2;
        this.dice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();

        oldDice = this.dice.copyDice();
        this.dice.turnDiceOppositeSide();
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDice.getClientDice(), dice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.EXTRACTED));
        return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);
    }

    /**
     * @param number is the number chosen by the player
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPerformThisMoveException every time that this method is called because it is not
     *                                        possible to pick a number while this card is used
     */
    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    /**
     * if none of the exception has been thrown the method removes the chosen dice from the extractedDices, adds it to
     * the player's wpc and clear the card.
     *
     * @param diceId is the id of the dice that could be place
     * @param pos    is the position where the player would like to place the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPerformThisMoveException if the state isn't the correct one or the given position is null
     * @throws CannotPickPositionException    if the chosen position doesn't respect sagrada's rules
     * @throws CannotPickDiceException        if the id of the chosen dice is different from the dice modified by this
     */
    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 2)
            throw new CannotPerformThisMoveException(username, 2, false);
        if (pos == null)
            throw new CannotPerformThisMoveException(username, 2, false);
        if (diceId != this.dice.getId())
            throw new CannotPickDiceException(diceId, ClientDiceLocations.EXTRACTED, 3);
        if (!cardWpc.addDiceWithAllRestrictions(this.dice, pos))
            throw new CannotPickPositionException();
        cardExtractedDices.remove(this.dice);
        currentStatus = 3;

        this.used = true;
        updateAndCopyToGameData(true, true, false);
        movesNotifications.add(new ToolCardDicePlacedNotification(username, this.dice.getClientDice(), pos));
        currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempClientExtractedDices, null, currentPlayer.getFavours()));
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
        cleanCard();
        return new MoveData(true, tempWpc, tempExtracted, null);
    }

    /**
     * Goes back to the last action that has been done, it changes all elements. Everything comes back of a step.
     *
     * @param all boolean
     * @return all the information related to the previous action that the player has done while using this toolcard
     * @throws CannotCancelActionException if the current status isn't correct or the boolean is false
     */
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        if (!all) {
            switch (currentStatus) {
                case 2: {
                    cardExtractedDices.remove(dice);
                    cardExtractedDices.add(oldDice);
                    updateClientExtractedDices();
                    this.dice = null;
                    this.oldDice = null;
                    this.currentStatus = 1;
                    movesNotifications.remove(movesNotifications.size() - 1);
                    return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempClientExtractedDices, null, null, null);
                }

                case 1:
                    return cancelStatusOne();
                case 0:
                    return cancelStatusZero();
                default:
                    throw new CannotCancelActionException(username, id, 1);
            }
        }
        return cancelCardFinalAction();
    }

    /**
     * cleans the card at the end of the usage
     */
    @Override
    protected void cleanCard() {
        defaultClean();
        dice = null;
        oldDice = null;
    }

    /**
     * @return all the information related to the next action that the player will have to do and all new parameters
     * created by the call of this method
     */
    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:
                return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempClientExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);
            default:
                return null;
        }
    }

    /**
     * if the current status is 30 or the value is different from OK it throws the exception, else interrupts the
     * tool card in a correct way.
     *
     * @param value can be YES; NO; OK
     * @return the new moveData
     * @throws CannotInterruptToolCardException everytime that it is called
     */
    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInterruptToolCardException {
        throw new CannotInterruptToolCardException(username, id);
    }

    //--------------------------------------Methods for Tests-----------------------------------------------------------

    Dice getOldDice() {
        return oldDice;
    }

    Dice getToolDice() {
        return dice;
    }
}
