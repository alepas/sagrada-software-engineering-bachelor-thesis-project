package server.model.cards.concreteToolCards;

import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import shared.clientInfo.*;
import shared.exceptions.dicebagExceptions.IncorrectNumberException;
import shared.exceptions.usersAndDatabaseExceptions.*;
import shared.network.commands.notifications.ToolCardDiceChangedNotification;
import shared.network.commands.notifications.ToolCardDicePlacedNotification;
import shared.network.commands.notifications.ToolCardUsedNotification;

import java.util.ArrayList;

public class ToolCard1 extends ToolCard {
    private Dice dice;
    private Dice oldDice;
    private ArrayList<Integer> numbers;

    /**
     * Object constructor: creates a new concrete tool card 1 setting all his parameters
     */
    public ToolCard1() {
        this.id = ToolCardConstants.TOOLCARD1_ID;
        this.name = ToolCardConstants.TOOL1_NAME;
        this.description = ToolCardConstants.TOOL1_DESCRIPTION;
        this.colorForDiceSingleUser = Color.VIOLET;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
        this.oldDice = null;
        this.dice = null;
        numbers = new ArrayList<>();
    }


    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard1();
    }


    /**
     * @param player is the player that wants to use the ToolCard
     * @return the first action that the player has to do with the ToolCard
     * @throws CannotUseToolCardException when it is not possible to use te object
     */
    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, false, false, 0, ClientDiceLocations.EXTRACTED, null, NextAction.SELECT_DICE_TOOLCARD
        );
    }


    /**
     * Checks if the game is a single player or multi player game: if it's a single player game it calls the related
     * method, if it's multi first of all checks if the player can do the action and then searches the dice in the
     * extracted array by calling a multi player game method and checks if the dice number is a bound number or not.
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

        Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
        this.dice = tempDice;
        currentStatus = 2;
        numbers.clear();
        int tempNum = this.dice.getDiceNumber();
        if (tempNum > 1)
            numbers.add(-1);
        if (tempNum < 6)
            numbers.add(1);

        return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, null, null, tempDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
    }


    /**
     * First of all it checks if the parameter respect all rules and if the current state is 2 or not; if no exceptions
     * are thrown it calls the setNumber() methods which modifies the dice number.
     *
     * @param number define if the players wants to add one or subtract one to the chosen dice number
     * @return all the information related to the next action that the player will have to do and all new parameters created
     * by the call of this method
     * @throws CannotPerformThisMoveException every time that the current state is different from 2, this means that
     *                                        the player is trying to do an action that can't be done in that moment
     * @throws CannotPickNumberException      if the parameter is a different number from -1 or +1 or if the chosen dice is 6
     *                                        and the param number is +1 or if the chosen dice is 1 and the param number is -1 (those cases are forbidden
     *                                        by sagrada's rules)
     */
    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        if (currentStatus != 2)
            throw new CannotPerformThisMoveException(username, 2, false);
        if (!((number == -1) || (number == 1)))
            throw new CannotPickNumberException(username, number);

        int tempNum = this.dice.getDiceNumber();
        if (((tempNum == 6) && (number == 1)) || ((tempNum == 1) && (number == -1)))
            throw new CannotPickNumberException(username, number);
        oldDice = this.dice.copyDice();
        try {
            dice.setNumber(tempNum + number);
        } catch (IncorrectNumberException e) {
            throw new CannotPickNumberException(username, number);
        }
        currentStatus = 3;
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDice.getClientDice(),
                dice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.EXTRACTED));
        return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC,
                null, tempClientExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);
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
        if (diceId != this.dice.getId())
            throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
        if (!cardWpc.addDiceWithAllRestrictions(this.dice, pos))
            throw new CannotPickPositionException(username, pos);
        cardExtractedDices.remove(this.dice);
        currentStatus = 4;
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
     * Sets object's parameters to null and creates a new array
     */
    @Override
    protected void cleanCard() {
        defaultClean();
        this.oldDice = null;
        this.dice = null;
        numbers = new ArrayList<>();
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
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null,
                        null, tempClientExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempClientExtractedDices, null,
                        this.dice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            case 3:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC,
                        null, tempClientExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);

        }
        return null;
    }


    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        if (!all) {
            switch (currentStatus) {
                case 3: {
                    try {
                        dice.setNumber(oldDice.getDiceNumber());
                    } catch (IncorrectNumberException e) {
                        throw new CannotCancelActionException(username, this.id, 3);
                    }
                    updateClientExtractedDices();
                    this.currentStatus = 2;
                    movesNotifications.remove(movesNotifications.size() - 1);
                    return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempClientExtractedDices, null, this.dice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
                }
                case 2: {
                    this.dice = null;
                    this.currentStatus = 1;
                    this.numbers.clear();
                    return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED);

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

    //----------------------------------- Used in testing -------------------------------------------------------

    Dice getToolDice() {
        return dice;
    }

    Dice getOldDice() {
        return oldDice;
    }

    int getNumberSize() {
        return numbers.size();
    }
}

