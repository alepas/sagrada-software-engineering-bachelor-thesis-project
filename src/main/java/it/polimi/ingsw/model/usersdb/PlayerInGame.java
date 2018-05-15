package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.*;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.wpc.Position;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.ArrayList;


public class PlayerInGame {
    private String username;
    private transient DatabaseUsers db;
    private Game game;
    private Color[] privateObjs;
    private int favours;
    private boolean active=false;
    private WPC wpc;
    private int actionsForTurn=0;



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

    public void chooseWPC(ArrayList<String> cards){

        //da implementare select carta e poi
        String ab="123";
        //this.wpc= wpc.getWpcByID(id); // id è una stringa
    }


    public void useToolCard(String cardID) throws CannotUseToolCardException, PlayerNotAuthorizedException {
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
        incrementActionsForTurn();

    }


    public void placeDice(int diceId, Position pos) throws PlayerNotAuthorizedException, CannotPlaceDiceException {
        if (!active)
            throw new PlayerNotAuthorizedException(username);
        boolean foundDice=false;
        Dice dice=null;
        for(Dice tempDice:game.getExtractedDices()){
            if(tempDice.getDiceID()==diceId){
                foundDice=true;
                dice=tempDice;
                break;
            }
        }
        if (!foundDice)
            throw new CannotPlaceDiceException(username,0);
        //wpc.addDice(dice,pos)


//finire aggiungendo eccezioni per cannotplacedice
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
