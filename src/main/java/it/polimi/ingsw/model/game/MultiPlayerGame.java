package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.usersdb.User;

public class MultiPlayerGame extends AbstractGame {
    int turnPlayer;
    int roundPlayer;
    int currentTurn;

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

    public boolean addPlayer(User user){
        return false;
    }

    public void removePlayer(User user){

    }
}
