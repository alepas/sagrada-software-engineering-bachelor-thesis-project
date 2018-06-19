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
        this.cardOnlyInFirstMove = true;
        this.used = false;
        this.numbers = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            this.numbers.add(i);
        }
        defaultClean();
        oldChosenDice=null;
        oldDiceExtracted=null;
        chosenDice=null;



    }

    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard11();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player,false,false,NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.DICEBAG);
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            return pickDiceInitializeSingleUserToolCard(diceId, NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG);
        } else throw new CannotPerformThisMoveException(username, 2, false);
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

        updateClientExtractedDices();
        printExtractedDices();
        movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDiceExtracted.getClientDice(), chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG));
        if (currentPlayer.getWPC().isDicePlaceable(chosenDice))
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED);
        this.currentStatus = 30;
        String text = "Il dado non può essere posizionato sulla Window Pattern Card. Continuando verrà riposizionato nei dadi estratti";
        return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, true, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus == 1) {
            oldDiceExtracted = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (pos != null) {
                throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
            }
            currentStatus = 20;

            currentGame.getDiceBag().reInsertDice(oldDiceExtracted);
            currentGame.getExtractedDices().remove(oldDiceExtracted);
            chosenDice = currentGame.getDiceBag().pickDice();
            System.out.println(chosenDice.getDiceColor());
            System.out.println(chosenDice.getDiceNumber());
            currentGame.getExtractedDices().add(chosenDice);
            updateClientExtractedDices();
            printExtractedDices();
            String text = "Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.";
            return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);
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

            this.used = true;
            updateClientWPC();
            updateClientExtractedDices();
            printExtractedDices();
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
            case 0: return cancelStatusZero();
            case 1: return cancelStatusOne();
            case 2: {
                currentStatus = 20;

                updateClientExtractedDices();
                String text = "Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);
            }
            case 3:
            case 30:{
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
        defaultClean();
        oldChosenDice=null;
        oldDiceExtracted=null;
        chosenDice=null;

    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:  return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG, null, tempExtractedDices, null, null, null);
            case 20:
                String text = "Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);
            case 2:
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);

            case 30:
                String text2 = "Il dado non può essere posizionato sulla Window Pattern Card. Continuando verrà riposizionato nei dadi estratti";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text2, false, true);
            case 3:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED);
        }
        return null;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        if (currentStatus == 20) {
            currentStatus = 2;
            updateClientExtractedDices();
            if (value == ToolCardInteruptValues.YES)
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            if (value != ToolCardInteruptValues.NO)
                throw new CannotInteruptToolCardException(username, id);
            ArrayList<ClientDice> tempExtracted = tempExtractedDices;
            movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDiceExtracted.getClientDice(), chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG));
            currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempExtractedDices, null));
            this.used = true;
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);
        }

        if (currentStatus != 30)
            throw new CannotInteruptToolCardException(username, id);
        updateClientExtractedDices();
        ArrayList<ClientDice> tempExtracted = tempExtractedDices;
        currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempExtractedDices, null));
        this.used = true;
        cleanCard();
        return new MoveData(true, null, tempExtracted, null);
    }

    private void printExtractedDices() {
        System.out.print("Dices:");
        for (ClientDice dice : tempExtractedDices)
            System.out.print(" id: " + dice.getDiceID());
        System.out.println("");
    }


}
