package it.polimi.ingsw.model.game;

import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.cards.PocDB;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.cards.ToolCardDB;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.dicebag.DiceBag;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.*;

public abstract class Game extends Observable implements Runnable {
    protected ArrayList<ToolCard> toolCards;
    protected ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private String id;
    protected ArrayList<Dice> extractedDices;
    protected DiceBag diceBag;

    RoundTrack roundTrack;
    int numPlayers;
    protected int currentTurn;
    PlayerInGame[] players;

    int numOfPrivateObjectivesForPlayer;
    int numOfToolCards;
    int numOfPublicObjectiveCards;

    WpcDB wpcDB = WpcDB.getInstance();
    ToolCardDB toolcardDB = ToolCardDB.getInstance();
    PocDB pocDB = PocDB.getInstance();

    private HashMap<String, ArrayList<String>> wpcsByUser = new HashMap<>();

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



    //----------------------------------------------------------------------------------

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

    public HashMap<String, ArrayList<String>> getWpcsByUser() {
        return wpcsByUser;
    }

    public boolean isFull(){
        return !(players[players.length-1] == null);
    }

    public int getCurrentTurn() {
        return currentTurn;
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
        HashMap<String, ClientColor[]> colorsByUser = new HashMap<>();


        for (PlayerInGame player : players){
            for (int i = 0; i < numOfPrivateObjectivesForPlayer; i++) {
                do {
                    color = Color.randomColor();
                } while (colorsExtracted.contains(color));

                colorsExtracted.add(color);
                player.setPrivateObjs(color, i);
            }

            Color[] playerColors = player.getPrivateObjs();
            int numPrivateObjs = playerColors.length;
            ClientColor[] playerClientColors = new ClientColor[numPrivateObjs];

            for(int j = 0; j < numPrivateObjs; j++){
                playerClientColors[j] = Color.getClientColor(playerColors[j]);
            }

            colorsByUser.put(player.getUser(), playerClientColors);
        }


        changeAndNotifyObservers(new PrivateObjExtractedNotification(colorsByUser));
    }

    void extractWPCs(){
        wpcsByUser = extractRandomWpcsForUser();
        HashMap<String, ArrayList<ClientWpc>> clientWpcByUser = new HashMap<>();

        for(PlayerInGame player : players){
            ArrayList<String> wpcIDs = wpcsByUser.get(player.getUser());
            ArrayList<ClientWpc> userClientWpcs = new ArrayList<>();

            for(String id : wpcIDs){
                userClientWpcs.add(wpcDB.getClientWpcByID(id));
            }

            clientWpcByUser.put(player.getUser(), new ArrayList<>(userClientWpcs));
        }

        changeAndNotifyObservers(new WpcsExtractedNotification(clientWpcByUser));
        waitForWpcResponse();
    }

    private HashMap<String, ArrayList<String>> extractRandomWpcsForUser(){
        ArrayList<String> ids = wpcDB.getWpcIDs();
        Collections.shuffle(ids);
        HashMap<String, ArrayList<String>> wpcsByUser = new HashMap<>();

        int numOfWpcs = GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER;

        for(int i = 0; i < players.length; i++){
            wpcsByUser.put(players[i].getUser(), new ArrayList<String> (
                    ids.subList(numOfWpcs*i, numOfWpcs*(i+1))));
        }

        return wpcsByUser;
    }

    private void waitForWpcResponse() {
        Thread waitForWpcs = new Thread(new ChooseWpcThread(players));

        try {
            System.out.println("Sto aspettando che i giocatori scelgano le wpc");
            waitForWpcs.start();
            waitForWpcs.join(GameConstants.CHOOSE_WPC_WAITING_TIME + GameConstants.TASK_DELAY);
            System.out.println("Ho smesso di aspettare che i giocatori scelgano le wpc");
            if (waitForWpcs.isAlive()) {
                waitForWpcs.interrupt();
                selectRandomWpc(wpcsByUser);
                System.out.println("Ho estratto casualmente le wpc dei giocatori rimanenti");
            }
        } catch (InterruptedException e){

        }
    }

    private void selectRandomWpc(HashMap<String,ArrayList<String>> wpcsByUser) {
        for (PlayerInGame player : players){
            if (player.getWPC() == null) {
                try {
                    Random r = new Random();
                    setPlayerWpc(player, wpcsByUser.get(player.getUser()).get(r.nextInt(GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER)));
                } catch (NotYourWpcException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setPlayerWpc(PlayerInGame player, String wpcID) throws NotYourWpcException {
        ArrayList<String> userWPCs = wpcsByUser.get(player.getUser());
        if (userWPCs == null || !userWPCs.contains(wpcID)) throw new NotYourWpcException(wpcID);
        player.setWPC(wpcID);
        changeAndNotifyObservers(new UserPickedWpcNotification(player.getUser(), wpcDB.getClientWpcByID(wpcID)));
    }

    void extractToolCards() {
        ArrayList<String> ids = toolcardDB.getCardsIDs();
        Collections.shuffle(ids);

        ids.clear();
        ids.add("1");
        ids.add("2");
        ids.add("3");

        ArrayList<String> toolCardsExtracted = new ArrayList<>(ids.subList(0, numOfToolCards));
        ArrayList<ClientToolCard> clientToolCards = new ArrayList<>();

        for (String id : toolCardsExtracted){
            ToolCard card = toolcardDB.getCardByID(id);
            toolCards.add(card);
            clientToolCards.add(card.getClientToolcard());
        }

        changeAndNotifyObservers(new ToolcardsExtractedNotification(clientToolCards));
    }

    void extractPublicObjectives(){
        ArrayList<String> ids = pocDB.getCardsIDs();
        Collections.shuffle(ids);
        ArrayList<String> publicCardsExtracted = new ArrayList<>(ids.subList(0, numOfPublicObjectiveCards));
        ArrayList<ClientPoc> clientPocs = new ArrayList<>();

        for (String id : publicCardsExtracted){
            PublicObjectiveCard card = pocDB.getCardByID(id);
            publicObjectiveCards.add(card);
            clientPocs.add(card.getClientPoc());
        }

        changeAndNotifyObservers(new PocsExtractedNotification(clientPocs));
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }


    public abstract boolean isSinglePlayerGame();

    //--------------------------------------- Metodi astratti --------------------------------------

    abstract void initializeGame();
    abstract void endGame();
    abstract void nextRound();
    public abstract void endTurn(ClientEndTurnData endTurnData);
    abstract void calculateScore();
    abstract void saveScore();


}