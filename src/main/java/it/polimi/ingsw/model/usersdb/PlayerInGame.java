package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.WPC.WPC;

import java.util.ArrayList;


public class PlayerInGame {
    private DatabaseUsers db;
    private String username;
    private Color privateObjective1;
    private Color privateObjective2;
    private int favours;
    private boolean active;
    private WPC wpc;



/*
    private Controller online;
    private Game activegame;
*/


    public PlayerInGame(String user, Color pvOb1){
        db=DatabaseUsers.getInstance();
        username=user;
        wpc=null;
        privateObjective1=pvOb1;
        privateObjective2=null;
        favours=0;
        active=false;

        /*
        la funzione deve inizializzare il valore di username e deve andare a chiamare la corrispettiva
        funzione per la crittografia della password e quindi salvare la password con relativo salt se presente.
        tutti i parametri sulle partite sono 0. Il parametro online è il Controller corrente dell-user. activegame è null.*/
    }

    public PlayerInGame(String user, Color pvOb1, Color pvOb2){
        db=DatabaseUsers.getInstance();
        username=user;
        wpc=null;
        privateObjective1=pvOb1;
        privateObjective2=pvOb2;
        favours=0;
        active=false;

        /*
        la funzione deve inizializzare il valore di username e deve andare a chiamare la corrispettiva
        funzione per la crittografia della password e quindi salvare la password con relativo salt se presente.
        tutti i parametri sulle partite sono 0. Il parametro online è il Controller corrente dell-user. activegame è null.*/
    }

    public PlayerInGame(String user){
        db=DatabaseUsers.getInstance();
        username=user;
        wpc=null;
        privateObjective1=null;
        privateObjective2=null;
        favours=0;
        active=false;

        /*
        la funzione deve inizializzare il valore di username e deve andare a chiamare la corrispettiva
        funzione per la crittografia della password e quindi salvare la password con relativo salt se presente.
        tutti i parametri sulle partite sono 0. Il parametro online è il Controller corrente dell-user. activegame è null.*/
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

    public void modifyRanking(int pointsToAdd){

        db.modifyRankingFromUsername(pointsToAdd,username);
    }
    public void addWonGame() {
        db.addWonGamesFromUsername(username);
        /*verificare che per ognuno di questi la chiamata produca un aumento efffettivo dei giochi*/
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

    public void setWPC(WPC[] arrayWPC){
        //scelta della carta
        // Cell[][]
        // wpc=WPC(Cell[][] schema);
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


    public boolean removeFavours(int favoursToRemove){
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

    public void chooseWPC(ArrayList<String> cards){
        //da implementare select carta e poi
        String ab="123";
        //this.wpc= new WPC(ab);
    }


    //isInGame() capire come implementarla

}
