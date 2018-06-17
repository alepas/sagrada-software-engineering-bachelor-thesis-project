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
        this.maxCancelStatus = 0;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
        this.movesNotifications = new ArrayList<>();
        this.tempExtractedDices = new ArrayList<>();

    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard7();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 0);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);
        if (player.getTurnForRound()!=2)
            throw new CannotUseToolCardException(id, 8);

        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        moveCancellable=true;
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        updateClientExtractedDices();
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame=true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED);
        }
        else {
            this.currentStatus = 1;
            this.moveCancellable=false;
            for (Dice extDice:currentGame.getExtractedDices()){
                extDice.rollDice();
            }
            updateClientExtractedDices();
            this.used = true;
            ArrayList<ClientDice> tempExtracted = tempExtractedDices;
            this.movesNotifications.add(new ToolCardExtractedDicesModifiedNotification(username));
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempExtractedDices, null));
            cleanCard();
            return new MoveData(true, null, tempExtracted, null);
        }
    }



    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (tempDice.getDiceColor() != colorForDiceSingleUser)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.EXTRACTED, 1);
            this.currentStatus = 1;
            this.moveCancellable=false;
            this.diceForSingleUser = tempDice;
            currentGame.getExtractedDices().remove(this.diceForSingleUser);
            for (Dice extDice:currentGame.getExtractedDices()){
                extDice.rollDice();
            }
            updateClientExtractedDices();
            this.used = true;
            ArrayList<ClientDice> tempExtracted = tempExtractedDices;
            this.movesNotifications.add(new ToolCardExtractedDicesModifiedNotification(username));
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications, null, tempExtractedDices, null));
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
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame){
                    cleanCard();
                    return new MoveData(true,true);
                }
                throw new CannotCancelActionException(username,id,1);
            }
            case 1: {
                if (singlePlayerGame) {
                    currentGame.getExtractedDices().add(diceForSingleUser);
                    updateClientExtractedDices();
                    diceForSingleUser = null;
                    this.currentStatus = 0;
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);
                }
                else throw new CannotCancelActionException(username,id,1);
            }
        }
        throw new CannotCancelActionException(username,id,1);

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
        this.movesNotifications = new ArrayList<>();
        tempExtractedDices = new ArrayList<>();

    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED,tempExtractedDices);
                else return null;
            }
        }
        return null;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username,id);
    }
}
