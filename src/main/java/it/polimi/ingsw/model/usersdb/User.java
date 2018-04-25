package it.polimi.ingsw.model.usersdb;


import java.io.Serializable;

class User implements Serializable {

    private String password;
    private byte[] salt;
    private int wonGames;
    private int lostGames;
    private int abandonedGames;
    private int ranking;

    User(String password,byte[] salt){
      this.password=password;
      this.salt=salt;
      wonGames=0;
      lostGames=0;
      abandonedGames=0;
      ranking=0;
    }


    void addWonGames(){
        wonGames++;
    }
    void addLostGames(){
        lostGames++;
    }
    void addAbandonedGames(){
        abandonedGames++;
    }
    void addPointsToRanking(int points){
        ranking+=points;
    }

    String getPassword() {
        return password;
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
