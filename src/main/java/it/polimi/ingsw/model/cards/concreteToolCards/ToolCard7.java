package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.ToolCardExtractedDicesModifiedNotification;
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

public class ToolCard7 extends ToolCard {


    public ToolCard7() {
        this.id = ToolCardConstants.TOOLCARD7_ID;
        this.name = ToolCardConstants.TOOL7_NAME;
        this.description = ToolCardConstants.TOOL7_DESCRIPTION;
        this.colorForDiceSingleUser = Color.BLUE;
        this.allowPlaceDiceAfterCard = true;
        this.cardBlocksNextTurn = false;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        defaultClean();
    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard7();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        setCardInitialStep(player, false, false, 2);
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            this.currentStatus = 1;
            for (Dice extDice : cardExtractedDices) {
                extDice.rollDice();
            }
            this.used = true;
            updateAndCopyToGameData(true, false, false);
            ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
            this.movesNotifications.add(new ToolCardExtractedDicesModifiedNotification(username));
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempClientExtractedDices, null, currentPlayer.getFavours()));
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);
        }
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            pickDiceInitializeSingleUserToolCard(diceId, null, null, null);
            for (Dice extDice : cardExtractedDices) {
                extDice.rollDice();
            }
            this.used = true;
            updateAndCopyToGameData(true, false, false);
            ArrayList<ClientDice> tempExtracted = tempClientExtractedDices;
            this.movesNotifications.add(new ToolCardExtractedDicesModifiedNotification(username));
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempClientExtractedDices, null, currentPlayer.getFavours()));
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);

        } else throw new CannotPerformThisMoveException(username, 2, false);


    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        throw new CannotPerformThisMoveException(username, 2, false);


    }


    @Override
    public MoveData cancelAction(boolean all) throws CannotCancelActionException {
        if (currentStatus == 0)
            return cancelStatusZero();
        else throw new CannotCancelActionException(username, id, 1);
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
        }
        return null;
    }

    @Override
    public MoveData interruptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username, id);
    }
}
