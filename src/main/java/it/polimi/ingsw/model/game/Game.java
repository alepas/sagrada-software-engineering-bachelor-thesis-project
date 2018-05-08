package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.dicebag.DiceBag;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;

import it.polimi.ingsw.control.network.commands.responses.notifications.PrivateObjExtractedNotification;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.*;

public abstract class Game extends Observable implements Runnable {
    private ArrayList<ToolCard> toolCards;
    private ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private String id;
    private ArrayList<Dice> extractedDices;
    private DiceBag diceBag;

    RoundTrack roundTrack;
    int numPlayers;
    PlayerInGame[] players;

    int numOfPrivateObjectivesForPlayer;
    int numOfToolCards;
    int numOfPublicObjectiveCards;

    Game(int numPlayers) {
        toolCards = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        id = UUID.randomUUID().toString();
        extractedDices = new ArrayList<>();
        diceBag = new DiceBag();

        roundTrack = new RoundTrack();
        this.numPlayers = numPlayers;
        players = new PlayerInGame[numPlayers];
    }





    public ArrayList<ToolCard> getToolCards() {
        return toolCards;
    }

    public ArrayList<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public String getID() { return id; }

    public int getNumPlayers() { return numPlayers; }

    public ArrayList<Dice> getExtractedDices() { return extractedDices; }

    public PlayerInGame[] getPlayers() { return players; }

    public void removeExtractedDice(Dice dice){ extractedDices.remove(dice); }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public boolean isFull(){
        return !(players[players.length-1] == null);
    }

    public void changeAndNotifyObservers(Object arg){
        this.setChanged();
        notifyObservers(arg);
    }

    public int nextFree(){
        //Restituisce la prima posizione null nell'array dei players
        //-1 se nessun valore è a null
        if (isFull()) return -1;
        for(int i = 0; i < players.length; i++){
            if (players[i] == null) return i;
        }
        return -1;
    }

    public int playerIndex(String username){
        for(int i = 0; i < players.length; i++){
            if (players[i] == null) return -1;
            if (players[i].getUser().equals(username)) return i;
        }
        return -1;
    }

    public void removeArrayIndex(Object[] array, int index){
        for(int i = index; i < array.length; i++){
            if (i == array.length-1) {
                array[i] = null;
                return;
            }
            if (array[i+1] == null) {
                array[i] = null;
                return;
            }
            Object temp = array[i];
            array[i] = array[i+1];
            array[i+1] = temp;
        }
    }

    public int numActualPlayers(){
        if (isFull()) return numPlayers;
        return nextFree();
    }

    void extractPrivateObjectives() {
        ArrayList<Color> colorsExtracted = new ArrayList<>();
        Color color;
        HashMap<String, Color[]> colorsByUser = new HashMap<>();


        for (PlayerInGame player : players){
            for (int i = 0; i < numOfPrivateObjectivesForPlayer; i++) {
                do {
                    color = Color.randomColor();
                } while (colorsExtracted.contains(color));

                colorsExtracted.add(color);
                player.setPrivateObjs(color, i);
            }

            colorsByUser.put(player.getUser(), player.getPrivateObjs());
        }


        changeAndNotifyObservers(new PrivateObjExtractedNotification(colorsByUser));
    }

    void extractWPCs(){
        //TODO:

//        ArrayList<String> ids = WpcDB.getWpcIDs();
//        Collections.shuffle(ids);
//        ChooseWPCThread[] chooseThreads = new ChooseWPCThread[numPlayers];
//
//        int i = 0;
//
//        for(PlayerInGame player : colorsByUser){
//            ChooseWPCThread chooseThread = new ChooseWPCThread(player,
//                    new ArrayList<String> (ids.subList(GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*i,
//                            GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*(i+1))));
//            chooseThreads[i] = chooseThread;
//            chooseThread.start();
//            i++;
//        }
//
//        try {
//            System.out.println("Waiting for threads to finish.");
//            for (ChooseWPCThread chooseThread : chooseThreads ){
//                chooseThread.join(GameConstants.CHOOSE_WPC_WAITING_TIME * 1000);
//            }
//        } catch (InterruptedException e) {
//            System.out.println("Main thread Interrupted");
//        }
//
//        for (i = 0; i < chooseThreads.length; i++){
//            if (chooseThreads[i].isInterrupted()){
//                colorsByUser.get(i).setWPC(ids.subList(GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*i,
//                        GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER*(i+1)).get(0));
//            }
//        }
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

    public DiceBag getDiceBag() {
        return diceBag;
    }


    //--------------------------------------- Metodi astratti --------------------------------------

    abstract void initializeGame();
    abstract void endGame();
    abstract void nextRound();
    public abstract void nextTurn();
    abstract void calculateScore();
    abstract void saveScore();

}
