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
import it.polimi.ingsw.model.wpc.DiceAndPosition;

import java.util.ArrayList;

public class ToolCard2 extends ToolCard {

    /**
     * Object constructor: creates a new concrete tool card 2 by setting all his parameters
     */
    public ToolCard2() {
        this.id = ToolCardConstants.TOOLCARD2_ID;
        this.name = ToolCardConstants.TOOL2_NAME;
        this.description = ToolCardConstants.TOOL2_DESCRIPTION;
        this.colorForDiceSingleUser = Color.BLUE;
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
        return new ToolCard2();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player,true,false,NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC);
    }


    /**
     * @param diceId is the id of the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     *         by the call of this method
     * @throws CannotPickDiceException  if the chosen dice isn't available, for example it is not located where the
     *         player has to pick the dice
     * @throws CannotPerformThisMoveException if the game in a multi player game
     */
    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame))
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.PLACE_DICE_TOOLCARD,
                    ClientDiceLocations.WPC, ClientDiceLocations.WPC);
        else throw new CannotPerformThisMoveException(username, 2, false);
    }


    /**
     * @param number define if the players wants to add one or subtract one to the chosen dice number
     * @return all the information related to the next action that the player will have to do and all new parameters created
     *         by the call of this method
     * @throws CannotPerformThisMoveException every time that this method is called because it is not
     *          possible to pick a number while this card is used
     */
    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    /**
     * if none of the exception has been thrown the method removes the chosen dice from his first position, adds it to
     * the player's wpc and clear the card. The placement doesn't check cell color restrictions.
     *
     * @param diceId is the id of the chosen dice
     * @param pos is the position where the player would like to place the chosen dice
     * @return all the information related to the next action that the player will have to do and all new parameters created
     *         by the call of this method
     * @throws CannotPerformThisMoveException if the status is different from 1 or if the chosen position is null
     * @throws CannotPickPositionException if it is not possible to add the chosen dice to the chosen position
     * @throws CannotPickDiceException if it is not possible to pick the chosen dice
     */
    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 1)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);

        if (pos == null)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);

        DiceAndPosition diceAndPosition = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.WPC);
        Dice tempDice = diceAndPosition.getDice();
        currentPlayer.getWPC().removeDice(diceAndPosition.getPosition());
        if (!currentPlayer.getWPC().addDicePersonalizedRestrictions(tempDice, pos, false, true, true, true, false)) {
            currentPlayer.getWPC().addDicePersonalizedRestrictions(tempDice, diceAndPosition.getPosition(), false, false, false, false, false);
            throw new CannotPickPositionException(username, pos);
        }
        this.used = true;
        updateClientWPC();
        movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
        currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(),
                movesNotifications, tempClientWpc, null, null, currentPlayer.getFavours()));
        ClientWpc tempWpc = tempClientWpc;
        cleanCard();
        return new MoveData(true, tempWpc, null, null);
    }


    /**
     * goes back to the last action that has been done, it changes all elements. Everything comes back on a step
     * @param all //todo
     * @return all the information related to the previous action that the player has done while using this toolcard
     *
     * @throws CannotCancelActionException if the current status isn't correct or the boolean is false
     */
    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        switch (currentStatus) {
            case 1:
                if (!all) return cancelStatusOne();
            case 0:
                if (!all) return cancelStatusZero();
        }
        if (!all)
            throw new CannotCancelActionException(username, id, 1);
        if (currentStatus == 1 && singlePlayerGame)
            currentGame.getExtractedDices().add(diceForSingleUser);
        updateClientWPC();
        updateClientExtractedDices();
        updateClientRoundTrack();
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempExtractedDices;
        ClientRoundTrack tempRound = tempRoundTrack;
        cleanCard();
        return new MoveData(true, true, tempWpc,tempExtracted,tempRound,null,null,null);

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
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC,
                        ClientDiceLocations.WPC, null, tempExtractedDices, null, null, null);
        }
        return null;
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
