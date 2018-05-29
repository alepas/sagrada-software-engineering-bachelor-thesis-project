package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.control.network.commands.notifications.DicePlacedNotification;
import it.polimi.ingsw.control.network.commands.notifications.PlayerSkipTurnNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardCanceledNotification;
import it.polimi.ingsw.control.network.commands.notifications.ToolCardUsedNotification;
import it.polimi.ingsw.control.network.commands.responses.MoveResponse;
import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.game.RoundTrack;
import it.polimi.ingsw.model.wpc.Position;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.ArrayList;


public class PlayerInGame {
    private String username;
    private DatabaseUsers db;
    private Game game;
    private Color[] privateObjs;
    private int favours;
    private boolean active=false;
    private WPC wpc;
    private int turnForRound;
    private ToolCard toolCardInUse=null;
    private Dice pickedDice=null;
    private int lastFavoursRemoved;
    private boolean toolCardUsedInTurn;
    private boolean placedDiceInTurn;
    private boolean allowPlaceDiceAfterCard;
    private ToolCard cardUsedBlockingTurn;



    public PlayerInGame(String user, Game game) throws CannotAddPlayerInDatabaseException {
        this.game=game;
        db=DatabaseUsers.getInstance();
        username=user;
        wpc=null;
        if (game instanceof MultiplayerGame) {
            privateObjs = new Color[GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME];
        } else {
            privateObjs = new Color[GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME];
        }
        favours=0;
        active=false;
        db.addPlayerInGameToDB(this);
        placedDiceInTurn=false;
        allowPlaceDiceAfterCard=true;
        toolCardUsedInTurn=false;
        lastFavoursRemoved=0;
        turnForRound=0;

    }

    public String getUser(){
        return username;
    }

    public boolean isActive() {
        return active;
    }


    public void setPickedDice(Dice pickedDice) {
        this.pickedDice = pickedDice;
    }


    public void setToolCardInUse(ToolCard toolCardInUse) {
        this.toolCardInUse = toolCardInUse;
    }

    public int getTurnForRound() {
        return turnForRound;
    }

    public int getWonGames() throws CannotFindUserInDBException {
        return db.getWonGamesFromUsername(username);
    }

    public int getLostGames() throws CannotFindUserInDBException {
        return db.getLostGamesFromUsername(username);
    }

    public int getAbandonedGames() throws CannotFindUserInDBException {
        return db.getAbandonedGamesFromUsername(username);
    }

    public int getRanking() throws CannotFindUserInDBException {
        return db.getRankingFromUsername(username);
    }


    public void addPointsToRanking(int pointsToAdd) throws CannotUpdateStatsForUserException {
        db.addPointsRankingFromUsername(username,pointsToAdd);

    }

    public void addWonGame() throws CannotUpdateStatsForUserException {
        db.addWonGamesFromUsername(username);

    }
    public void addLostGame() throws CannotUpdateStatsForUserException {
        db.addLostGamesFromUsername(username);


    }
    public void addAbandonedGame() throws CannotUpdateStatsForUserException {
        db.addAbandonedGamesFromUsername(username);

    }

    public void incrementTurnsForRound(){
        if (turnForRound<3){
            turnForRound++;
        }
        else if (turnForRound==3){
            clearPlayerRound();
            turnForRound++;
        }
        if ((turnForRound==2)&&(cardUsedBlockingTurn!=null)){
            game.changeAndNotifyObservers(new PlayerSkipTurnNotification(username,cardUsedBlockingTurn.getID()));
            game.nextTurn();
        }

    }

    public WPC getWPC() {
        return wpc;
    }

    public boolean setWPC(String id) {
        if (wpc==null) {
            WpcDB dbWpc=WpcDB.getInstance();
            wpc=dbWpc.getWpcByID(id).copyWpc();
            return true;
        }
        else return false;
    }


    public int getFavours(){

        return favours;
    }




