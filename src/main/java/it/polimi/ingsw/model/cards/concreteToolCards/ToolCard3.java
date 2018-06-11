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
        this.maxCancelStatus = 1;
        this.cardOnlyInFirstMove = false;
        this.used = false;
        this.diceForSingleUser = null;
        this.currentPlayer = null;
        this.currentStatus = 0;
        this.stoppable = false;
        this.currentGame = null;
        this.username = null;
        tempExtractedDices=new ArrayList<>();
        movesNotifications=new ArrayList<>();

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
        if (currentGame.isSinglePlayerGame()) {
            singlePlayerGame=true;
            return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED);
        }
        else {
            this.currentStatus = 1;
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC);

        }
    }


    @Override
    public MoveData pickDice(int diceId) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus==0)&&(singlePlayerGame)){
            Dice tempDice=currentPlayer.dicePresentInLocation(diceId,ClientDiceLocations.EXTRACTED).getDice();
            if (tempDice.getDiceColor()!=colorForDiceSingleUser)
                throw new CannotPickDiceException(username, tempDice.getDiceNumber(), tempDice.getDiceColor(),ClientDiceLocations.EXTRACTED, 1);
            this.currentStatus = 1;
            this.diceForSingleUser= tempDice;
            currentGame.getExtractedDices().remove(this.diceForSingleUser);
            updateClientExtractedDices();
            return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC,null,tempExtractedDices,null,null, null);
        }
        else throw new CannotPerformThisMoveException(username,2,false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username,2,false);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if(currentStatus!=1)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        DiceAndPosition diceAndPosition=currentPlayer.dicePresentInLocation(diceId,ClientDiceLocations.WPC);
        Dice tempDice=diceAndPosition.getDice();
        if (pos==null){
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        }
        currentPlayer.getWPC().removeDice(diceAndPosition.getPosition());
        if (!currentPlayer.getWPC().addDicePersonalizedRestrictions(tempDice,pos,true,false,true,true,false)){
            currentPlayer.getWPC().addDicePersonalizedRestrictions(tempDice,diceAndPosition.getPosition(),false,false,false,false,false);
            throw new CannotPickPositionException(username, pos);
        }
        this.used = true;
        updateClientWPC();
        movesNotifications.add(new ToolCardDicePlacedNotification(username, tempDice.getClientDice(),pos,tempClientWpc,null,null));
        currentPlayer.getGame().changeAndNotifyObservers(new ToolCardUsedNotification(username,this.getClientToolcard(),movesNotifications));
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
                    return new MoveData(true,true);
                }
                return null;
            }
            case 1: {
                if (!singlePlayerGame){
                    System.out.println("sto cancellando la cartina bbbeella");
                    cleanCard();
                    return new MoveData(true,true);

                }
                currentGame.getExtractedDices().add(diceForSingleUser);
                updateClientExtractedDices();
                diceForSingleUser=null;
                this.currentStatus=0;
                return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED,null,null,tempExtractedDices,null,null, null);
            }
        }
        return null;

    }

    private void updateClientExtractedDices(){
        tempExtractedDices.clear();
        for (Dice tempdice:currentPlayer.getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
    }

    private void updateClientWPC(){
        tempClientWpc=currentPlayer.getWPC().getClientWpc();
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
        this.singlePlayerGame=false;
        this.tempClientWpc=null;
        this.tempExtractedDices=new ArrayList<>();
        this.movesNotifications=new ArrayList<>();
    }

    @Override
    public MoveData getNextMove() {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame)
                    return new MoveData(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD,ClientDiceLocations.EXTRACTED);
                else return null;
            }
            case 1: return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.WPC,ClientDiceLocations.WPC,null,tempExtractedDices,null,null, null);

        }
        return null;
    }


}
