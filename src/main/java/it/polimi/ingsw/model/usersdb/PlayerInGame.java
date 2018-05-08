package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotUseToolCardException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.PlayerNotAuthorizedException;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.io.Serializable;
import java.util.ArrayList;


public class PlayerInGame {
    private String username;
    private transient DatabaseUsers db;
    private Game game;
    private Color[] privateObjs;
    private int favours;
    private boolean active=false;
    private WPC wpc;



/*
    private Controller online;
    private Game activegame;
*/

    public PlayerInGame(String user, Game game){
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

    }



    public String getUser(){

        return username;
    }

    public int getWonGames(){

        return db.getWonGamesFromUsername(username);
    }

    public int getLostGames(){

        return db.getLostGamesFromUsername(username);
    }

    public int getAbandonedGames(){

        return db.getAbandonedGamesFromUsername(username);
    }

    public int getRanking() {

        return db.getRankingFromUsername(username);
    }


    public void addPointsToRanking(int pointsToAdd){

        db.addPointsRankingFromUsername(username,pointsToAdd);
    }

    public void addWonGame() {
        db.addWonGamesFromUsername(username);
    }
    public void addLostGame() {
        db.addLostGamesFromUsername(username);

    }
    public void addAbandonedGame() {

        db.addAbandonedGamesFromUsername(username);
    }

    public WPC getWPC() {
        return wpc;
    }

    public boolean setWPC(String id) {
        if (wpc==null) {
            WpcDB dbwpc=WpcDB.getInstance();
            this.wpc=dbwpc.getWpcByID(id).copyWpc();
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
        if (foundCard==false)
            throw new CannotUseToolCardException(cardID,0);
        if (card.isUsed()==true) {
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


    }

    // public void placeDice( HO BISOGNO DI RIUSCIRE A CAPIRE QUAL E' IL DADO A CUI MI RIFERISCO.. AGGIUNGIAMO UN ID AL SINGOLO DADO?


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