    public Color[] getPrivateObjs() {
        return privateObjs;
    }

    public void setPrivateObjs(Color color, int index) {
        privateObjs[index] = color;
    }


    public synchronized void endTurn() throws PlayerNotAuthorizedException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null) {
            if (pickedDice == null) {
                clearPlayerTurn();
                game.nextTurn();
            }else throw new CannotPerformThisMoveException(username,0,true);
        } else throw new CannotPerformThisMoveException(username,1,true);
    }

    public synchronized MoveResponse getNextMove() throws PlayerNotAuthorizedException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null) {
            if (pickedDice==null){
                if ((!placedDiceInTurn)&&(!toolCardUsedInTurn))
                    return new MoveResponse((ClientMoveModifiedThings)null,ClientNextActions.MENU);
                else if (placedDiceInTurn)
                    return new MoveResponse((ClientMoveModifiedThings) null,ClientNextActions.MENU_ONLY_TOOLCARD);
                else if (toolCardUsedInTurn)
                    return new MoveResponse((ClientMoveModifiedThings)null,ClientNextActions.MENU_ONLY_PICKDICE);
            }
            else return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.PICKPOSITION,getClientExtractedDices(),pickedDice.getClientDice());
        } else return toolCardInUse.getNextMove();
        return null;
    }

    public synchronized Response placeDice(int diceId, Position pos) throws CannotPickDiceException, CannotPerformThisMoveException, PlayerNotAuthorizedException, CannotPickPositionException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if ((pickedDice != null)) {
            throw new CannotPerformThisMoveException(username,0,false);
        } else if ((toolCardInUse != null)) {
            throw new CannotPerformThisMoveException(username,1,false);
        }
        if (placedDiceInTurn){
            throw new CannotPerformThisMoveException(username,2,false);
        }
        Dice dice=null;
        for(Dice tempDice:game.getExtractedDices()){
            if(tempDice.getId()==diceId){
                dice=tempDice;
                break;
            }
        }
        if (dice!=null)
            throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.EXTRACTED, 0);
        pickedDice=dice;
        game.getExtractedDices().remove(dice);
        if ( wpc.addDiceWithAllRestrictions(pickedDice,pos,game.getCurrentTurn())==false)
            throw new CannotPickPositionException(username, pos);



        ClientWpc clientWpc=wpc.getClientWpc();
        game.changeAndNotifyObservers(new DicePlacedNotification(username,pos.getClientPosition(), false, clientWpc));
        incrementActionInTurn(false);
        MoveResponse tempResponse= new MoveResponse(ClientMoveModifiedThings.WINDOW,ClientNextActions.MOVEFINISHED,clientWpc);

        if ((!placedDiceInTurn)&&(allowPlaceDiceAfterCard))
            return new MoveResponse(tempResponse,ClientNextActions.MENU_ONLY_PICKDICE);
        else return new MoveResponse(tempResponse,ClientNextActions.TURNFINISHED);


    }

    public  Response pickDice(int diceId) throws CannotPickDiceException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if ((pickedDice != null)) {
            throw new CannotPerformThisMoveException(username,0,false);
        } else if ((toolCardInUse != null)) {
            throw new CannotPerformThisMoveException(username,1,false);
        }
        if (placedDiceInTurn){
            throw new CannotPerformThisMoveException(username,2,false);
        }
        Dice dice=null;
        for(Dice tempDice:game.getExtractedDices()){
            if(tempDice.getId()==diceId){
                dice=tempDice;
                break;
            }
        }
        if (dice!=null)
            throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.EXTRACTED, 0);
        pickedDice=dice;
        game.getExtractedDices().remove(dice);

        return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.PICKPOSITION,getClientExtractedDices(),dice.getClientDice());
    }

    public Response pickPosition(Position pos) throws PlayerNotAuthorizedException, CannotPickPositionException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if ((pickedDice == null)) {
            throw new CannotPerformThisMoveException(username,2,false);
        } else if ((toolCardInUse != null)) {
            throw new CannotPerformThisMoveException(username,1,false);
        }
        if ( wpc.addDiceWithAllRestrictions(pickedDice,pos,game.getCurrentTurn())==false)
            throw new CannotPickPositionException(username, pos);



        ClientWpc clientWpc=wpc.getClientWpc();
        game.changeAndNotifyObservers(new DicePlacedNotification(username,pos.getClientPosition(), false, clientWpc));
        incrementActionInTurn(false);
        return new MoveResponse(ClientMoveModifiedThings.WINDOW,ClientNextActions.MOVEFINISHED,clientWpc);


    }



    public synchronized MoveResponse cancelAction() throws CannotCancelActionException, PlayerNotAuthorizedException {
        MoveResponse temp;
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse != null) {
            ToolCard oldCard=toolCardInUse;
            temp = toolCardInUse.cancelAction();
            if (temp.cardStatus == ClientToolCardStatus.CANCELEDCARD) {
                favours += lastFavoursRemoved;
                game.changeAndNotifyObservers(new ToolCardCanceledNotification(username, oldCard.getID()));
                if (!placedDiceInTurn)
                    return new MoveResponse(temp,ClientNextActions.MENU);
                else return new MoveResponse(temp,ClientNextActions.MENU_ONLY_TOOLCARD);

            }

            else return toolCardInUse.cancelAction();

        }else if (pickedDice != null) {
            getUpdatedExtractedDices().add(pickedDice);
            pickedDice = null;
            if (!toolCardUsedInTurn)
                return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.MENU,getClientExtractedDices(),false,null,false);
            else return new MoveResponse(ClientMoveModifiedThings.EXTRACTEDDICES,ClientNextActions.MENU_ONLY_PICKDICE,getClientExtractedDices(),false,null,false);
        }
        else throw new CannotCancelActionException(username,null,0);

    }

    public synchronized Response stopToolCard() throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotStopToolCardException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse == null)
            throw new NoToolCardInUseException(username);
        MoveResponse tempResponse= toolCardInUse.stopToolCard();
        if (tempResponse.cardStatus==ClientToolCardStatus.FINISHEDCARD){
            incrementActionInTurn(true);
            if ((!placedDiceInTurn)&&(allowPlaceDiceAfterCard))
                return new MoveResponse(tempResponse,ClientNextActions.MENU_ONLY_PICKDICE);
            else return new MoveResponse(tempResponse,ClientNextActions.TURNFINISHED);
        }
        return tempResponse;
    }


    public synchronized Response setToolCard(String cardID) throws CannotUseToolCardException, PlayerNotAuthorizedException, CannotPerformThisMoveException {
        MoveResponse tempResponse;
        if (!active)
            throw new PlayerNotAuthorizedException(username);

        if ((pickedDice != null)) {
            throw new CannotPerformThisMoveException(username,0,false);
        }
        if ((toolCardInUse != null)) {
            throw new CannotPerformThisMoveException(username,1,false);
        }
        if ((toolCardUsedInTurn))
            throw new CannotPerformThisMoveException(username,3,false);

        boolean foundCard=false;
        ToolCard card=null;
        for (ToolCard tempCard:game.getToolCards()) {
            if (tempCard.getID().equals(cardID)) {
                foundCard = true;
                card=tempCard;
                break;
            }
        }
        if (!foundCard)
            throw new CannotUseToolCardException(cardID,0);
        if (game instanceof MultiplayerGame) {

            if (card.isUsed()) {
                if (favours >= 2) {
                    favours = favours - 2;
                    lastFavoursRemoved = 2;
                    tempResponse = card.setCard(this);
                    game.changeAndNotifyObservers(new ToolCardUsedNotification(username, cardID));
                } else throw new CannotUseToolCardException(cardID, 1);
            } else {
                if (favours >= 1) {
                    favours = favours - 1;
                    lastFavoursRemoved = 1;
                    tempResponse = card.setCard(this);
                    game.changeAndNotifyObservers(new ToolCardUsedNotification(username, cardID));
                } else throw new CannotUseToolCardException(cardID, 1);
            }
        }
        else {
            tempResponse=card.setCard(this);
        }
        if (tempResponse.cardStatus==ClientToolCardStatus.FINISHEDCARD){
            incrementActionInTurn(true);
            if ((!placedDiceInTurn)&&(allowPlaceDiceAfterCard))
                return new MoveResponse(tempResponse,ClientNextActions.MENU_ONLY_PICKDICE);
            else return new MoveResponse(tempResponse,ClientNextActions.TURNFINISHED);
        }
        return tempResponse;
    }


    public synchronized Response pickDiceforToolCard(int diceId, ClientDiceLocations where) throws CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        boolean foundDice=false;
        Dice dice=null;
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);
        MoveResponse tempResponse=new MoveResponse(null);

        switch (where) {
            case EXTRACTED: {
                for(Dice tempDice:game.getExtractedDices()){
                    if(tempDice.getId()==diceId){
                        foundDice=true;
                        dice=tempDice;
                        break;
                    }
                }
                if (!foundDice)
                    throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.EXTRACTED, 0);
                tempResponse= toolCardInUse.use( dice, ClientDiceLocations.EXTRACTED);
                break;


            }
            case WINDOW: {
                for(Dice tempDice:wpc.getWpcDices()){
                    if(tempDice.getId()==diceId){
                        foundDice=true;
                        dice=tempDice;
                        break;
                    }
                }
                if (!foundDice)
                    throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.WINDOW, 0);
                tempResponse= toolCardInUse.use( dice, ClientDiceLocations.WINDOW);
                break;

            }
            case ROUNDTRACK: {
                for(Dice tempDice:game.getRoundTrack().getDicesNotUsed()){
                    if(tempDice.getId()==diceId){
                        foundDice=true;
                        dice=tempDice;
                        break;
                    }
                }
                if (!foundDice)
                    throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.ROUNDTRACK, 0);
                tempResponse= toolCardInUse.use(dice, ClientDiceLocations.ROUNDTRACK);
                break;



            }

        }
        if (tempResponse.cardStatus==ClientToolCardStatus.FINISHEDCARD){
            incrementActionInTurn(true);
            if ((!placedDiceInTurn)&&(allowPlaceDiceAfterCard))
                return new MoveResponse(tempResponse,ClientNextActions.MENU_ONLY_PICKDICE);
            else return new MoveResponse(tempResponse,ClientNextActions.TURNFINISHED);
        }
        return tempResponse;

    }

    public synchronized Response placeDiceForToolCard(int diceId, ClientDiceLocations diceFrom, Position pos) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickPositionException, CannotPerformThisMoveException, CannotPickDiceException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);
        if (diceId!=this.pickedDice.getId())
            throw new CannotPickDiceException(username,diceId,null,diceFrom,0);
        MoveResponse tempResponse= toolCardInUse.use(pos);
        if (tempResponse.cardStatus==ClientToolCardStatus.FINISHEDCARD){
            incrementActionInTurn(true);
            if ((!placedDiceInTurn)&&(allowPlaceDiceAfterCard))
                return new MoveResponse(tempResponse,ClientNextActions.MENU_ONLY_PICKDICE);
            else return new MoveResponse(tempResponse,ClientNextActions.TURNFINISHED);
        }
        return tempResponse;
    }

    public synchronized Response pickColorForToolCard(Color color) throws NoToolCardInUseException, PlayerNotAuthorizedException, CannotPickColorException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);
        MoveResponse tempResponse= toolCardInUse.use(color);
        if (tempResponse.cardStatus==ClientToolCardStatus.FINISHEDCARD){
            incrementActionInTurn(true);
            if ((!placedDiceInTurn)&&(allowPlaceDiceAfterCard))
                return new MoveResponse(tempResponse,ClientNextActions.MENU_ONLY_PICKDICE);
            else return new MoveResponse(tempResponse,ClientNextActions.TURNFINISHED);
        }
        return tempResponse;
    }

    public synchronized Response pickNumberForToolCard(int num) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException, CannotPerformThisMoveException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);
        MoveResponse tempResponse= toolCardInUse.use(num);
        if (tempResponse.cardStatus==ClientToolCardStatus.FINISHEDCARD){
            incrementActionInTurn(true);
            if ((!placedDiceInTurn)&&(allowPlaceDiceAfterCard))
                return new MoveResponse(tempResponse,ClientNextActions.MENU_ONLY_PICKDICE);
            else return new MoveResponse(tempResponse,ClientNextActions.TURNFINISHED);
        }
        return tempResponse;
    }

    public WPC getUpdatedWpc(String username) throws UserNotInThisGameException {
        if (this.username==username)
            return wpc;
        for (PlayerInGame player: game.getPlayers()) {
            if (player.getUser() == username)
                return player.wpc;
        }
        throw new UserNotInThisGameException(username, this.game);
    }

    public ArrayList<Dice> getUpdatedExtractedDices(){
        return game.getExtractedDices();
    }

    public RoundTrack getUpdatedRoundTrack(){
        return game.getRoundTrack();
    }

    public ArrayList<ToolCard> getUpdatedToolCards() {
        return game.getToolCards();
    }

    public ArrayList<PublicObjectiveCard> getUpdatedPOCs(){
        return game.getPublicObjectiveCards();
    }

    public Game getGame() {
        return game;
    }

    public synchronized void setActive() {
        this.active = true;
        incrementTurnsForRound();
    }


    public boolean isToolCardUsedInTurn() {
        return toolCardUsedInTurn;
    }

    public void setToolCardUsedInTurn(boolean toolCardUsedInTurn) {
        this.toolCardUsedInTurn = toolCardUsedInTurn;
    }

    public boolean isPlacedDiceInTurn() {
        return placedDiceInTurn;
    }

    public void setPlacedDiceInTurn(boolean placedDiceInTurn) {
        this.placedDiceInTurn = placedDiceInTurn;
    }

    public boolean isAllowPlaceDiceAfterCard() {
        return allowPlaceDiceAfterCard;
    }

    public void setAllowPlaceDiceAfterCard(boolean allowPlaceDiceAfterCard) {
        this.allowPlaceDiceAfterCard = allowPlaceDiceAfterCard;
    }

    public ToolCard getCardUsedBlockingTurn() {
        return cardUsedBlockingTurn;
    }

    public void setCardUsedBlockingTurn(ToolCard cardUsedBlockingTurn) {
        this.cardUsedBlockingTurn = cardUsedBlockingTurn;
    }

    public void setNotActive() {
        this.active = false;
    }



    private ArrayList<ClientDice> getClientExtractedDices(){
        ArrayList<ClientDice> tempExtractedDices=new ArrayList<>();
        for (Dice tempdice:getUpdatedExtractedDices())
            tempExtractedDices.add(tempdice.getClientDice());
        return tempExtractedDices;

    }

    private void incrementActionInTurn(boolean cardAction) {
        if (cardAction) {
            toolCardUsedInTurn=true;
            if ((placedDiceInTurn)||(!allowPlaceDiceAfterCard))
                game.nextTurn();
        }else {
            placedDiceInTurn=true;
            if (toolCardUsedInTurn)
                game.nextTurn();
        }
    }


    private void clearPlayerTurn(){
        toolCardUsedInTurn=false;
        placedDiceInTurn=false;
        lastFavoursRemoved=0;
        allowPlaceDiceAfterCard=true;


    }

    private void clearPlayerRound(){
        turnForRound=0;
        cardUsedBlockingTurn=null;
        clearPlayerTurn();
    }


}
