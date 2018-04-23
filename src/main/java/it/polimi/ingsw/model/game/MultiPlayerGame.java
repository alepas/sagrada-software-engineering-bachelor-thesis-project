package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.exceptions.NotEnoughPlayersException;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;
import java.util.Collections;

public class MultiPlayerGame extends AbstractGame implements Runnable {
    private Thread t;
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

    //Non da testare
    public int getTurnPlayer() { return turnPlayer; }

    //Non da testare
    public int getRoundPlayer() { return roundPlayer; }

    //Non da testare
    public int getCurrentTurn() { return currentTurn; }

//    // Metodo utilizzato solo per testing
//    public void simulateRound(int roundPlayer) {
//        this.roundPlayer = roundPlayer;
//        this.turnPlayer = roundPlayer;
//        this.currentTurn = 1;
//    }

    //Coperto dai test
    @Override
    public void startGame() throws NotEnoughPlayersException {
        if (players.size() < numPlayers){
            throw new NotEnoughPlayersException(this);
        }
        extractPrivateObjectives();
        extractWPC();
        extractToolCard();
        extractPublicObjectives();
        shufflePlayers();
        nextRound();
    }

    private void extractPrivateObjectives() {
        ArrayList<Color> colorExtracted = new ArrayList<>();
        Color color;

        for (PlayerInGame player : players){
            do{
                color = Color.randomColour();
            } while (colorExtracted.contains(color));

            colorExtracted.add(color);
            player.setPrivateObjective1(color);
        }
    }

    //Non da testare
    @Override
    void extractWPC() {
        ArrayList<String> ids = WPC.getWPCids();
        Collections.shuffle(ids);
        ChooseWPCThread[] chooseThreads = new ChooseWPCThread[numPlayers];

        int i = 0;

        for(PlayerInGame player : players){
            ChooseWPCThread chooseThread = new ChooseWPCThread(player, (ArrayList<String>) ids.subList(4*i, 4*i+3));
            chooseThreads[i] = chooseThread;
            chooseThread.start();
            i++;
        }

        try {
            int chooseWpcWaitingTime = 60;
            System.out.println("Waiting for threads to finish.");
            for (ChooseWPCThread chooseThread : chooseThreads ){
                chooseThread.join(chooseWpcWaitingTime * 1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }

        for (i = 0; i < chooseThreads.length; i++){
            if (chooseThreads[i].isInterrupted()){
                disconnectPlayer(players.get(i));
            }
        }
    }

    private void disconnectPlayer(PlayerInGame playerInGame) {

    }


    private void extractToolCard() {

    }

    private void extractPublicObjectives() {

    }

    //Non da testare
    private void shufflePlayers(){
        Collections.shuffle(players);
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
