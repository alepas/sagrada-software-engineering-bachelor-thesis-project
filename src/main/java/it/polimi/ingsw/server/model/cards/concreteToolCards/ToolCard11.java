package it.polimi.ingsw.server.model.cards.concreteToolCards;

import it.polimi.ingsw.server.constants.ToolCardConstants;
import it.polimi.ingsw.server.model.cards.ToolCard;
import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.users.MoveData;
import it.polimi.ingsw.server.model.users.PlayerInGame;
import it.polimi.ingsw.shared.clientInfo.*;
import it.polimi.ingsw.shared.exceptions.dicebagExceptions.IncorrectNumberException;
import it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.shared.network.commands.notifications.ToolCardDiceChangedNotification;
import it.polimi.ingsw.shared.network.commands.notifications.ToolCardDicePlacedNotification;
import it.polimi.ingsw.shared.network.commands.notifications.ToolCardUsedNotification;

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
        oldChosenDice = null;
        oldDiceExtracted = null;
        chosenDice = null;


    }

    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard11();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        return setCardDefault(player, false, false, 0, ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG, NextAction.PLACE_DICE_TOOLCARD);
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
        movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDiceExtracted.getClientDice(), chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG));
        if (cardWpc.isDicePlaceable(chosenDice))
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED);
        this.currentStatus = 30;
        String text = "Il dado non può essere posizionato sulla Window Pattern Card. Continuando verrà riposizionato nei dadi estratti";
        return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, true, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus == 1) {
            oldDiceExtracted = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (pos != null) {
                throw new CannotPerformThisMoveException(username, 2, false);
            }
            currentStatus = 20;

            currentGame.getDiceBag().reInsertDice(oldDiceExtracted);
            int indexDice = cardExtractedDices.indexOf(oldDiceExtracted);
            chosenDice = currentGame.getDiceBag().pickDice();
            cardExtractedDices.set(indexDice, chosenDice);
            updateClientExtractedDices();
            String text = "Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.";
            return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);
        }

        if (currentStatus == 3) {
            if (pos == null)
                throw new CannotPerformThisMoveException(username, 2, false);
            if (diceId != this.chosenDice.getId())
                throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
            if (!cardWpc.addDiceWithAllRestrictions(this.chosenDice, pos))
                throw new CannotPickPositionException(username, pos);
            cardExtractedDices.remove(this.chosenDice);
            currentStatus = 4;
            this.used = true;
            updateAndCopyToGameData(true, true, false);
            movesNotifications.add(new ToolCardDicePlacedNotification(username, this.chosenDice.getClientDice(), pos));
            currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempClientExtractedDices, null, currentPlayer.getFavours()));
            ClientWpc tempWpc = tempClientWpc;
            ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
            cleanCard();
            return new MoveData(true, tempWpc, tempExtracted, null);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }


    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        boolean canceledCard = true;
        if (!all) {
            switch (currentStatus) {
                case 3:
                case 30:
                    try {
                        chosenDice.setNumber(oldChosenDice.getDiceNumber());
                    } catch (IncorrectNumberException e) {
                        //impossible
                    }
                    updateClientExtractedDices();
                    this.currentStatus = 2;
                    movesNotifications.remove(movesNotifications.size() - 1);
                    return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempClientExtractedDices, null, this.chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);

                case 2:
                    currentStatus = 20;
                    updateClientExtractedDices();
                    String text = "Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.";
                    return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);


                case 1:
                    return cancelStatusOne();
                case 0:
                    return cancelStatusZero();
            }
            throw new CannotCancelActionException(username, id, 1);

        }
        switch (currentStatus) {
            case 3:
                currentStatus = 30;
            case 30:
                try {
                    return interruptToolCard(ToolCardInteruptValues.OK);
                } catch (CannotInteruptToolCardException e) {
                    //impossible
                }
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
        oldChosenDice = null;
        oldDiceExtracted = null;
        chosenDice = null;

    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:
                return defaultNextMoveStatusZero();
            case 1:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG, null, tempClientExtractedDices, null, null, null);
            case 20:
                String text = "Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, true, false, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, null, false);
            case 2:
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);

            case 30:
                String text2 = "Il dado non può essere posizionato sulla Window Pattern Card. Continuando verrà riposizionato nei dadi estratti";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text2, false, true);
            case 3:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED);
        }
        return null;
    }

    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        if (currentStatus == 20) {
            currentStatus = 2;
            updateClientExtractedDices();
            if (value == ToolCardInteruptValues.YES)
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD, null, tempClientExtractedDices, null, chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers, false);
            if (value != ToolCardInteruptValues.NO)
                throw new CannotInteruptToolCardException(username, id);
            updateAndCopyToGameData(true, false, false);
            ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
            movesNotifications.add(new ToolCardDiceChangedNotification(username, oldDiceExtracted.getClientDice(), chosenDice.getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.DICEBAG));
            currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempClientExtractedDices, null, currentPlayer.getFavours()));
            this.used = true;
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);
        }

        if (currentStatus != 30)
            throw new CannotInteruptToolCardException(username, id);
        if (value != ToolCardInteruptValues.OK)
            throw new CannotInteruptToolCardException(username, id);
        updateAndCopyToGameData(true, false, false);
        ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
        currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempClientExtractedDices, null, currentPlayer.getFavours()));
        this.used = true;

        cleanCard();
        return new MoveData(true, null, tempExtracted, null);
    }


}