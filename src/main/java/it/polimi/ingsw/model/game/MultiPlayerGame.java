package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.usersdb.User;

public class MultiPlayerGame extends AbstractGame {
    int turnPlayer;
    int roundPlayer;
    int currentTurn;

    public MultiPlayerGame(User user, int numPlayers) {
        super(user, numPlayers);
    }

    @Override
    void startGame() {

    }

    @Override
    void endGame() {

    }

    @Override
    void nextRound() {

    }

    @Override
    void nextTurn() {

    }

    @Override
    void extractWPC() {

    }

    @Override
    void calculateScore() {

    }

    @Override
    void saveScore() {

    }
}
