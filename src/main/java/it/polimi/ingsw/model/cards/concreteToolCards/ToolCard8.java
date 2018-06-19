package it.polimi.ingsw.model.cards.concreteToolCards;

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
import it.polimi.ingsw.model.wpc.DiceAndPosition;
import it.polimi.ingsw.model.wpc.Wpc;

import java.util.ArrayList;

public class ToolCard8 extends ToolCard {

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


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard8();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 0);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);
        System.out.println("turno per il player: " + player.getTurnForRound());
        if (player.getTurnForRound() != 1)
            throw new CannotUseToolCardException(id, 7);

        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        updateClientExtractedDices();
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            if (currentPlayer.isPlacedDiceInTurn())
                this.currentStatus = 10;
            else this.currentStatus = 1;
            System.out.println("non ho ancora messo nessun dado, ne devo mettere 2");
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC);

        }
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException {
        if (currentStatus == 1) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (pos == null) {
                throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
            }

            if (!currentPlayer.getWPC().addDiceWithAllRestrictions(tempDice, pos)) {
                throw new CannotPickPositionException(username, pos);
            }
            currentPlayer.getUpdatedExtractedDices().remove(tempDice);
            updateClientWPC();
            updateClientExtractedDices();
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            if (verifyExtractedDicesPlaceable()) {
                this.currentStatus = 2;
                System.out.println("ho posto per mettere un altro dado");
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, tempClientWpc, tempExtractedDices, null, null, null);
            } else {
                this.currentStatus = 30;
                String text = "Nessun dado tra quelli estratti può essere posizionato sulla Window Pattern Card.\n" +
                        "Il primo piazzamento è considerato valido. L'utilizzo della toolCard è stato annullato.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, false);
            }

        } else if ((currentStatus == 2) || (currentStatus == 10)) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (pos == null) {
                throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
            }

            if (!currentPlayer.getWPC().addDiceWithAllRestrictions(tempDice, pos)) {
                throw new CannotPickPositionException(username, pos);
            }
            currentPlayer.getUpdatedExtractedDices().remove(tempDice);
            updateClientWPC();
            updateClientExtractedDices();
            movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(), pos));
            this.used = true;
            currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, tempClientWpc, tempExtractedDices, null));
            ClientWpc tempWpc = tempClientWpc;
            ArrayList<ClientDice> tempDices = tempExtractedDices;
            cleanCard();
            return new MoveData(true, tempWpc, tempDices, null);
        } else throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);


    }

    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (tempDice.getDiceColor() != colorForDiceSingleUser)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.EXTRACTED, 1);
            this.diceForSingleUser = tempDice;
            currentGame.getExtractedDices().remove(this.diceForSingleUser);
            updateClientExtractedDices();
            if (currentPlayer.isPlacedDiceInTurn())
                this.currentStatus = 10;
            else this.currentStatus = 1;
            System.out.println("non ho ancora messo nessun dado, ne devo mettere 2");
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, null, null);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPickNumberException, CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0:
                return cancelStatusZero();
            case 1:
                return cancelStatusOne();

            case 2: {
                throw new CannotCancelActionException(username, id, 1);
            }
            case 10: {
                cleanCard();
                return new MoveData(true, true);
            }
        }
        throw new CannotCancelActionException(username, id, 1);

    }

    @Override
    protected void cleanCard() {
        defaultClean();
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0:
                return defaultNextMoveStatusZero();
            case 1:
            case 10:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, tempClientWpc, tempExtractedDices, null, null, null);

            case 30:
                String text = "Nessun dado tra quelli estratti può essere posizionato sulla Window Pattern Card.\n" +
                        "Il primo piazzamento è considerato valido. L'utilizzo della toolCard è stato annullato.";
                return new MoveData(NextAction.INTERRUPT_TOOLCARD, text, false, false);

        }
        return null;
    }

    private boolean verifyExtractedDicesPlaceable() {
        boolean condition = true;
        boolean tempcondition;
        for (Dice dice : currentPlayer.getUpdatedExtractedDices()) {
            tempcondition = currentPlayer.getWPC().isDicePlaceable(dice);
            System.out.println("Dado " + dice + " è posizionabile: " + tempcondition);
            condition = condition || tempcondition;
        }
        return condition;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        if (currentStatus != 30)
            throw new CannotInteruptToolCardException(username, id);
        if (value != ToolCardInteruptValues.OK)
            throw new CannotInteruptToolCardException(username, id);
        this.used = false;
        updateClientExtractedDices();
        updateClientWPC();
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempDices = tempExtractedDices;
        cleanCard();
        return new MoveData(null, tempWpc, tempDices, null, null, null, true);
    }
}