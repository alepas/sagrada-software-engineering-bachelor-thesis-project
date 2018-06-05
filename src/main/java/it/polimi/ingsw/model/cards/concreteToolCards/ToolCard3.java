package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
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

public class ToolCard3 extends ToolCard {
    private ArrayList<ClientDice> tempExtractedDices;
    private ClientWpc tempClientWpc;


    public ToolCard3() {
        this.id = ToolCardConstants.TOOLCARD3_ID;
        this.name = ToolCardConstants.TOOL3_NAME;
        this.description = ToolCardConstants.TOOL3_DESCRIPTION;
        this.colorForDiceSingleUser = Color.RED;
        this.allowPlaceDiceAfterCard = true;
        this.cardBlocksNextTurn = false;
        this.maxCancelStatus = 3;
        this.cardOnlyInFirstMove = false;
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
        tempExtractedDices=new ArrayList<>();
    }


    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard3();
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
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC);

        }
    }


    @Override
    public MoveData pickDice(Dice dice, ClientDiceLocations location) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus==0)&&(singlePlayerGame)){
            if (dice.getDiceColor()!=colorForDiceSingleUser)
                throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),location, 1);
            this.currentStatus = 1;
            this.diceForSingleUser=dice;
            currentGame.getExtractedDices().remove(dice);
            updateClientExtractedDices();
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC,null,tempExtractedDices,null,null);
        }
        else throw new CannotPerformThisMoveException(username,2,false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username,2,false);
    }

    @Override
    public MoveData placeDice(Dice dice, ClientDiceLocations startLocation, ClientDiceLocations finishLocation, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if(currentStatus!=1)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (!((startLocation==finishLocation)&&(finishLocation==ClientDiceLocations.WPC))){
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        }
        Position oldPosition=currentPlayer.getWPC().getPositionFromDice(dice.getId());
        if (!currentPlayer.getWPC().addDicePersonalizedRestrictions(dice,pos,true,false,true,true,false))
            throw new CannotPickPositionException(username, pos);
        currentPlayer.getWPC().removeDice(oldPosition);
        updateClientWPC();
        currentPlayer.getGame().changeAndNotifyObservers(new DicePlacedNotification(username,dice.getClientDice(),pos,tempClientWpc,null,null));
        currentPlayer.setToolCardUsedInTurn(true);
        ClientWpc tempWpc=tempClientWpc;
        cleanCard();
        return new MoveData(true,tempWpc,null,null);
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
        }
        return null;

    }

    private void updateClientExtractedDices(){
        for (Dice tempdice:currentPlayer.getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
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
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED);
                else return null;
            }
            case 1: return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC,null,tempExtractedDices,null,null);

        }
        return null;
    }


}
