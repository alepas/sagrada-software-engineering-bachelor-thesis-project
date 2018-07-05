package server.model.users;


import shared.clientinfo.ClientUser;

import java.io.Serializable;


/**
 * This class is the user class. It contains the login data of the user (username, password and salt used for the
 * hashing process of the password) and its statistics: won games, lost games, abandoned games, ranking.
 */
class User implements Serializable {

    private String username;
    private String password;
    private byte[] salt;
    private int wonGames;
    private int lostGames;
    private int abandonedGames;
    private int ranking;


    /**
     * Constructor of a user object. All the statistics data are set to 0.
     *
     * @param username is the username of the user.
     * @param password is the password chosen by the user.
     * @param salt is the salt used during the hashing process.
     */
    User(String username, String password,byte[] salt){
        this.username = username;
        this.password=password;
        this.salt=salt;
        wonGames=0;
        lostGames=0;
        abandonedGames=0;
        ranking=0;
    }


    /**
     * Returns the ClientUser object corresponding to the current user object
     *
     * @return a ClientUser object with the same values of the current object user
     */
    ClientUser getClientUser(){
        return new ClientUser(this.username, this.wonGames,
                this.lostGames, this.abandonedGames, this.ranking);
    }


    /**
     * Increments the number of won games of the user
     */
    void addWonGames(){
        wonGames++;
    }


    /**
     * Removes a won game from the user
     */
    void removeWonGames(){wonGames--;}


    /**
     * Increments the number of lost games of the user
     */
    void addLostGames(){
        lostGames++;
    }


    /**
     * Removes a lost game from the user
     */
    void removeLostGames(){lostGames--;}


    /**
     * Increments the number of abandoned games of the user
     */
    void addAbandonedGames(){
        abandonedGames++;
    }


    /**
     * Removes a game from the lost games of the user
     */
    void removeAbandonedGames(){abandonedGames--;}


    /**
     * Adds points to the ranking of the user
     *
     * @param points are the points to add or remove from the player ranking. If points is a number > 0 the points will
     *              be added, if is a number < 0 the points will be removed.
     */
    void addPointsToRanking(int points){
        ranking+=points;
    }


    /**
     * @return the password of the user
     */
    String getPassword() {
        return password;
    }


    /**
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }


    /**
     * @return the salt of the user
     */
    byte[] getSalt() {
        return salt;
    }


    /**
     * @return the number of the games won by the user
     */
    int getWonGames() {
        return wonGames;
    }


    /**
     * @return the number of the games lost by the user
     */
    int getLostGames() {
        return lostGames;
    }


    /**
     * @return the number of the games abandoned by the user
     */
    int getAbandonedGames() {
        return abandonedGames;
    }


    /**
     * @return the points of the ranking of the user
     */
    int getRanking() {
        return ranking;
    }

}
