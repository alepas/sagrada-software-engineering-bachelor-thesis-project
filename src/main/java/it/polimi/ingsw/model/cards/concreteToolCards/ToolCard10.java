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
        this.maxCancelStatus = 3;
        this.cardOnlyInFirstMove = true;
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
        this.dice=null;
        this.singlePlayerGame=false;
        tempExtractedDices=new ArrayList<>();
        movesNotifications=new ArrayList<>();
}

    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard10();
    }


    @Override
    public MoveData setCard(PlayerInGame player) throws CannotUseToolCardException {
        if ((currentPlayer != null) || (currentStatus != 0)) {
            throw new CannotUseToolCardException(id, 0);
        }
        if (cardOnlyInFirstMove)
            if (player.isPlacedDiceInTurn())
                throw new CannotUseToolCardException(id, 2);

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
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);

        }
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus==0)&&(singlePlayerGame)){
            Dice tempDice=currentPlayer.dicePresentInLocation(diceId,ClientDiceLocations.EXTRACTED).getDice();
            if (tempDice.getDiceColor()!=colorForDiceSingleUser)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(),ClientDiceLocations.EXTRACTED, 1);
            this.currentStatus = 1;
            moveCancellable=true;
            this.diceForSingleUser= tempDice;
            currentGame.getExtractedDices().remove(this.diceForSingleUser);
            updateClientExtractedDices();
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null, null);
        }
        if(currentStatus!=1)
            throw new CannotPerformThisMoveException(username,2,false);
        this.currentStatus=2;
        Dice tempDice=currentPlayer.dicePresentInLocation(diceId,ClientDiceLocations.EXTRACTED).getDice();
        this.dice= tempDice;
        this.moveCancellable=true;
        oldDice=this.dice.copyDice();
        this.dice.turnDiceOppositeSide();
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDiceChangedNotification(username,oldDice.getClientDice(),dice.getClientDice(),ClientDiceLocations.EXTRACTED,ClientDiceLocations.EXTRACTED));
        return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,null,dice.getClientDice(), ClientDiceLocations.EXTRACTED);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
            throw new CannotPerformThisMoveException(username,2,false);
          }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if(currentStatus!=2)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (pos==null)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (diceId!=this.dice.getId())
            throw new CannotPickDiceException(username, diceId,ClientDiceLocations.EXTRACTED,3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(this.dice, pos))
            throw new CannotPickPositionException(username, pos);
        currentGame.getExtractedDices().remove(this.dice);
        currentStatus=3;
        moveCancellable=false;
        this.used = true;
        updateClientWPC();
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDicePlacedNotification(username, this.dice.getClientDice(),pos));
        currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username,this.getClientToolcard(),movesNotifications,tempClientWpc,tempExtractedDices,null));
        ClientWpc tempWpc=tempClientWpc;
        ArrayList<ClientDice> tempExtracted=tempExtractedDices;
        cleanCard();
        return new MoveData(true,tempWpc,tempExtracted,null);
    }



    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus){
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
                currentGame.getExtractedDices().remove(dice);
                currentGame.getExtractedDices().add(oldDice);
                updateClientExtractedDices();
                this.dice=null;
                this.oldDice=null;
                this.currentStatus=1;
                movesNotifications.remove(movesNotifications.size()-1);
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null,null);

            }
        }
        throw new CannotCancelActionException(username,id,1);

    }


    @Override
    protected void cleanCard(){
        currentPlayer.setToolCardInUse(null);
        this.diceForSingleUser=null;
        this.currentPlayer=null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
        this.dice=null;
        this.singlePlayerGame=false;
        this.tempClientWpc=null;
        this.tempExtractedDices=new ArrayList<>();
        this.oldDice=null;
        this.movesNotifications=new ArrayList<>();
        moveCancellable=false;

    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED,tempExtractedDices);
                else return null;
            }
            case 1: return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null, null);
            case 2: return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,null,dice.getClientDice(), ClientDiceLocations.EXTRACTED);

        }
        return null;
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username,id);
    }



}
