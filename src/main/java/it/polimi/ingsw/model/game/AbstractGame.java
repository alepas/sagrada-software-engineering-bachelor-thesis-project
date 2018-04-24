package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.DiceBag;
import it.polimi.ingsw.model.RoundTrack;
import it.polimi.ingsw.model.WPC.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public abstract class AbstractGame {
    ArrayList<ToolCard> toolCards;
    ArrayList<PublicObjectiveCard> publicObjectiveCards;
    String gameID;
    int numPlayers;
    RoundTrack roundTrack;
    ArrayList<Dice> extractedDices;
    ArrayList<PlayerInGame> players;
    DiceBag diceBag;

    int numOfPrivateObjectivesForPlayer;
    int numOfToolCards;
    int numOfPublicObjectiveCards;

    //Altre costanti utilizzate
    int chooseWpcWaitingTime = 60;

    AbstractGame(String user, int numPlayers) {
        toolCards = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        gameID = UUID.randomUUID().toString();
        this.numPlayers = numPlayers;
        roundTrack = new RoundTrack();
        extractedDices = new ArrayList<>();
        players = new ArrayList<>();
        PlayerInGame player = new PlayerInGame(user);
        players.add(player);
        diceBag = new DiceBag();
        //Nota: settare il numero di private objectives nel costruttore della classe concreta
        //Nota: settare il numero di toolcard nel costruttore della classe concreta
        //Nota: settare il numero di public objectives nel costruttore della classe concreta
    }



    abstract void startGame();
    abstract void endGame();
    abstract void nextRound();
    abstract void nextTurn();
    abstract void calculateScore();
    abstract void saveScore();

    protected void extractWPCs(){
        ArrayList<String> ids = WPC.getWpcIDs();
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

    protected void extractPrivateObjectives() {
        ArrayList<Color> colorsExtracted = new ArrayList<>();
        Color color;

        for (PlayerInGame player : players){
            for (int i = 0; i < numOfPrivateObjectivesForPlayer; i++) {
                do {
                    color = Color.randomColour();
                } while (colorsExtracted.contains(color));

                colorsExtracted.add(color);
                if (i == 0) {
                    player.setPrivateObjective1(color);
                } else {
                    player.setPrivateObjective2(color);
                }
            }
        }
    }

    protected void extractToolCards() {
        ArrayList<String> ids = ToolCard.getCardsIDs();
        Collections.shuffle(ids);
        ArrayList<String> toolCardsExtracted = (ArrayList<String>) ids.subList(0, numOfToolCards-1);
        for (String id : toolCardsExtracted){
            toolCards.add(ToolCard.getCardByID(id));
        }
    }

    protected void extractPublicObjectives(){
        ArrayList<String> ids = PublicObjectiveCard.getCardsIDs();
        Collections.shuffle(ids);
        ArrayList<String> publicCardsExtracted = (ArrayList<String>) ids.subList(0, numOfPublicObjectiveCards-1);
        for (String id : publicCardsExtracted){
            publicObjectiveCards.add(PublicObjectiveCard.getCardByID(id));
        }
    }

    protected abstract void disconnectPlayer(PlayerInGame playerInGame);


    public ArrayList<ToolCard> getToolCards() {
        return toolCards;
    }

    public ArrayList<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public String getGameID() {
        return gameID;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<Dice> getExtractedDices() {
        return extractedDices;
    }

    public ArrayList<PlayerInGame> getPlayers() {
        return players;
    }

    public void removeExtractedDice(Dice dice){
        extractedDices.remove(dice);
    }

}
