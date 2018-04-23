package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.model.*;



import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class UserInGame {
    private DatabaseUsers db;
    private String username;
    private Colour privateObjectOne;
    private Colour getPrivateObjectTwo;
    private int favours;
    private boolean active;
    private WPC wpc;



/*
    private Controller online;
    private Game activegame;
*/


    UserInGame(String user){
        db=DatabaseUsers.getInstance();
        username=user;

        /*
        la funzione deve inizializzare il valore di username e deve andare a chiamare la corrispettiva
        funzione per la crittografia della password e quindi salvare la password con relativo salt se presente.
        tutti i parametri sulle partite sono 0. Il parametro online è il Controller corrente dell-user. activegame è null.*/
    }



    public String getUsername(){
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

    public void getWPC(WPC[] arrayWPC){
        //scelta della carta
       // Cell[][]
       // wpc=WPC(Cell[][] schema);
    }

    public int getFavours(){
        return favours;
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



    //isInGame() capire come implementarla

}
