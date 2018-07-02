package server.model.cards.concreteToolCards;

import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import shared.clientInfo.*;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.ToolCardExtractedDicesModifiedNotification;
import shared.network.commands.notifications.ToolCardUsedNotification;

import java.util.ArrayList;

public class ToolCard7 extends ToolCard {

    /**
     * Object constructor: creates a new concrete tool card 7 by setting all its parameters
     */
    public ToolCard7() {
        this.id = ToolCardConstants.TOOLCARD7_ID;
        this.name = ToolCardConstants.TOOL7_NAME;
        this.description = ToolCardConstants.TOOL7_DESCRIPTION;
        this.colorForDiceSingleUser = Color.BLUE;
        this.allowPlaceDiceAfterCard = true;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
    }

    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard7();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        setCardInitialStep(player, false, false, 2);
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            this.currentStatus = 1;
            for (Dice extDice : cardExtractedDices) {
                extDice.rollDice();
            }
            this.used = true;
            updateAndCopyToGameData(true, false, false);
            ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
            this.movesNotifications.add(new ToolCardExtractedDicesModifiedNotification(username));
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempClientExtractedDices, null, currentPlayer.getFavours()));
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);
        }
    }

    /**
     * If the game is a single player game is calls the method that initialize the use of a tool card by a single player;
     * if the game is a multi player game it changes all dices of the extracted dices arrayList
     *
     * @param diceId is the id of the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     *         by the call of this method
     * @throws CannotPickDiceException if the chosen dice isn't available, for example it is not located where the
     *         player has to pick the dice
     * @throws CannotPerformThisMoveException if the game in a multi player game
     */
    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            pickDiceInitializeSingleUserToolCard(diceId, null, null, null);
            for (Dice extDice : cardExtractedDices) extDice.rollDice();
            this.used = true;
            updateAndCopyToGameData(true, false, false);
            ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
            this.movesNotifications.add(new ToolCardExtractedDicesModifiedNotification(username));
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempClientExtractedDices, null, currentPlayer.getFavours()));
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);

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
     * @param diceId is the id of the chosen dice
     * @param pos    is the position where the player would like to place the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPerformThisMoveException every time that this method is called in this tool card
     */
    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }


    /**
     * If the status is 0 it calls the default methods else it throws the exception.
     *
     * @param all boolean
     * @return all the information related to the previous action that the player has done while using this toolcard
     * @throws CannotCancelActionException if the current status isn't correct or the boolean is false
     */
    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        if (currentStatus == 0)
            return cancelStatusZero();
        else throw new CannotCancelActionException(username, id, 1);
    }


    /**
     * Calls the default cleaner
     */
    @Override
    protected void cleanCard() { defaultClean(); }

    /**
     * @return all the information related to the next action that the player will have to do and all new parameters
     * created by the call of this method
     */
    @Override
    public MoveData getNextMove() {
        if(currentStatus == 0) return defaultNextMoveStatusZero();
        return null;
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
