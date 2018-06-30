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


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard12();
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


    private boolean checkColorInRoundTrack(Dice dice) {
        ArrayList<Dice> roundTrackDices = cardRoundTrack.getDicesNotUsed();
        for (Dice tempDice : roundTrackDices) {
            if (tempDice.getDiceColor() == dice.getDiceColor())
                return true;
        }
        return false;
    }


    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus == 1) {
            firstDiceInitial = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.WPC);
            Dice tempDice = firstDiceInitial.getDice();
            if (pos == null) {
                throw new CannotPerformThisMoveException(username, 2, false);
            }
            if (!checkColorInRoundTrack(tempDice))
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.WPC, 2);
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
            if (pos == null) {
                throw new CannotPerformThisMoveException(username, 2, false);
            }
            if (tempDice.getDiceColor() != chosenColor)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.WPC, 2);
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
        MoveData temp;
        boolean canceledCard = true;

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

    @Override
    protected void cleanCard() {
        defaultClean();
        firstDiceInitial = null;
        secondDiceInitial = null;
        chosenColor = null;
    }

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
}
