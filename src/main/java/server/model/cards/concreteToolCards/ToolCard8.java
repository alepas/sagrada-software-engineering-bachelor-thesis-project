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
import shared.network.commands.notifications.DicePlacedNotification;
import shared.network.commands.notifications.ToolCardDicePlacedNotification;
import shared.network.commands.notifications.ToolCardUsedNotification;

import java.util.ArrayList;

public class ToolCard8 extends ToolCard {
    private DiceAndPosition diceAndPosition;

    /**
     * Object constructor: creates a new concrete tool card 8 setting all his parameters
     */
    public ToolCard8() {
        this.id = ToolCardConstants.TOOLCARD8_ID;
        this.name = ToolCardConstants.TOOL8_NAME;
        this.description = ToolCardConstants.TOOL8_DESCRIPTION;
        this.colorForDiceSingleUser = Color.RED;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = true;
        this.cardOnlyInFirstMove = false;
        this.used = false;
        defaultClean();
    }

    /**
     * @return a copy of this object
     */
    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard8();
    }

    /**
     * Calls the setCardInitialStep() method if nobody is already using this tool card  the Player in game is set equal
     * to the player else it throws an exception. If the game is a single player game the next action will be the
     * SELECT_DICE_TO_ACTIVATE_TOOLCARD else it will check if a dice is already been placed by the user and then, if not,
     * will return a new moveData with nextAction equals to PLACE_DICE_TOOLCARD.
     *
     * @param player is the player that wants to use the ToolCard
     * @return the first action that the player has to do with the ToolCard
     * @throws CannotUseToolCardException when it is not possible to use te object
     */
    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        setCardInitialStep(player, false, false, 1);
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            if (currentPlayer.isPlacedDiceInTurn())
                this.currentStatus = 10;
            else this.currentStatus = 1;
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC);
        }
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
    public MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException {
        if (currentStatus == 1) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();

            if (pos == null)
                throw new CannotPerformThisMoveException(username, 2, false);

            if (!cardWpc.addDiceWithAllRestrictions(tempDice, pos))
                throw new CannotPickPositionException(username, pos);

            diceAndPosition= new DiceAndPosition(tempDice,pos);
            cardExtractedDices.remove(tempDice);
            updateClientWPC();
            updateClientExtractedDices();
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            if (verifyExtractedDicesPlaceable()) {
                this.currentStatus = 2;
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, tempClientWpc, tempClientExtractedDices, null, null, null);
            } else {
                this.currentStatus = 30;
                String text = "Nessun dado tra quelli estratti può essere posizionato sulla Window Pattern Card.\n" +
                        "Il primo piazzamento è considerato valido. L'utilizzo della toolCard è stato annullato.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, false, null, tempClientExtractedDices,null,null,null,null,false);
            }

        } else if ((currentStatus == 2) || (currentStatus == 10)) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (pos == null)
                throw new CannotPerformThisMoveException(username, 2, false);

            if (!cardWpc.addDiceWithAllRestrictions(tempDice, pos))
                throw new CannotPickPositionException(username, pos);
            cardExtractedDices.remove(tempDice);
            this.used = true;
            updateAndCopyToGameData(true, true, false);
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempClientExtractedDices, null, currentPlayer.getFavours()));
            ClientWpc tempWpc = tempClientWpc;
            ArrayList<ClientDice> tempDices = tempClientExtractedDices;
            cleanCard();
            return new MoveData(true, tempWpc, tempDices, null);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }


    /**
     * In this ToolCard this method should be called only when the game is a single player game, the
     * first action that a single player must to do use a ToolCard is to pick a dice of the give color
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
        if ((currentStatus == 0) && (singlePlayerGame)) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (tempDice.getDiceColor() != colorForDiceSingleUser)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.EXTRACTED, 1);
            this.diceForSingleUser = tempDice;
            cardExtractedDices.remove(this.diceForSingleUser);
            updateClientExtractedDices();
            if (currentPlayer.isPlacedDiceInTurn())
                this.currentStatus = 10;
            else this.currentStatus = 1;
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, null, null);
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
                case 10:
                    return cancelStatusOne();
                case 0:
                    return cancelStatusZero();
            }
            throw new CannotCancelActionException(username, id, 1);
        }

        switch (currentStatus) {
            case 0:
            case 1:
            case 10:
                return cancelCardFinalAction();
            case 2:
                currentStatus = 30;
            case 30:
                try {
                    return interruptToolCard(ToolCardInteruptValues.OK);
                } catch (CannotInteruptToolCardException e) {
                    //impossible
                }
        }
        throw new CannotCancelActionException(username, id, 0);
    }

    /**
     * Calls the default cleaner
     */
    @Override
    protected void cleanCard() {
        defaultClean();
        diceAndPosition=null;
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
            case 10:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, tempClientWpc, tempClientExtractedDices, null, null, null);

            case 30:
                String text = "Nessun dado tra quelli estratti può essere posizionato sulla Window Pattern Card.\n" +
                        "Il primo piazzamento è considerato valido. L'utilizzo della toolCard è stato annullato.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, false, null, tempClientExtractedDices,null,null,null,null,false);

        }
        return null;
    }


    /**
     * @return true if it is possible to place the dice, false if not
     */
    private boolean verifyExtractedDicesPlaceable() {
        boolean condition = true;
        boolean tempcondition;
        for (Dice dice : cardExtractedDices) {
            tempcondition = cardWpc.isDicePlaceable(dice);
            condition = condition || tempcondition;
        }
        return condition;
    }

    /**
     * if the current status is 30 or the value is different from OK it throws the exception, else interrupts the
     * tool card in a correct way.
     *
     * @param value can be YES; NO; OK
     * @return the new moveData
     * @throws CannotInteruptToolCardException everytime that it is called
     */
    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        if (currentStatus != 30)
            throw new CannotInteruptToolCardException(username, id);
        if (value != ToolCardInteruptValues.OK)
            throw new CannotInteruptToolCardException(username, id);
        if (singlePlayerGame)
            cardExtractedDices.add(diceForSingleUser);
        updateAndCopyToGameData(true, true, false);
        currentGame.changeAndNotifyObservers(new DicePlacedNotification(username, this.diceAndPosition.getDice().getClientDice(), this.diceAndPosition.getPosition(), tempClientWpc, tempClientExtractedDices, null));
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempDices = tempClientExtractedDices;
        cleanCard();
        return new MoveData(null, tempWpc, tempDices, null, null, null, true);
    }
}
