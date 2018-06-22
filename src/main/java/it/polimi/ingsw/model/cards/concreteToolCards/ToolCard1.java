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

public class ToolCard1 extends ToolCard {
    private Dice dice;
    private Dice oldDice;
    private ArrayList<Integer> numbers;

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
        this.oldDice=null;
        this.dice = null;
        numbers = new ArrayList<>();
    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard1();
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
        movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDice.getClientDice(), dice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.EXTRACTED));
        return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 3)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (pos == null)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (diceId != this.dice.getId())
            throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(this.dice, pos))
            throw new CannotPickPositionException(username, pos);
        currentGame.getExtractedDices().remove(this.dice);
        currentStatus = 4;
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



   /* @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0:
                return cancelStatusZero();
            case 1:
                return cancelStatusOne();
            case 2: {
                this.dice = null;
                this.currentStatus = 1;
                this.numbers.clear();
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED);

            }
            case 3: {
                try {
                    dice.setNumber(oldDice.getDiceNumber());
                } catch (IncorrectNumberException e) {
                    throw new CannotCancelActionException(username, this.id, 3);
                }
                updateClientExtractedDices();
                this.currentStatus = 2;
                movesNotifications.remove(movesNotifications.size() - 1);
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, this.dice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            }
        }
        throw new CannotCancelActionException(username, id, 1);

    }*/



    @Override
    protected void cleanCard() {
      defaultClean();
        this.oldDice=null;
        this.dice = null;
        numbers = new ArrayList<>();
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:
                return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, this.dice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);


            case 3:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, dice.getClientDice(), ClientDiceLocations.EXTRACTED);

        }
        return null;
    }



    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        MoveData temp;
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
                if (!all)
                    return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, this.dice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            }
            case 2: {
                this.dice = null;
                this.currentStatus = 1;
                this.numbers.clear();
                if (!all)
                    return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED);

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


}

