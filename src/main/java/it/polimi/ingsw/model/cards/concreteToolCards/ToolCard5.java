package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.DiceChangedNotification;
import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardUsedNotification;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.RoundTrack;
import it.polimi.ingsw.model.usersdb.MoveData;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public class ToolCard5 extends ToolCard {
    private Dice diceFromExtracted;
    private Dice diceFromRoundTrack;
    private ArrayList<ClientDice> tempExtractedDices;
    private ClientRoundTrack tempRoundTrack;
    private ClientWpc tempClientWpc;
    private Position positionInRoundTrack;

    public ToolCard5() {
        this.id = ToolCardConstants.TOOLCARD5_ID;
        this.name = ToolCardConstants.TOOL5_NAME;
        this.description = ToolCardConstants.TOOL5_DESCRIPTION;
        this.colorForDiceSingleUser=Color.GREEN;
        this.allowPlaceDiceAfterCard=false;
        this.cardBlocksNextTurn=false;
        this.maxCancelStatus=3;
        this.cardOnlyInFirstMove=true;
        this.used=false;
        this.diceForSingleUser=null;
        this.currentPlayer=null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
        this.diceFromExtracted=null;
        this.diceFromRoundTrack=null;
    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard5() ;
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
        if (cardBlocksNextTurn) {
            currentPlayer.setCardUsedBlockingTurn(this);
        }
        this.currentPlayer.setToolCardInUse(this);
        this.used = true;
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame=true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED);
        }
        else {
            this.currentStatus = 1;
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, id));
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);

        }
    }


    @Override
    public MoveData pickDice(Dice dice, ClientDiceLocations location) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus==0)&&(singlePlayerGame)){
            if (dice.getDiceColor()!=colorForDiceSingleUser)
                throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),location, 1);
            if (location!=ClientDiceLocations.EXTRACTED)
                throw new CannotPickDiceException(username,dice.getId(),ClientDiceLocations.EXTRACTED,0);
            this.currentStatus = 1;
            this.diceForSingleUser=dice;
            currentGame.getExtractedDices().remove(dice);
            updateClientExtractedDices();
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null);
        }
        else if(currentStatus==1){
            if (location!=ClientDiceLocations.EXTRACTED)
                throw new CannotPerformThisMoveException(username,2,false);
            currentStatus=2;
            this.diceFromExtracted=dice;
            return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.ROUNDTRACK,null,null,null,null,diceFromExtracted.getId());
        }
        else if(currentStatus==2){
            if (location!=ClientDiceLocations.ROUNDTRACK)
                throw new CannotPerformThisMoveException(username,2,false);
            currentStatus=3;
            this.diceFromRoundTrack=dice;
            positionInRoundTrack=currentGame.getRoundTrack().getPositionFromDice(diceFromRoundTrack);
            if (positionInRoundTrack==null)
                throw new CannotPickDiceException(username,dice.getId(),ClientDiceLocations.ROUNDTRACK,0);
            currentGame.getExtractedDices().remove(diceFromExtracted);
            currentGame.getRoundTrack().swapDice(diceFromExtracted,positionInRoundTrack);
            currentGame.getExtractedDices().add(diceFromRoundTrack);
            updateClientExtractedDices();
            updateClientRoundTrack();
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,tempRoundTrack,diceFromRoundTrack.getId());
        }
        else   throw new CannotPerformThisMoveException(username,2,false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        throw new CannotPerformThisMoveException(username,2,false);
    }

    @Override
    public MoveData placeDice(Dice dice, ClientDiceLocations startLocation, ClientDiceLocations finishLocation, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if(currentStatus!=3)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (finishLocation!=ClientDiceLocations.WPC)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (dice.getId()!=this.diceFromRoundTrack.getId())
            throw new CannotPickDiceException(username,dice.getId(),ClientDiceLocations.EXTRACTED,3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(dice, pos))
            throw new CannotPickPositionException(username, pos);
        currentStatus=4;
        currentGame.getExtractedDices().remove(dice);
        updateClientWPC();
        updateClientExtractedDices();
        currentPlayer.getGame().changeAndNotifyObservers(new DicePlacedNotification(username,dice.getClientDice(),pos,tempClientWpc,tempExtractedDices,null));
        currentPlayer.setToolCardUsedInTurn(true);
        ClientWpc tempWpc=tempClientWpc;
        cleanCard();
        return new MoveData(true,tempWpc,tempExtractedDices,null);
    }



    @Override
    public MoveData cancelAction() throws CannotCancelActionException {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame){
                    cleanCard();
                    return new MoveData(true,true,null,null,null,null,null);
                }
                return null;
            }
            case 1: {
                if (!singlePlayerGame){
                    cleanCard();
                    return new MoveData(true,true,null,null,null,null,null);

                }
                currentGame.getExtractedDices().add(diceForSingleUser);
                updateClientExtractedDices();
                diceForSingleUser=null;
                return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null);
            }
            case 2: {
                this.diceFromExtracted=null;
                this.currentStatus=1;
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);

            }
            case 3: {
                currentGame.getRoundTrack().swapDice(diceFromRoundTrack,positionInRoundTrack);
                currentGame.getExtractedDices().remove(diceFromRoundTrack);
                currentGame.getExtractedDices().add(diceFromExtracted);
                updateClientRoundTrack();
                updateClientExtractedDices();
                this.diceFromRoundTrack=null;
                this.currentStatus=2;
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.WPC,null,null,tempExtractedDices,tempRoundTrack,diceFromExtracted.getId());

            }

        }
        return null;

    }

    private void updateClientExtractedDices(){
        for (Dice tempdice:currentPlayer.getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
    }

    private void updateClientRoundTrack(){
        tempRoundTrack=currentGame.getRoundTrack().getClientRoundTrack();
    }

    private void updateClientWPC(){
        tempClientWpc=currentPlayer.getWPC().getClientWpc();
    }

    @Override
    protected void cleanCard(){
        currentPlayer.setToolCardInUse(null);
        this.used=false;
        this.diceForSingleUser=null;
        this.currentPlayer=null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
        this.singlePlayerGame=false;
        this.tempClientWpc=null;
        this.tempExtractedDices=null;
        this.diceFromRoundTrack=null;
        this.diceFromExtracted=null;
        this.positionInRoundTrack=null;
        this.tempRoundTrack=null;
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED);
                else return null;
            }
            case 1:     return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null);
            case 2:            return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.ROUNDTRACK,null,null,null,null,diceFromExtracted.getId());
            case 3:             return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,tempRoundTrack,diceFromRoundTrack.getId());

        }
        return null;
    }


}
