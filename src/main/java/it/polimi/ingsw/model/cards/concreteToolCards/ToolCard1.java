package it.polimi.ingsw.model.cards.concreteToolCards;

import it.polimi.ingsw.control.network.commands.notifications.DiceChangedNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardUsedNotification;
import it.polimi.ingsw.control.network.commands.responses.MoveResponse;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.ToolCardConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Position;

import java.util.ArrayList;

public class ToolCard1 extends ToolCard {
    private Dice dice;
    private Dice oldDice;
    private ArrayList<ClientDice> tempExtractedDices;
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
        numbers.add(-1);
        numbers.add(1);
    }



    @Override
    public ToolCard getToolCardCopy() {
        return new ToolCard1() ;
    }


    @Override
    public MoveResponse setCard(PlayerInGame player) throws CannotUseToolCardException {
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
        if (currentGame instanceof MultiplayerGame) {
            this.currentStatus = 1;
            this.currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username, id));
            return new MoveResponse(null, ClientNextActions.PICKDICE, ClientDiceLocations.EXTRACTED, true, ClientToolCardStatus.SETTEDCARD, false);
        }
        else {
            singlePlayerGame=true;
            return new MoveResponse(null,ClientNextActions.PICKDICE_SINGLEPLAYER_CARD,ClientDiceLocations.EXTRACTED,true,ClientToolCardStatus.WAITDICE,false);
        }
    }


    @Override
    public MoveResponse use(Dice dice, ClientDiceLocations location) throws CannotPickDiceException, CannotPerformThisMoveException {
        if ((currentStatus==0)&&(singlePlayerGame)){
            if (dice.getDiceColor()!=colorForDiceSingleUser)
                throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),location, 1);
            this.currentStatus = 1;
            this.dice=dice;
            currentGame.getExtractedDices().remove(dice);
            updateClientExtractedDices();
            currentGame.changeAndNotifyObservers(new ToolCardUsedNotification(username,id));
            return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES, ClientNextActions.PICKDICE, ClientDiceLocations.EXTRACTED, dice.getClientDice(),true, ClientToolCardStatus.SETTEDCARD, false);
        }
        if(currentStatus!=1)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        int diceNum=dice.getDiceNumber();
        if ((diceNum==1)||(diceNum==6))
            throw new CannotPickDiceException(currentPlayer.getUser(),dice.getDiceNumber(),dice.getDiceColor(),location, 2);
        this.dice=dice;
        currentStatus=2;
        tempExtractedDices=new ArrayList<>();
        currentGame.getExtractedDices().remove(dice);

        updateClientExtractedDices();
        return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.PICKNUMBER,null,numbers,tempExtractedDices, true, null,false);
    }

    @Override
    public MoveResponse use(Color color) throws CannotPerformThisMoveException {
        throw new CannotPerformThisMoveException(username,2,false);
    }

    @Override
    public MoveResponse use(int number) throws CannotPerformThisMoveException, CannotPickNumberException {
        if(currentStatus!=2)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        if ((number!=-1)||(number!=-1))
            throw new CannotPickNumberException(currentPlayer.getUser(),number);
        currentStatus=3;
        int tempNum=this.dice.getDiceNumber();
        oldDice=this.dice.copyDice();
        try {
            dice.setNumber(tempNum+number);
        } catch (IncorrectNumberException e) { }
        currentGame.getExtractedDices().add(dice);
        updateClientExtractedDices();
        currentGame.changeAndNotifyObservers(new DiceChangedNotification(username,oldDice.getClientDice(),dice.getClientDice(),ClientDiceLocations.EXTRACTED,ClientDiceLocations.EXTRACTED,tempExtractedDices));
        /*  player.setPickedDice(dice);*/
        return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES, ClientNextActions.PICKPOSITION, true, null,true);
    }

    @Override
    public MoveResponse use(Position position) throws CannotPerformThisMoveException, CannotPickPositionException {
        if(currentStatus!=3)
            throw new CannotPerformThisMoveException(currentPlayer.getUser(),2,false);
        currentStatus=4;
        if (!currentPlayer.getWPC().addDiceWithAllRestrictions(dice, position, currentGame.getCurrentTurn()))
            throw new CannotPickPositionException(username, position);
        currentPlayer.getGame().changeAndNotifyObservers(new DiceChangedNotification(username,oldDice.getClientDice(),dice.getClientDice(),ClientDiceLocations.EXTRACTED,ClientDiceLocations.EXTRACTED,tempExtractedDices));
        /*  player.setPickedDice(dice);*/
        currentPlayer.setToolCardUsedInTurn(true);
        cleanCard();
        return new MoveResponse(ClientMoveModifiedThings.WINDOW, ClientNextActions.MOVEFINISHED, false, ClientToolCardStatus.FINISHEDCARD,false);
    }


    @Override
    public MoveResponse cancelAction() throws CannotCancelActionException {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame){
                    currentGame.getExtractedDices().add(diceForSingleUser);
                    updateClientExtractedDices();
                    ArrayList<ClientDice> tempDices=tempExtractedDices;
                    cleanCard();
                    return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.MOVEFINISHED,tempDices,false,ClientToolCardStatus.CANCELEDCARD,false);
                }
                    return null;
            }
            case 1: {
                if (!singlePlayerGame){
                    cleanCard();
                    return new MoveResponse(null,ClientNextActions.MOVEFINISHED,false,ClientToolCardStatus.CANCELEDCARD,false);
                }
                return new MoveResponse(null,ClientNextActions.PICKDICE_SINGLEPLAYER_CARD,ClientDiceLocations.EXTRACTED,true,ClientToolCardStatus.WAITDICE,false);
            }
            case 2: return new MoveResponse(null, ClientNextActions.PICKDICE, ClientDiceLocations.EXTRACTED, true, ClientToolCardStatus.SETTEDCARD, false);
            case 3: return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.PICKNUMBER,null,numbers,tempExtractedDices, true, null,false);
        }
        return null;

    }

    private void updateClientExtractedDices(){
        for (Dice tempdice:currentPlayer.getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
    }

    @Override
    protected void cleanCard(){
        currentPlayer.setToolCardInUse(null);
        currentPlayer.setAllowPlaceDiceAfterCard(true);
        this.used=false;
        this.diceForSingleUser=null;
        this.currentPlayer=null;
        this.currentStatus=0;
        this.stoppable=false;
        this.currentGame=null;
        this.username=null;
        this.dice=null;
        this.singlePlayerGame=false;
    }

    @Override
    public MoveResponse getNextMove() {
        switch (currentStatus){
            case 0: {
                if (singlePlayerGame)
                    return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES, ClientNextActions.PICKDICE, ClientDiceLocations.EXTRACTED, dice.getClientDice(),true, ClientToolCardStatus.SETTEDCARD, false);
                break;
            }
            case 1: return new MoveResponse(null, ClientNextActions.PICKDICE, ClientDiceLocations.EXTRACTED, true, ClientToolCardStatus.SETTEDCARD, false);
            case 2: return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.PICKNUMBER,null,numbers,tempExtractedDices, true, null,false);
            case 3: return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES, ClientNextActions.PICKPOSITION, true, null,true);
        }
        return null;
    }


}

