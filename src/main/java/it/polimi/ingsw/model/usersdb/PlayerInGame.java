package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.game.AbstractGame;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.ArrayList;


public class PlayerInGame {
    private String username;
    private DatabaseUsers db;
    private AbstractGame game;
    private Color privateObjective1;
    private Color privateObjective2;
    private int favours;
    private boolean active=false;
    private WPC wpc;



/*
    private Controller online;
    private Game activegame;
*/

    public PlayerInGame(String user, AbstractGame game){
        this.game=game;
        db=DatabaseUsers.getInstance();
        username=user;
        wpc=null;
        privateObjective1=null;
        privateObjective2=null;
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

    public void setWPC(String id) {
        this.wpc =  WpcDB.getWpcByID(id);
    }

    public int getFavours(){

        return favours;
    }



    public Color getPrivateObjective1() {
        return privateObjective1;
    }

    public Color getPrivateObjective2() {
        return privateObjective2;
    }


    private boolean removeFavours(int favoursToRemove){
        if (favours<favoursToRemove)
            return false;
        favours-=favoursToRemove;
        return true;
    }

    private void setFavours(int favours){
        this.favours=favours;
    }

    public void setPrivateObjective1(Color color) {
        this.privateObjective1=color;

    }

    public void setPrivateObjective2(Color color) {
        this.privateObjective2=color;

    }


    public void endTurn(){
        //game.
    }

    public void chooseWPC(ArrayList<String> cards){

        //da implementare select carta e poi
        String ab="123";
        //this.wpc= wpc.getWpcByID(id); // id è una stringa
    }

    void setWpc(String wpcId){
        WpcDB dbwpc=WpcDB.getInstance();
        this.wpc=dbwpc.getWpcByID(wpcId).copyWpc();
    }

    void UseToolCard(){}


    //isInGame() capire come implementarla

}
