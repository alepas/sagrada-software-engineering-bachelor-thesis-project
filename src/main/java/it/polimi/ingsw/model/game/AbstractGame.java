package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.dicebag.DiceBag;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public abstract class AbstractGame {
    private ArrayList<ToolCard> toolCards;
    private ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private String gameID;
    private ArrayList<Dice> extractedDices;
    private DiceBag diceBag;

    int numPlayers;
    RoundTrack roundTrack;
    ArrayList<PlayerInGame> players;

    int numOfPrivateObjectivesForPlayer;
    int numOfToolCards;
    int numOfPublicObjectiveCards;

    AbstractGame(String user, int numPlayers) {
        toolCards = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        gameID = UUID.randomUUID().toString();
        this.numPlayers = numPlayers;
        roundTrack = new RoundTrack();
        extractedDices = new ArrayList<>();
        players = new ArrayList<>();
        PlayerInGame player = new PlayerInGame(user, this);
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

    public ArrayList<ToolCard> getToolCards() {
        return toolCards;
    }

    public ArrayList<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public String getGameID() { return gameID; }

    public int getNumPlayers() { return numPlayers; }

    public ArrayList<Dice> getExtractedDices() { return extractedDices; }

    public ArrayList<PlayerInGame> getPlayers() { return players; }

    public void removeExtractedDice(Dice dice){ extractedDices.remove(dice); }

}
