package it.polimi.ingsw.model.usersdb;


import java.io.Serializable;

class UserInDB implements Serializable {
    String password;
    byte[] salt;
    int wonGames;
    int lostGames;
    int abandonedGames;
    int ranking;

}
