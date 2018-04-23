package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.exceptions.NotEnoughPlayersException;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;
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
        int i = 0;

        for(PlayerInGame player : players){
            player.chooseWPC(ids.subList(4*i, 4*i+3));
            i++;
        }
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
