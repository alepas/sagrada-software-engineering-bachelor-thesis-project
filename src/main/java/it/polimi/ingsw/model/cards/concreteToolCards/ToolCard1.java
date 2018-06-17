package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.ToolCardDiceChangedNotification;
import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
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

public class ToolCard1 extends ToolCard {
    private Dice dice;
    private Dice oldDice;
    ArrayList<Integer> numbers;

    public ToolCard1() {
        this.id = ToolCardConstants.TOOLCARD1_ID;
        this.name = ToolCardConstants.TOOL1_NAME;
        this.description = ToolCardConstants.TOOL1_DESCRIPTION;
        this.colorForDiceSingleUser=Color.VIOLET;
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
        this.dice=null;
        this.singlePlayerGame=false;
        numbers=new ArrayList<>();
        tempExtractedDices=new ArrayList<>();
        movesNotifications=new ArrayList<>();
    }



    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard1() ;
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

        Dice tempDice=currentPlayer.dicePresentInLocation(diceId,ClientDiceLocations.EXTRACTED).getDice();
        this.dice= tempDice;
        currentStatus=2;
        moveCancellable=true;
        int tempNum=this.dice.getDiceNumber();
        if (tempNum>1)
            numbers.add(-1);
        if (tempNum<6)
            numbers.add(1);


        return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD,null,null,null, tempDice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers,false);
    }


    @Override
    public MoveData pickNumber(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        if(currentStatus!=2)
            throw new CannotPerformThisMoveException(username,2,false);
        if (!((number==-1)||(number==1)))
            throw new CannotPickNumberException(username,number);

        int tempNum=this.dice.getDiceNumber();
        if (((tempNum==6)&&(number==1))||((tempNum==1)&&(number==-1)))
            throw new CannotPickNumberException(username, number);
        oldDice=this.dice.copyDice();
        try {
            dice.setNumber(tempNum+number);
        } catch (IncorrectNumberException e) {
            throw new CannotPickNumberException(username,number);
        }
        currentStatus=3;
        moveCancellable=true;
        updateClientExtractedDices();
        movesNotifications.add(new ToolCardDiceChangedNotification(username,oldDice.getClientDice(),dice.getClientDice(),ClientDiceLocations.EXTRACTED,ClientDiceLocations.EXTRACTED));
        return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,null,dice.getClientDice(), ClientDiceLocations.EXTRACTED);
    }

    @Override
    public MoveData interuptToolCard(ToolCardInteruptValues value) throws CannotInteruptToolCardException {
        throw new CannotInteruptToolCardException(username,id);
    }

    @Override
    public MoveData placeDice(int diceId, Position pos) throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        if(currentStatus!=3)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (pos==null)
                throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if (diceId!=this.dice.getId())
            throw new CannotPickDiceException(username, diceId,ClientDiceLocations.EXTRACTED,3);
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(this.dice, pos))
            throw new CannotPickPositionException(username, pos);
        currentGame.getExtractedDices().remove(this.dice);
        currentStatus=4;
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
                this.dice=null;
                this.currentStatus=1;
                this.numbers.clear();
                return new MoveData(NextAction.SELECT_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED);

            }
            case 3: {
                try {
                    dice.setNumber(oldDice.getDiceNumber());
                } catch (IncorrectNumberException e) {
                    throw new CannotCancelActionException(username,this.id,3);
                }
                updateClientExtractedDices();
                this.currentStatus=2;
                movesNotifications.remove(movesNotifications.size()-1);
                return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD,null,tempExtractedDices,null,this.dice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers,false);
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
        this.numbers.clear();
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
            case 2: return new MoveData(NextAction.SELECT_NUMBER_TOOLCARD,null,tempExtractedDices,null,this.dice.getClientDice(), ClientDiceLocations.EXTRACTED, numbers,false);


            case 3: return new MoveData(NextAction.PLACE_DICE_TOOLCARD,ClientDiceLocations.EXTRACTED,ClientDiceLocations.WPC,null,tempExtractedDices,null,dice.getClientDice(), ClientDiceLocations.EXTRACTED);

        }
        return null;
    }


}

