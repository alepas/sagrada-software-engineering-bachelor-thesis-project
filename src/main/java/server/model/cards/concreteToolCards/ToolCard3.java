package server.model.cards.concreteToolCards;

import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.DiceAndPosition;
import shared.clientInfo.*;
import shared.constants.ToolcardConstants;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.ToolCardDicePlacedNotification;
import shared.network.commands.notifications.ToolCardUsedNotification;

public class ToolCard3 extends ToolCard {

    /**
     * Object constructor: creates a new concrete tool card 3 by setting all his parameters
     */
    public ToolCard3() {
        this.id = ToolCardConstants.TOOLCARD3_ID;
        this.name = ToolcardConstants.TOOL3_NAME;
        this.description = ToolcardConstants.TOOL3_DESCRIPTION;
        this.colorForDiceSingleUser = Color.RED;
        this.allowPlaceDiceAfterCard = true;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = false;
        this.used = false;
        defaultClean();

    }

    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard3();
    }

    /**
     * @param player is the player that wants to use the ToolCard
     * @return the first action that the player has to do with the ToolCard
     * @throws CannotUseToolCardException when it is not possible to use te object
     */
    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, true, false, 0, ClientDiceLocations.WPC, ClientDiceLocations.WPC, NextAction.PLACE_DICE_TOOLCARD);
    }

    /**
     * @param diceId is the id of the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPickDiceException        if the chosen dice isn't available, for example it is not located where the
     *                                        player has to pick the dice
     * @throws CannotPerformThisMoveException if the game in a multi player game
     */
    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame))
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC);
        else throw new CannotPerformThisMoveException(username, 2, false);
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
     * if none of the exception has been thrown the method removes the chosen dice from his first position, adds it to
     * the player's wpc and clear the card. The placement doesn't check number restrictions.
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
        if (currentStatus != 1)
            throw new CannotPerformThisMoveException(username, 2, false);
        DiceAndPosition diceAndPosition = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.WPC);
        Dice tempDice = diceAndPosition.getDice();
        if (pos == null) {
            throw new CannotPerformThisMoveException(username, 2, false);
        }
        cardWpc.removeDice(diceAndPosition.getPosition());
        if (!cardWpc.addDicePersonalizedRestrictions(tempDice, pos, true, false, true, true, false)) {
            cardWpc.addDicePersonalizedRestrictions(tempDice, diceAndPosition.getPosition(), false, false, false, false, false);
            throw new CannotPickPositionException(username, pos);
        }
        this.used = true;
        updateAndCopyToGameData(true, true, false);
        movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
        currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, null, null, currentPlayer.getFavours()));
        ClientWpc tempWpc = tempClientWpc;
        cleanCard();
        return new MoveData(true, tempWpc, null, null);
    }

    /**
     * Goes back to the last action that has been done, it changes all elements. Everything comes back of a step
     *
     * @param all boolean
     * @return all the information related to the previous action that the player has done while using this toolcard
     * @throws CannotCancelActionException if the current status isn't correct or the boolean is false
     */
    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        if (!all) {
            switch (currentStatus) {
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
     * Calls the default cleaner
     */
    @Override
    protected void cleanCard() {
        defaultClean();
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
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, null, null);
            default:
                return null;
        }

    }

    /**
     * @param value can be YES; NO; OK
     * @return always the exception
     * @throws CannotInteruptToolCardException everytime that it is called
     */
    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }
}
