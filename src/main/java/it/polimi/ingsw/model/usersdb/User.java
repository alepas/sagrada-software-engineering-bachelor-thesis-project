package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.clientModel.ClientUser;

import java.io.Serializable;

class User implements Serializable {

    private String username;
    private String password;
    private byte[] salt;
    private int wonGames;
    private int lostGames;
    private int abandonedGames;
    private int ranking;

    User(String username, String password,byte[] salt){
        this.username = username;
        this.password=password;
        this.salt=salt;
        wonGames=0;
        lostGames=0;
        abandonedGames=0;
        ranking=0;
    }

    ClientUser getClientUser(){
        return new ClientUser(this.username, this.wonGames,
                this.lostGames, this.abandonedGames, this.ranking);
    }


    void addWonGames(){
        wonGames++;
    }
    void removeWonGames(){wonGames--;}
    void addLostGames(){
        lostGames++;
    }
    void removeLostGames(){lostGames--;}
    void addAbandonedGames(){
        abandonedGames++;
    }
    void removeAbandonedGames(){abandonedGames--;}
    void addPointsToRanking(int points){
        ranking+=points;
    }

    String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    byte[] getSalt() {
        return salt;
    }

    int getWonGames() {
        return wonGames;
    }

    int getLostGames() {
        return lostGames;
    }

    int getAbandonedGames() {
        return abandonedGames;
    }

    int getRanking() {
        return ranking;
    }
}
