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

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPickPositionException, CannotPerformThisMoveException {
        if (currentStatus == 1) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (pos == null) {
                throw new CannotPerformThisMoveException(username, 2, false);
            }

            if (!cardWpc.addDiceWithAllRestrictions(tempDice, pos)) {
                throw new CannotPickPositionException(username, pos);
            }
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
            if (pos == null) {
                throw new CannotPerformThisMoveException(username, 2, false);
            }

            if (!cardWpc.addDiceWithAllRestrictions(tempDice, pos)) {
                throw new CannotPickPositionException(username, pos);
            }
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
            System.out.println("non ho ancora messo nessun dado, ne devo mettere 2");
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempClientExtractedDices, null, null, null);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPickNumberException, CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

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

    @Override
    protected void cleanCard() {
        defaultClean();
        diceAndPosition=null;
    }

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

    private boolean verifyExtractedDicesPlaceable() {
        boolean condition = true;
        boolean tempcondition;
        for (Dice dice : cardExtractedDices) {
            tempcondition = cardWpc.isDicePlaceable(dice);
            condition = condition || tempcondition;
        }
        return condition;
    }

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
