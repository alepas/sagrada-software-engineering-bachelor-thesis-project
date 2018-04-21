package it.polimi.ingsw.model.usersdb;

import it.polimi.ingsw.model.usersdb.UserInDB;

import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class User {
    private UserInDB databaselinkedUser;
    private String username;
    private int wonGames;
    private int lostGames;
    private int abandonedGames;
    private int ranking;
/*
    private Controller online;
    private Game activegame;
*/


    User(String user, UserInDB data){
        databaselinkedUser=data;
        username=user;
        wonGames=data.wonGames;
        lostGames=data.lostGames;
        abandonedGames=data.abandonedGames;
        ranking=data.ranking;
        /*
        la funzione deve inizializzare il valore di username e deve andare a chiamare la corrispettiva
        funzione per la crittografia della password e quindi salvare la password con relativo salt se presente.
        tutti i parametri sulle partite sono 0. Il parametro online è il Controller corrente dell-user. activegame è null.*/
    }



    public String getUsername(){
        return username;
    }

    public int getWonGames(){
        return wonGames;
    }

    public int getLostGames(){
        return lostGames;
    }

    public int getAbandonedGames(){
        return abandonedGames;
    }

    public int getRanking() {
        return ranking;
    }

    public void addWonGame() {
        /*verificare che per ognuno di questi la chiamata produca un aumento efffettivo dei giochi*/
        this.wonGames ++;
    }
    public void addLostGame() {
        this.lostGames ++;

    }
    public void addAbandonedGame() {
        this.abandonedGames ++;
    }

    public boolean login(String username, String pwd){
        /*
        devo andare a fare lookup nel database degli utenti per vedere se ci sono
        corrispondenze di username
        chiamare la funzione di crittografia della password appena inserita
        fare la verifica se la password crittografata corrisponde a quella salvata
        nell database
        in tal caso deve settare logged al Controller corrente

        TEST:
        verificare che dopo il login il Controller sia inserito in logged
         */
        return true;
    }

    public boolean logout(){
        /*
        va a rimuovere il corrente Controller dal logged
        TEST:
         */
        return true;
    }

    //isInGame() capire come implementarla

}
