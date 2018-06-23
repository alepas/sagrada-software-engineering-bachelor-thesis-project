package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.ToolCardDiceChangedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardDicePlacedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardUsedNotification;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public class ToolCard10 extends ToolCard {
    private Dice dice;
    private Dice oldDice;

    public ToolCard10() {
        this.id = ToolCardConstants.TOOLCARD10_ID;
        this.name = ToolCardConstants.TOOL10_NAME;
        this.description = ToolCardConstants.TOOL10_DESCRIPTION;
        this.colorForDiceSingleUser = Color.GREEN;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
        dice=null;
        oldDice=null;
    }

    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard10();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player,false,false,NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null);
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null);
        }
        if (currentStatus != 1)
            throw new CannotPerformThisMoveException(username, 2, false);
        this.currentStatus = 2;
        Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
        this.dice = tempDice;

        oldDice = this.dice.copyDice();
        this.dice.turnDiceOppositeSide();
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDice.getClientDice(), dice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.EXTRACTED));
        return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 2)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (pos == null)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (diceId != this.dice.getId())
            throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(this.dice, pos))
            throw new CannotPickPositionException(username, pos);
        currentGame.getExtractedDices().remove(this.dice);
        currentStatus = 3;

        this.used = true;
        updateClientWPC();
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDicePlacedNotification(username, this.dice.getClientDice(), pos));
        currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempExtractedDices, null, currentPlayer.getFavours()));
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempExtractedDices;
        cleanCard();
        return new MoveData(true, tempWpc, tempExtracted, null);
    }



    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        MoveData temp;
        switch (currentStatus) {
            case 2: {
                currentGame.getExtractedDices().remove(dice);
                currentGame.getExtractedDices().add(oldDice);
                updateClientExtractedDices();
                this.dice = null;
                this.oldDice = null;
                this.currentStatus = 1;
                movesNotifications.remove(movesNotifications.size() - 1);
                if (!all)
                    return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);

            }

            case 1:
                if (!all) return cancelStatusOne();
            case 0:
                if (!all){
                    return cancelStatusZero();
                }

        }
        if (!all)
            throw new CannotCancelActionException(username, id, 1);
        if (currentStatus==1){
            if (singlePlayerGame){
                currentGame.getExtractedDices().add(diceForSingleUser);
            }
        }
        updateClientWPC();
        updateClientExtractedDices();
        updateClientRoundTrack();
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempExtractedDices;
        ClientRoundTrack tempRound = tempRoundTrack;
        cleanCard();
        return new MoveData(true, true, tempWpc,tempExtracted,tempRound,null,null,null);

    }



    @Override
    protected void cleanCard() {
        defaultClean();
        dice=null;
        oldDice=null;

    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:
                return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);

        }
        return null;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }


}
