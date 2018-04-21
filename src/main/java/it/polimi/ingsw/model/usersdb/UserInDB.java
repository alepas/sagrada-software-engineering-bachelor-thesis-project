package it.polimi.ingsw.model.usersdb;


import java.io.Serializable;

class UserInDB implements Serializable {
    String password;
    byte[] salt;
    int wonGames;
    int lostGames;
    int abandonedGames;
    int ranking;

/*
    private UserInDB(String pwd, byte[] salt){
        password=pwd;
        this.salt=salt;
        wonGames=0;
        lostGames=0;
        abandonedGames=0;
        ranking=0;
    }
    private UserInDB(String pwd, byte[] salt, int){
        password=pwd;
        this.salt=salt;
        wonGames=won;
        lostGames=0;
        abandonedGames=0;
        ranking=0;
    }
*/

}
