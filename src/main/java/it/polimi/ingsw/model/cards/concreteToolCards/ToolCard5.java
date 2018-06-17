package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardDiceChangedNotification;
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
        this.maxCancelStatus = 3;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
        this.fromExtracted = null;
        this.fromRoundTrack = null;
        this.tempRoundTrack = null;
        this.tempClientWpc = null;
        this.tempExtractedDices = new ArrayList<>();
        this.movesNotifications=new ArrayList<>();

    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard5();
    }

    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 0);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);
        if (player.getUpdatedRoundTrack().getNumberOfDices()==0)
            throw new CannotUseToolCardException(id, 5);

        this.currentPlayer = player;
        this.currentGame = player.getGame();
        this.username = player.getUser();
        currentPlayer.setAllowPlaceDiceAfterCard(allowPlaceDiceAfterCard);
        this.moveCancellable=true;
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        updateClientWPC();
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame = true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED);
        } else {
            this.currentStatus = 1;
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED);

        }
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus == 0) && (singlePlayerGame)) {
            Dice tempDice = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED).getDice();
            if (tempDice.getDiceColor() != colorForDiceSingleUser)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(), ClientDiceLocations.EXTRACTED, 1);
            this.currentStatus = 1;
            this.moveCancellable=true;
            this.diceForSingleUser = tempDice;
            currentGame.getExtractedDices().remove(this.diceForSingleUser);
            updateClientExtractedDices();
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);
        } else if (currentStatus == 1) {
            this.fromExtracted = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.EXTRACTED);
            currentStatus = 2;
            this.moveCancellable=true;
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.ROUNDTRACK, null, null, null, null, fromExtracted.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
        } else if (currentStatus == 2) {
            this.fromRoundTrack = currentPlayer.dicePresentInLocation(diceId, ClientDiceLocations.ROUNDTRACK);
            currentStatus = 3;
            this.moveCancellable=true;
            currentGame.getExtractedDices().remove(fromExtracted.getDice());
            currentGame.getRoundTrack().swapDice(fromExtracted.getDice(), fromRoundTrack.getPosition());
            currentGame.getExtractedDices().add(fromRoundTrack.getDice());
            updateClientExtractedDices();
            updateClientRoundTrack();
            movesNotifications.add(new ToolCardDiceChangedNotification(username, fromExtracted.getDice().getClientDice(), fromRoundTrack.getDice().getClientDice(), ClientDiceLocations.EXTRACTED, ClientDiceLocations.ROUNDTRACK));
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, tempRoundTrack, fromRoundTrack.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
        } else throw new CannotPerformThisMoveException(username, 2, false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        throw new CannotPerformThisMoveException(username, 2, false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if (currentStatus != 3)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (pos == null)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(), 2, false);
        if (diceId != this.fromRoundTrack.getDice().getId())
            throw new CannotPickDiceException(username, diceId, ClientDiceLocations.EXTRACTED, 3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(this.fromRoundTrack.getDice(), pos))
            throw new CannotPickPositionException(username, pos);
        currentGame.getExtractedDices().remove(this.fromRoundTrack.getDice());
        currentStatus = 4;
        this.moveCancellable=false;
        this.used = true;
        updateClientWPC();
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDicePlacedNotification(username, this.fromRoundTrack.getDice().getClientDice(), pos));
        currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username, this.getClientToolcard(), movesNotifications,tempClientWpc,tempExtractedDices,tempRoundTrack));
        ClientWpc tempWpc = tempClientWpc;
        ArrayList<ClientDice> tempExtracted = tempExtractedDices;
        cleanCard();
        return new MoveData(true, tempWpc, tempExtracted, null);
    }


    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame){
                    cleanCard();
                    return new MoveData(true,true);
                }
                return null;
            }
            case 1: {
                if (!singlePlayerGame){
                    cleanCard();
                    return new MoveData(true,true);
                }
                currentGame.getExtractedDices().add(diceForSingleUser);
                updateClientExtractedDices();
                diceForSingleUser=null;
                this.currentStatus=0;
                return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null, null);
            }
            case 2: {
                this.fromExtracted = null;
                this.currentStatus = 1;
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);

            }
            case 3: {
                currentGame.getRoundTrack().swapDice(fromRoundTrack.getDice(), fromRoundTrack.getPosition());
                currentGame.getExtractedDices().remove(fromRoundTrack.getDice());
                currentGame.getExtractedDices().add(fromExtracted.getDice());
                updateClientRoundTrack();
                updateClientExtractedDices();
                this.fromRoundTrack = null;
                this.currentStatus = 2;
                movesNotifications.remove(movesNotifications.size()-1);
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.ROUNDTRACK, null, null, tempExtractedDices, tempRoundTrack, fromExtracted.getDice().getClientDice(),ClientDiceLocations.EXTRACTED);
            }

        }
        return null;

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
        this.fromExtracted = null;
        this.fromRoundTrack = null;
        this.tempRoundTrack = null;
        this.tempClientWpc = null;
        this.tempExtractedDices = new ArrayList<>();
        this.movesNotifications=new ArrayList<>();
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus) {
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, ClientDiceLocations.EXTRACTED,tempExtractedDices);
                else return null;
            }
            case 1:
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, null, null, tempExtractedDices, null, null, null);
            case 2:
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.ROUNDTRACK, null, null, tempExtractedDices, null, fromExtracted.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);
            case 3:
                return new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.EXTRACTED, ClientDiceLocations.WPC, null, tempExtractedDices, tempRoundTrack, fromRoundTrack.getDice().getClientDice(), ClientDiceLocations.EXTRACTED);

        }
        return null;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username,id);
    }

}
