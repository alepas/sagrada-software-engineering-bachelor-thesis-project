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

public class ToolCard11 extends ToolCard {
    Dice chosenDice = null;
    ArrayList<Integer> numbers;
    Dice oldChosenDice = null;
    Dice oldDiceExtracted = null;

    public ToolCard11() {
        this.id = ToolCardConstants.TOOLCARD11_ID;
        this.name = ToolCardConstants.TOOL11_NAME;
        this.description = ToolCardConstants.TOOL11_DESCRIPTION;
        this.colorForDiceSingleUser = Color.VIOLET;
        this.allowPlaceDiceAfterCard = false;
        this.cardBlocksNextTurn = false;
        this.maxCancelStatus = 3;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
        this.numbers = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            this.numbers.add(i);
        }
        tempExtractedDices = new ArrayList<>();
        movesNotifications = new ArrayList<>();
    }

    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard11();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 0);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);
        if (player.getWPC().getNumOfDices() == 0)
            throw new CannotUseToolCardException(id, 5);

        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        this.moveCancellable = true;
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        updateClientExtractedDices();
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            this.currentStatus = 1;
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG);
            //return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);


        }
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (tempDice.getDiceColor() != colorForDiceSingleUser)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.EXTRACTED, 1);
            this.currentStatus = 1;
            this.moveCancellable = true;
            this.diceForSingleUser = tempDice;
            currentGame.getExtractedDices().remove(this.diceForSingleUser);
            updateClientExtractedDices();
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG, null, tempExtractedDices, null, null, null);
            //return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null, null);
        }
/*        if (currentStatus == 1) {
            oldDiceExtracted = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            currentStatus=2;
            moveCancellable=false;
            currentGame.getDiceBag().reInsertDice(oldDiceExtracted);
            currentGame.getExtractedDices().remove(oldDiceExtracted);
            chosenDice=currentGame.getDiceBag().pickDice();
            currentGame.getExtractedDices().add(chosenDice);
            updateClientExtractedDices();
            System.out.println("numbers");
            System.out.println(numbers.toString());
            //movesNotifications.add(new ToolCardDiceChangedNotification(username,tempDice.getClientDice(),chosenDice.getClientDice(),ClientDiceLocations.EXTRACTED,ClientDiceLocations.DICEBAG));
            return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD,null,tempExtractedDices,null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers,false);
        }*/

        else throw new CannotPerformThisMoveException(username, 2, false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        if (currentStatus != 2)
            throw new CannotPerformThisMoveException(username, 2, false);
        if ((number < 1) || (number > 6))
            throw new CannotPickNumberException(username, number);

        oldChosenDice = this.chosenDice.copyDice();
        try {
            chosenDice.setNumber(number);
        } catch (IncorrectNumberException e) {
            throw new CannotPickNumberException(username, number);
        }
        currentStatus = 3;
        moveCancellable = true;
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDiceExtracted.getClientDice(), chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG));
        if (currentPlayer.getWPC().isDicePlaceable(chosenDice))
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED);
        this.currentStatus=30;
        String text="Il dado non può essere posizionato sulla Window Pattern Card. Continuando verrà riposizionato nei dadi estratti";
        return new MoveData(NextAction.INTERRUPT_TOOLCARD,text,false,true);

    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus == 1) {
            oldDiceExtracted = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (pos != null) {
                throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
            }
            currentStatus = 2;
            moveCancellable = false;
            currentGame.getDiceBag().reInsertDice(oldDiceExtracted);
            currentGame.getExtractedDices().remove(oldDiceExtracted);
            chosenDice = currentGame.getDiceBag().pickDice();
            currentGame.getExtractedDices().add(chosenDice);
            updateClientExtractedDices();
            return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, null, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
        }

        if (currentStatus == 3) {
            if (pos == null)
                throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
            if (diceId != this.chosenDice.getId())
                throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
            if (!currentPlayer.getWPC().addDiceWithAllRestrictions(this.chosenDice, pos))
                throw new CannotPickPositionException(username, pos);
            currentGame.getExtractedDices().remove(this.chosenDice);
            currentStatus = 4;
            moveCancellable = false;
            this.used = true;
            updateClientWPC();
            updateClientExtractedDices();
            movesNotifications.add(new ToolCardDicePlacedNotification(username, this.chosenDice.getClientDice(), pos));
            currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempExtractedDices, null));
            ClientWpc tempWpc = tempClientWpc;
            ArrayList<ClientDice> tempExtracted = tempExtractedDices;
            cleanCard();
            return new MoveData(true, tempWpc, tempExtracted, null);
        } else throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
    }


    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame) {
                    cleanCard();
                    return new MoveData(true, true);
                }
                return null;
            }
            case 1: {
                if (!singlePlayerGame) {
                    cleanCard();
                    return new MoveData(true, true);
                }
                currentGame.getExtractedDices().add(diceForSingleUser);
                updateClientExtractedDices();
                diceForSingleUser = null;
                this.currentStatus = 0;
                return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);
            }
            case 3: {
                try {
                    chosenDice.setNumber(oldChosenDice.getDiceNumber());
                } catch (IncorrectNumberException e) {
                    throw new CannotCancelActionException(username, this.id, 3);
                }
                updateClientExtractedDices();
                this.currentStatus = 2;
                movesNotifications.remove(movesNotifications.size() - 1);
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, this.chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            }
            case 30: {
                try {
                    chosenDice.setNumber(oldChosenDice.getDiceNumber());
                } catch (IncorrectNumberException e) {
                    throw new CannotCancelActionException(username, this.id, 3);
                }
                updateClientExtractedDices();
                this.currentStatus = 2;
                movesNotifications.remove(movesNotifications.size() - 1);
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, this.chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            }
        }
        throw new CannotCancelActionException(username, id, 1);

    }


    @Override
    protected void cleanCard() {
        currentPlayer.setToolCardInUse(null);
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
        this.singlePlayerGame = false;
        this.tempClientWpc = null;
        this.tempExtractedDices = new ArrayList<>();
        this.movesNotifications = new ArrayList<>();
        this.moveCancellable = false;
        this.chosenDice = null;
        this.oldChosenDice = null;
        this.oldDiceExtracted = null;

    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, tempExtractedDices);
                else return null;
            }
            case 1:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG, null, tempExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            case 3:
                if (currentPlayer.getWPC().isDicePlaceable(chosenDice))
                    return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED);
                return null;
        }
        return null;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        if (currentStatus != 30)
            throw new CannotInteruptToolCardException(username, id);
        updateClientExtractedDices();
        ArrayList<ClientDice> tempExtracted = tempExtractedDices;
        this.used = true;
        cleanCard();
        return new MoveData(true, null, tempExtracted, null);
    }

}
