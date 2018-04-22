package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.usersdb.User;

public class MultiPlayerGame extends AbstractGame {
    private int turnPlayer;
    private int roundPlayer;
    private int currentTurn;

    //Coperto dai test
    public MultiPlayerGame(User user, int numPlayers) {
        super(user, numPlayers);
    }

    public int getTurnPlayer() {
        return turnPlayer;
    }

    public int getRoundPlayer() {
        return roundPlayer;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void endGame() {

    }

    @Override
    public void nextRound() {

    }

    @Override
    public void nextTurn() {

    }

    @Override
    public void extractWPC() {

    }

    @Override
    public void calculateScore() {

    }

    @Override
    public void saveScore() {

    }

    //Coperto dai test
    public boolean addPlayer(User user){
        return false;
    }

    //Coperto dai test
    public void removePlayer(User user){

    }
}
