package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.exceptions.NotEnoughPlayersException;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;
import java.util.Collections;

public class MultiPlayerGame extends AbstractGame implements Runnable {
    private Thread t;
    private int turnPlayer;
    private int roundPlayer;
    private int currentTurn;

    public MultiPlayerGame(String user, int numPlayers) {
        super(user, numPlayers);
        turnPlayer = 0;
        roundPlayer = 0;
        currentTurn = 1;
        numOfPrivateObjectivesForPlayer = 1;
        numOfToolCards = 3;
        numOfPublicObjectiveCards = 3;
    }

    public void start(){
        if (t == null){
            t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        //Codice che regola il funzionamento della partita
    }

    public int getTurnPlayer() { return turnPlayer; }

    public int getRoundPlayer() { return roundPlayer; }

    public int getCurrentTurn() { return currentTurn; }

//    // Metodo utilizzato solo per testing
//    public void simulateRound(int roundPlayer) {
//        this.roundPlayer = roundPlayer;
//        this.turnPlayer = roundPlayer;
//        this.currentTurn = 1;
//    }

    @Override
    public void startGame() throws NotEnoughPlayersException {
        if (players.size() < numPlayers){
            throw new NotEnoughPlayersException(this);
        }
        extractPrivateObjectives();
        extractWPCs();
        extractToolCards();
        extractPublicObjectives();
        shufflePlayers();
    }

    @Override
    protected void disconnectPlayer(PlayerInGame playerInGame) {

    }

    private void shufflePlayers(){ Collections.shuffle(players); }

    @Override
    public void endGame() {

    }

    @Override
    public void nextRound() {

    }

    @Override
    public void nextTurn() {
        if (currentTurn < 2*getNumPlayers()) {
            turnPlayer = nextPlayer();
            currentTurn++;
        } else {
//            nextRound();
        }
    }

    private int nextPlayer(){
        return 0;
    }

    //Da testare
    @Override
    public void calculateScore() {

    }

    //Da testare
    @Override
    public void saveScore() {

    }

    public void addPlayer(String user){

    }

    public void removePlayer(String user){

    }
}
