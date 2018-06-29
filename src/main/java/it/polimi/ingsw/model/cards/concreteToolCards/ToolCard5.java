package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.ToolCardDiceChangedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardDicePlacedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardUsedNotification;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.DiceAndPosition;

import java.util.ArrayList;

public class ToolCard5 extends ToolCard {
    private DiceAndPosition fromExtracted;
    private DiceAndPosition fromRoundTrack;

    public ToolCard5() {
        this.id = ToolCardConstants.TOOLCARD5_ID;
        this.name = ToolCardConstants.TOOL5_NAME;
        this.description = ToolCardConstants.TOOL5_DESCRIPTION;
        this.colorForDiceSingleUser = Color.GREEN;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
        fromExtracted = null;
        fromRoundTrack = null;

    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard5();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, false, true, 0, ClientDiceLocations.EXTRACTED, null, NextAction.SELECT_DICE_TOOLCARD);
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null);
        } else if (currentStatus == 1) {
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


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

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
            }
            throw new CannotCancelActionException(username, id, 1);
        }
        return cancelCardFinalAction();

    }


    @Override
    protected void cleanCard() {
        defaultClean();
        fromExtracted = null;
        fromRoundTrack = null;
    }

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

        }
        return null;
    }

    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }

}
