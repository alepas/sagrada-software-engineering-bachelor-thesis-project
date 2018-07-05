package server.model.cards.concreteToolCards;

import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.DiceAndPosition;
import shared.clientInfo.*;
import shared.constants.ToolcardConstants;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.ToolCardDiceChangedNotification;
import shared.network.commands.notifications.ToolCardDicePlacedNotification;
import shared.network.commands.notifications.ToolCardUsedNotification;

import java.util.ArrayList;

public class ToolCard5 extends ToolCard {
    private DiceAndPosition fromExtracted;
    private DiceAndPosition fromRoundTrack;

    /**
     * Object constructor: creates a new concrete tool card 5 setting all his parameters
     */
    public ToolCard5() {
        this.id = ToolCardConstants.TOOLCARD5_ID;
        this.name = ToolcardConstants.TOOL5_NAME;
        this.description = ToolcardConstants.TOOL5_DESCRIPTION;
        this.colorForDiceSingleUser = Color.GREEN;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
        fromExtracted = null;
        fromRoundTrack = null;

    }

    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard5();
    }


    /**
     * @param player is the player that wants to use the ToolCard
     * @return the first action that the player has to do with the ToolCard
     * @throws CannotUseToolCardException when it is not possible to use te object
     */
    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, false, true, 0, ClientDiceLocations.EXTRACTED, null, NextAction.SELECT_DICE_TOOLCARD);
    }

    /**
     * Checks if the game is a single player or multi player game: if it's a single player game it calls the related
     * method, if it's multi and the status is one the fromExtracted dice is set if is 2 it calls all the methods that
     * makes the swap possible.
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
        else if (currentStatus == 1) {
            this.fromExtracted = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED);
            currentStatus = 2;

            return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.ROUNDTRACK, null, null, null, null, fromExtracted.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
        } else if (currentStatus == 2) {
            this.fromRoundTrack = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.ROUNDTRACK);
            currentStatus = 3;
            int indexDice = cardExtractedDices.indexOf(fromExtracted.getDice());
            cardRoundTrack.swapDice(fromExtracted.getDice(), fromRoundTrack.getPosition());
            cardExtractedDices.set(indexDice, fromRoundTrack.getDice());
            updateClientExtractedDices();
            updateClientRoundTrack();
            movesNotifications.add(new ToolCardDiceChangedNotification(username, fromExtracted.getDice().getClientDice(), fromRoundTrack.getDice().getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.ROUNDTRACK));
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, tempClientRoundTrack, fromRoundTrack.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }

    /**
     * @param number is a number chosen by the player
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
        if (currentStatus != 3)
            throw new CannotPerformThisMoveException(username, 2, false);
        if (pos == null)
            throw new CannotPerformThisMoveException(username, 2, false);
        if (diceId != this.fromRoundTrack.getDice().getId())
            throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
        if (!cardWpc.addDiceWithAllRestrictions(this.fromRoundTrack.getDice(), pos))
            throw new CannotPickPositionException(username, pos);
        cardExtractedDices.remove(this.fromRoundTrack.getDice());
        currentStatus = 4;
        this.used = true;
        updateAndCopyToGameData(true, true, true);
        movesNotifications.add(new ToolCardDicePlacedNotification(username, this.fromRoundTrack.getDice().getClientDice(), pos));
        currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempClientExtractedDices, tempClientRoundTrack, currentPlayer.getFavours()));
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
        cleanCard();
        return new MoveData(true, tempWpc, tempExtracted, null);
    }

    /**
     * goes back to the last action that has been done, it changes all elements. Everything comes back of a step
     *
     * @param all boolean
     * @return all the information related to the previous action that the player has done while using this toolcard
     * @throws CannotCancelActionException if the current status isn't correct or the boolean is false
     */
    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        if (!all) {
            switch (currentStatus) {
                case 3: {
                    cardRoundTrack.swapDice(fromRoundTrack.getDice(), fromRoundTrack.getPosition());
                    int indexDice = cardExtractedDices.indexOf(fromRoundTrack.getDice());
                    cardExtractedDices.set(indexDice, fromExtracted.getDice());
                    updateClientRoundTrack();
                    updateClientExtractedDices();
                    this.fromRoundTrack = null;
                    this.currentStatus = 2;
                    movesNotifications.remove(movesNotifications.size() - 1);
                    return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.ROUNDTRACK, null, null, tempClientExtractedDices, tempClientRoundTrack, fromExtracted.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
                }
                case 2: {
                    this.fromExtracted = null;
                    this.currentStatus = 1;
                    return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED);
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
     * Sets object's parameters to null and creates a new array
     */
    @Override
    protected void cleanCard() {
        defaultClean();
        fromExtracted = null;
        fromRoundTrack = null;
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
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.ROUNDTRACK, null, null, tempClientExtractedDices, null, fromExtracted.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
            case 3:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, tempClientRoundTrack, fromRoundTrack.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
            default:
                return null;
        }
    }

    /**
     * @param value can be YES; NO; OK
     * @return always the exception
     * @throws CannotInteruptToolCardException every time that it is called
     */
    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }

}
