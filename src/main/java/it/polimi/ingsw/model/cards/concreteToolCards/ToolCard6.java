package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.ToolCardDiceChangedNotification;
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

import java.util.ArrayList;

public class ToolCard6 extends ToolCard {
    private Dice dice;
    private Dice oldDice;

    public ToolCard6() {

        this.id = ToolCardConstants.TOOLCARD6_ID;
        this.name = ToolCardConstants.TOOL6_NAME;
        this.description = ToolCardConstants.TOOL6_DESCRIPTION;
        this.colorForDiceSingleUser = Color.VIOLET;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
        oldDice = null;
        dice = null;

    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard6();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, false, false, 0, ClientDiceLocations.EXTRACTED, null, NextAction.SELECT_DICE_TOOLCARD);
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null);
        } else if (currentStatus == 1) {
            currentStatus = 2;
            this.dice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            oldDice = this.dice.copyDice();
            this.dice.rollDice();
            updateClientExtractedDices();

            movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDice.getClientDice(), dice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.EXTRACTED));
            if (cardWpc.isDicePlaceable(dice))
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, this.dice.getClientDice(), ClientDiceLocations.EXTRACTED);
            else {
                this.currentStatus = 30;
                String text = "Il dado non può essere posizionato sulla Window Pattern Card. È stato riposizionato nei dadi estratti.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, false);
            }
        } else throw new CannotPerformThisMoveException(username, 2, false);

    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 2)
            throw new CannotPerformThisMoveException(username, 2, false);
        if (pos == null)
            throw new CannotPerformThisMoveException(username, 2, false);
        if (diceId != this.dice.getId())
            throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
        if (!cardWpc.addDiceWithAllRestrictions(this.dice, pos))
            throw new CannotPickPositionException(username, pos);
        cardExtractedDices.remove(this.dice);
        currentStatus = 3;
        this.used = true;
        updateAndCopyToGameData(true, true, true);
        movesNotifications.add(new ToolCardDicePlacedNotification(username, this.dice.getClientDice(), pos));
        currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempClientExtractedDices, null, currentPlayer.getFavours()));
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
        cleanCard();
        return new MoveData(true, tempWpc, tempExtracted, null);
    }


    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        boolean canceledCard = true;

        switch (currentStatus) {
            case 2:
                if (!all) break;
                if (cardWpc.autoAddDice(this.dice))
                    cardExtractedDices.remove(this.dice);
            case 30:
                if (!all) break;
                currentStatus = 1;
                canceledCard = false;
            case 1:
                if (!all) return cancelStatusOne();
            case 0:
                if (!all)
                    return cancelStatusZero();
        }
        if (!all)
            throw new CannotCancelActionException(username, id, 1);

        if (currentStatus == 1) {
            if (singlePlayerGame) {
                cardExtractedDices.add(diceForSingleUser);
            }
        }
        updateAndCopyToGameData(true, true, true);
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
        ClientRoundTrack tempRound = tempClientRoundTrack;
        cleanCard();
        return new MoveData(true, canceledCard, tempWpc, tempExtracted, tempRound, null, null, null);

    }


    @Override
    protected void cleanCard() {
        defaultClean();
        oldDice = null;
        dice = null;
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:
                return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempClientExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, this.dice.getClientDice(), ClientDiceLocations.EXTRACTED);
            case 30:
                String text = "Il dado non può essere posizionato sulla Window Pattern Card. È stato riposizionato nei dadi estratti.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, false);

        }
        return null;
    }

    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        if (currentStatus != 30)
            throw new CannotInteruptToolCardException(username, id);
        if (value != ToolCardInteruptValues.OK)
            throw new CannotInteruptToolCardException(username, id);
        updateClientExtractedDices();
        ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
        this.used = true;
        cleanCard();
        return new MoveData(true, null, tempExtracted, null);
    }
}
