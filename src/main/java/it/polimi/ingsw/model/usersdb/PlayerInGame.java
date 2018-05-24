package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.control.network.commands.responses.Response;
import it.polimi.ingsw.control.network.commands.responses.ToolCardResponse;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.clientModel.ClientDiceLocations;
import it.polimi.ingsw.model.clientModel.ClientToolCardModifiedThings;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.wpc.Position;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.wpc.WpcDB;


public class PlayerInGame {
    private String username;
    private transient DatabaseUsers db;
    private Game game;
    private Color[] privateObjs;
    private int favours;
    private boolean active=false;
    private WPC wpc;
    private int actionsForTurn=0;
    private ToolCard toolCardInUse=null;
    private Dice pickedDice=null;
    private Dice tempDice=null;



/*
    private Controller online;
    private Game activegame;
*/

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
    }

    public String getUser(){

        return username;
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


    public void endTurn()throws PlayerNotAuthorizedException{
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        game.nextTurn();
    }

    public Response useToolCard(String cardID) throws CannotUseToolCardException, PlayerNotAuthorizedException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
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
        if (card.isUsed()) {
            if (favours >= 2) {
                favours = favours - 2;
                card.use(this);
            } else throw new CannotUseToolCardException(cardID, 1);
        }
        else{
            if (favours >= 1) {
                favours = favours - 1;
                card.use(this);
            } else throw new CannotUseToolCardException(cardID, 1);
        }

        return new ToolCardResponse(ClientToolCardModifiedThings.TOOLCARDON, null);
    }


    public void pickPosition(Position pos) throws PlayerNotAuthorizedException, CannotPickPositionException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if ( wpc.addDiceWithAllRestrictions(pickedDice,pos,game.getCurrentTurn())==false)
            throw new CannotPickPositionException(username, pos);
        incrementActionsForTurn();
    }



    // public void placeDice( HO BISOGNO DI RIUSCIRE A CAPIRE QUAL E' IL DADO A CUI MI RIFERISCO.. AGGIUNGIAMO UN ID AL SINGOLO DADO?

    private void incrementActionsForTurn(){
        actionsForTurn++;
        if (actionsForTurn==2){
            actionsForTurn=0;
            game.nextTurn();
        }
    }

    // to use for toolCards interaction
    public Response pickDiceforToolCard(int diceId, ClientDiceLocations where) throws CannotPickDiceException, PlayerNotAuthorizedException, NoToolCardInUseException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        boolean foundDice=false;
        Dice dice=null;
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);

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
                    throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.EXTRACTED);
                return toolCardInUse.use(this, dice);


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
                    throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.WINDOW);
                return toolCardInUse.use(this, dice);

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
                    throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.ROUNDTRACK);
                return toolCardInUse.use(this, dice);



            }

        }
        return null;
    }

    public Response pickPositionForToolCard(Position pos) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickPositionException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);
        return toolCardInUse.use(this,pos);
    }

    public Response pickColorForToolCard(Color color) throws NoToolCardInUseException, PlayerNotAuthorizedException, CannotPickColorException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);
        return toolCardInUse.use(this,color);
    }

    public Response pickNumberForToolCard(int num) throws PlayerNotAuthorizedException, NoToolCardInUseException, CannotPickNumberException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse==null)
            throw new NoToolCardInUseException(username);
        return toolCardInUse.use(this,num);
    }

    public void pickDice(int diceId) throws CannotPickDiceException, PlayerNotAuthorizedException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (pickedDice!=null)
            throw new CannotPickDiceException(username, pickedDice.getDiceNumber(), pickedDice.getDiceColor(),ClientDiceLocations.EXTRACTED);
        boolean foundDice=false;
        Dice dice=null;
        for(Dice tempDice:game.getExtractedDices()){
            if(tempDice.getId()==diceId){
                foundDice=true;
                dice=tempDice;
                break;
            }
        }
        if (!foundDice)
            throw new CannotPickDiceException(username,dice.getDiceNumber(),dice.getDiceColor(),ClientDiceLocations.EXTRACTED);
        pickedDice=dice;
    }

    public boolean cancelAction() throws CannotCancelToolCardException, PlayerNotAuthorizedException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        if (toolCardInUse!=null){
            toolCardInUse.cancel(this);
            return true;
        }
        else if (pickedDice!=null) {
            pickedDice = null;
            return true;
        }
        return false;
    }



    public Game getGame() {
        return game;
    }

    public void setActive() {
        this.active = true;
    }

    public void setNotActive() {
        this.active = false;
    }



}
