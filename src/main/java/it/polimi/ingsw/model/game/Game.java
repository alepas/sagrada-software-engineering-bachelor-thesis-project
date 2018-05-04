package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.dicebag.DiceBag;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.game.gameObservers.GameObserver;
import it.polimi.ingsw.model.game.gameObservers.ObservedGame;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public abstract class Game implements Runnable, Serializable, ObservedGame {
    private ArrayList<ToolCard> toolCards;
    private ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private String id;
    private ArrayList<Dice> extractedDices;
    private transient DiceBag diceBag;

    RoundTrack roundTrack;
    int numPlayers;
    ArrayList<PlayerInGame> players;

    transient int numOfPrivateObjectivesForPlayer;
    transient int numOfToolCards;
    transient int numOfPublicObjectiveCards;

    transient ArrayList<GameObserver> gameObservers;



    //----------------------------- Metodi validi per entrambi i lati -----------------------------

    @Override
    public void addObserver(GameObserver observer){ gameObservers.add(observer); }

    @Override
    public void removeObserver(GameObserver observer) { gameObservers.remove(observer); }

    public ArrayList<ToolCard> getToolCards() {
        return toolCards;
    }

    public ArrayList<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public String getID() { return id; }

    public int getNumPlayers() { return numPlayers; }

    public ArrayList<Dice> getExtractedDices() { return extractedDices; }

    public ArrayList<PlayerInGame> getPlayers() { return players; }

    public void removeExtractedDice(Dice dice){ extractedDices.remove(dice); }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }





    //------------------------------- Metodi validi solo lato server ------------------------------

    Game(int numPlayers) {
        toolCards = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        id = UUID.randomUUID().toString();
        extractedDices = new ArrayList<>();
        diceBag = new DiceBag();

        roundTrack = new RoundTrack();
        this.numPlayers = numPlayers;
        players = new ArrayList<>();

        gameObservers = new ArrayList<>();
    }

    void extractPrivateObjectives() {
        ArrayList<Color> colorsExtracted = new ArrayList<>();
        Color color;

        for (PlayerInGame player : players){
            for (int i = 0; i < numOfPrivateObjectivesForPlayer; i++) {
                do {
                    color = Color.randomColor();
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

    void extractWPCs(){
        ArrayList<String> ids = WpcDB.getWpcIDs();
        Collections.shuffle(ids);
        ChooseWPCThread[] chooseThreads = new ChooseWPCThread[numPlayers];

        int i = 0;

        for(PlayerInGame player : players){
            ChooseWPCThread chooseThread = new ChooseWPCThread(player,
                    new ArrayList<String> (ids.subList(GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*i,
                            GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*(i+1))));
            chooseThreads[i] = chooseThread;
            chooseThread.start();
            i++;
        }

        try {
            System.out.println("Waiting for threads to finish.");
            for (ChooseWPCThread chooseThread : chooseThreads ){
                chooseThread.join(GameConstants.CHOOSE_WPC_WAITING_TIME * 1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }

        for (i = 0; i < chooseThreads.length; i++){
            if (chooseThreads[i].isInterrupted()){
                players.get(i).setWPC(ids.subList(GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*i,
                        GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*(i+1)).get(0));
            }
        }
    }

    void extractToolCards() {
        ArrayList<String> ids = ToolCard.getCardsIDs();
        Collections.shuffle(ids);
        ArrayList<String> toolCardsExtracted = new ArrayList<>(ids.subList(0, numOfToolCards));
        for (String id : toolCardsExtracted){
            toolCards.add(ToolCard.getCardByID(id));
        }
    }

    void extractPublicObjectives(){
        ArrayList<String> ids = PublicObjectiveCard.getCardsIDs();
        Collections.shuffle(ids);
        ArrayList<String> publicCardsExtracted = new ArrayList<>(ids.subList(0, numOfPublicObjectiveCards));
        for (String id : publicCardsExtracted){
            publicObjectiveCards.add(PublicObjectiveCard.getCardByID(id));
        }
    }

    public int getNumOfPrivateObjectivesForPlayer() {
        return numOfPrivateObjectivesForPlayer;
    }

    public int getNumOfToolCards() {
        return numOfToolCards;
    }

    public int getNumOfPublicObjectiveCards() {
        return numOfPublicObjectiveCards;
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }





    //------------------------------- Metodi validi solo lato client -------------------------------

    public void initializeObservers(){ gameObservers = new ArrayList<>(); }

    public void setToolCards(ArrayList<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    public void setPublicObjectiveCards(ArrayList<PublicObjectiveCard> publicObjectiveCards) {
        this.publicObjectiveCards = publicObjectiveCards;
    }

    public void setExtractedDices(ArrayList<Dice> extractedDices) {
        this.extractedDices = extractedDices;
    }





    //--------------------------------------- Metodi astratti --------------------------------------

    abstract void initializeGame();
    abstract void endGame();
    abstract void nextRound();
    public abstract void nextTurn();
    abstract void calculateScore();
    abstract void saveScore();

}
