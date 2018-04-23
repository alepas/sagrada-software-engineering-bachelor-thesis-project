package it.polimi.ingsw.model.game;

import java.util.Collections;

public class MultiPlayerGame extends AbstractGame {
    private int turnPlayer;
    private int roundPlayer;
    private int currentTurn;

    //Coperto dai test
    public MultiPlayerGame(String user, int numPlayers) {
        super(user, numPlayers);
        turnPlayer = 0;
        roundPlayer = 0;
        currentTurn = 1;
    }

    //Non da testare
    public int getTurnPlayer() {
        return turnPlayer;
    }

    //Non da testare
    public int getRoundPlayer() {
        return roundPlayer;
    }

    //Non da testare
    public int getCurrentTurn() {
        return currentTurn;
    }

//    // Metodo utilizzato solo per testing
//    public void simulateRound(int roundPlayer) {
//        this.roundPlayer = roundPlayer;
//        this.turnPlayer = roundPlayer;
//        this.currentTurn = 1;
//    }

    //Coperto dai test
    @Override
    public void startGame() {
        shufflePlayers();
        extractWPC();
    }

    //Non da testare
    private void shufflePlayers(){
        Collections.shuffle(players);
    }

    //Non da testare
    @Override
    void extractWPC() {

    }

    @Override
    public void endGame() {

    }

    //Coperto dai test
    @Override
    public void nextRound() {

    }

    //Coperto dai test
    @Override
    public void nextTurn() {
        if (currentTurn < 2*getNumPlayers()) {
            turnPlayer = nextPlayer();
            currentTurn++;
        } else {
//            nextRound();
        }
    }

    //Non da testare
    private int nextPlayer(){
        return 0;
    }

    @Override
    public void calculateScore() {

    }

    @Override
    public void saveScore() {

    }

    //Coperto dai test
    public void addPlayer(String user){

    }

    //Coperto dai test
    public void removePlayer(String user){

    }
}
