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

import java.util.ArrayList;

public class ToolCard12 extends ToolCard {
    private DiceAndPosition firstDiceInitial;
    private DiceAndPosition secondDiceInitial;
    private Color chosenColor;

    /**
     * Object constructor: creates a new concrete tool card 12 by setting all his parameters
     */
    public ToolCard12() {
        this.id = ToolCardConstants.TOOLCARD12_ID;
        this.name = ToolCardConstants.TOOL12_NAME;
        this.description = ToolCardConstants.TOOL12_DESCRIPTION;
        this.colorForDiceSingleUser = Color.BLUE;
        this.allowPlaceDiceAfterCard = true;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = false;
        this.used = false;
        defaultClean();
        firstDiceInitial = null;
        secondDiceInitial = null;
        chosenColor = null;
    }

    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard12();
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
     * In this ToolCard this method should be called only when the game is a single player game, the
     *      * first action that a single player must to do use a ToolCard is to pick a dice of the give color
     *
     * @param diceId is the id of the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPickDiceException        if the chosen dice isn't available, for example it is not located where the
     *                                        player has to pick the dice
     * @throws CannotPerformThisMoveException if the game in a multi player game
     */
    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC);
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
     * checks if there's at least a dice with the same color of the chosen dice inside the roundTrack
     *
     * @param dice is the dice chosen by the player
     * @return true if there's at least a dice in the roundTrack with the same color of the chosen dice, false if not
     */
    private boolean checkColorInRoundTrack(Dice dice) {
        ArrayList<Dice> roundTrackDices = cardRoundTrack.getDicesNotUsed();
        for (Dice tempDice : roundTrackDices)
            if (tempDice.getDiceColor() == dice.getDiceColor())
                return true;
        return false;
    }

    /**
     * this method is composed by actions that can be done in two different status:
     * - if the status is equal to 1 and none of the exceptions is thrown it removes the dice from its first position and
     * places it in the new one; after those operation it sets the current status equals to 20 and sends to the player
     * a stop message in which asks to the player if he/she wants to modify the position of a second dice.
     *
     * - if the status is equal to 2 it means that the player has chosen to place an other dice, if none of the
     * exceptions is thrown it modifies the dice position and ends the use of this
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
            if (pos == null)
                throw new CannotPerformThisMoveException(username, 2, false);
            if (!checkColorInRoundTrack(tempDice))
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), Color.getClientColor(tempDice.getDiceColor()), ClientDiceLocations.WPC, 2);
            chosenColor = tempDice.getDiceColor();
            cardWpc.removeDice(firstDiceInitial.getPosition());
            if (!cardWpc.addDiceWithAllRestrictions(tempDice, pos)) {
                cardWpc.addDicePersonalizedRestrictions(tempDice, firstDiceInitial.getPosition(), false, false, false, false, false);
                throw new CannotPickPositionException(username, pos);
            }
            this.currentStatus = 20;
            updateAndCopyToGameData(true, true, false);
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            String text = "Vuoi spostare un altro dado dello stesso colore del dado appena spostato?";
            return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, tempClientWpc, null, null, null, null, null, false);
        }

        if (currentStatus == 2) {
            secondDiceInitial = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.WPC);
            Dice tempDice = secondDiceInitial.getDice();
            if (pos == null)
                throw new CannotPerformThisMoveException(username, 2, false);
            if (tempDice.getDiceColor() != chosenColor)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), Color.getClientColor(tempDice.getDiceColor()), ClientDiceLocations.WPC, 2);
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
                case 2:
                    currentStatus = 20;
                    return getNextMove();
                case 1:
                    return cancelStatusOne();
                case 0:
                    return cancelStatusZero();
            }
            throw new CannotCancelActionException(username, id, 1);

        }
        switch (currentStatus) {
            case 2:
                currentStatus = 20;
            case 20:
                try {
                    return interruptToolCard(ToolCardInteruptValues.NO);
                } catch (CannotInteruptToolCardException e) {
                    //impossible
                }
        }
        return cancelCardFinalAction();
    }

    /**
     * Sets null all attributes of this and calls the default clean method
     */
    @Override
    protected void cleanCard() {
        defaultClean();
        firstDiceInitial = null;
        secondDiceInitial = null;
        chosenColor = null;
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
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, tempClientWpc, null, null, null, null);

            case 20:
                String text = "Vuoi spostare un altro dado dello stesso colore del dado appena spostato?";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, tempClientWpc, null, null, null, null, null, false);
        }
        return null;
    }

    /**
     * this method should be called only after the placement of the first dice when the current status is equal to 20.
     * If the value is equal to YES means that the player wants to modify the position of a second dice, if it is NO
     * the player wants to stop the tool card without modify the position of a second dice.
     *
     * @param value can be YES; NO; OK
     * @return all the information related to the next action that the player will have to do and all new parameters
     * created by the call of this method
     * @throws CannotInteruptToolCardException if the current status is different from 20
     */
    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        if (currentStatus != 20)
            throw new CannotInteruptToolCardException(username, id);
        currentStatus = 2;
        if (value == ToolCardInteruptValues.YES)
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC, tempClientWpc, null, null, null, null);
        if (value != ToolCardInteruptValues.NO)
            throw new CannotInteruptToolCardException(username, id);
        updateClientWPC();
        updateClientExtractedDices();
        currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempClientExtractedDices, null, currentPlayer.getFavours()));
        this.used = true;
        cleanCard();
        return new MoveData(true, null, null, null);

    }

    //--------------------------------------Methods for Tests-----------------------------------------------------------

    DiceAndPosition getFirstDiceInitial(){return  firstDiceInitial;}

    DiceAndPosition getSecondDiceInitial(){return secondDiceInitial;}
}
