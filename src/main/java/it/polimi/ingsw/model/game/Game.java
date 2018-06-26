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
import it.polimi.ingsw.model.exceptions.gameExceptions.CannotCreatePlayerException;
import it.polimi.ingsw.model.exceptions.gameExceptions.MaxPlayersExceededException;
import it.polimi.ingsw.model.exceptions.gameExceptions.NotYourWpcException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserAlreadyInThisGameException;
import it.polimi.ingsw.model.game.thread.ChooseWpcThread;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.*;

public abstract class Game extends Observable implements Runnable {
    protected ArrayList<ToolCard> toolCards;
    protected ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private String id;
    protected ArrayList<Dice> extractedDices;
    protected DiceBag diceBag;
    protected boolean turnFinished;

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

    HashMap<String, ArrayList<String>> wpcsByUser = new HashMap<>();

    Game(int numPlayers) {
        toolCards = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        id = UUID.randomUUID().toString();
        extractedDices = new ArrayList<>();
        diceBag = new DiceBag();

        roundTrack = new RoundTrack();
        this.numPlayers = numPlayers;
        players = new PlayerInGame[numPlayers];
        turnFinished = false;
    }

    public ClientGame getClientGame(){
        ClientGame clientCopy = new ClientGame(this.id, this.numPlayers);
        clientCopy.setGameActualPlayers(this.numActualPlayers());

        for(PlayerInGame player : players){
            clientCopy.setUserWpc(player.getUser(), player.getWPC().getClientWpc());
            clientCopy.setUserFavours(player.getUser(), player.getFavours());
        }

        ArrayList<ClientToolCard> clientToolCards = new ArrayList<>();
        for(ToolCard card : this.toolCards) clientToolCards.add(card.getClientToolcard());
        clientCopy.setToolCards(clientToolCards);

        ArrayList<ClientPoc> clientPocs = new ArrayList<>();
        for(PublicObjectiveCard card : this.publicObjectiveCards) clientPocs.add(card.getClientPoc());
        clientCopy.setPublicObjectiveCards(clientPocs);

        ArrayList<ClientDice> clientDices = new ArrayList<>();
        for(Dice dice : this.extractedDices) clientDices.add(dice.getClientDice());
        clientCopy.setExtractedDices(clientDices);

        clientCopy.setCurrentTurn(this.currentTurn);
        clientCopy.setRoundTrack(this.roundTrack.getClientRoundTrack());

        return clientCopy;
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

    public HashMap<String, ArrayList<String>> getWpcsByUser() { return wpcsByUser; }

    /**
     * @return true if the game has all players
     */
    public boolean isFull(){ return !(players[players.length-1] == null); }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void changeAndNotifyObservers(Object arg){
        this.setChanged();
        notifyObservers(arg);
    }

    /**
     * Checks if there are null elements inside the players array, if there's still space it means that the game isn't
     * full yet and that is still possible to add players to the game.
     *
     * @return the index of the first available position in the players array or -1 if the array is full
     */
    int nextFree(){
        if (isFull()) return -1; //todo: serve davvero? quando si esce dal ciclo for tecnicamente è perche è full
        for(int i = 0; i < players.length; i++){
            if (players[i] == null) return i;
        }
        return -1;
    }

    /**
     * Searches the player with the given username inside the players array
     *
     * @param username is the username of a player
     * @return the index of the player with the given username inside the players array or -1 if the username isn't
     * associated to one of the game's players
     */
    int playerIndex(String username){
        for(int i = 0; i < players.length; i++){
            if (players[i] == null) return -1;
            if (players[i].getUser().equals(username)) return i;
        }
        return -1;
    }


    /**
     * Removes the element in the given index from the given array and moves all the others
     *
     * @param array is a undefined array, it could be for example the player one
     * @param index is the index in the array of the element that will be removed
     */
    void removeArrayIndex(Object[] array, int index){
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

    /**
     * @return the actual number of players: it is equals to the numPlayers if the game is full, if not to the index of
     * the first null position in the players array
     */
    public int numActualPlayers(){
        if (isFull()) return numPlayers;
        return nextFree();
    }

    /**
     * Extracts in  a random way a private objective for each player in the game. A private objective is a color chosen
     * from the color enum, every player must have a different color from the others. Sets the ClientColor and the
     * Hashmap of players and private Objectives.
     */
    void extractPrivateObjectives() {
        ArrayList<Color> colorsExtracted = new ArrayList<>();
        Color color;
        HashMap<String, ClientColor[]> colorsByUser = new HashMap<>();

        for (PlayerInGame player : players){
            for (int i = 0; i < numOfPrivateObjectivesForPlayer; i++) {

                do {
                    color = Color.randomColor();
                }while (colorsExtracted.contains(color));

                colorsExtracted.add(color);
                player.setPrivateObjs(color, i);
            }
            Color[] playerColors = player.getPrivateObjs();
            int numPrivateObjs = playerColors.length;
            ClientColor[] playerClientColors = new ClientColor[numPrivateObjs];

            for(int j = 0; j < numPrivateObjs; j++)
                playerClientColors[j] = Color.getClientColor(playerColors[j]);

            colorsByUser.put(player.getUser(), playerClientColors);
        }

        changeAndNotifyObservers(new PrivateObjExtractedNotification(colorsByUser));
    }

    /**
     *Extracts in a random way 4 schemas for each player, creates the ClientWpc equal to the chosen ones
     */
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

    /**
     * Selects four random different schemas from the 24 given and associates the ids to  the user with an HashMap
     *
     * @return an HashMap containing for each String key (player username) an ArrayList with four different ids of four
     * different schemas
     */
    private HashMap<String, ArrayList<String>> extractRandomWpcsForUser(){
        ArrayList<String> ids = wpcDB.getWpcIDs();
        Collections.shuffle(ids);
        HashMap<String, ArrayList<String>> wpcsByUser = new HashMap<>();

        int numOfWpcs = GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER;

        for(int i = 0; i < players.length; i++){
            wpcsByUser.put(players[i].getUser(), new ArrayList<>(
                    ids.subList(numOfWpcs * i, numOfWpcs * (i + 1))));
        }

        return wpcsByUser;
    }

    /**
     * Sets in the player in game object the chosen schema.
     *
     * @param player is the player that has chosen a schema before the timer's end
     * @param wpcID is the id of the chosen schema
     * @throws NotYourWpcException if the chosen id is not one of the given to each player
     */
    public void setPlayerWpc(PlayerInGame player, String wpcID) throws NotYourWpcException {
        ArrayList<String> userWPCs = wpcsByUser.get(player.getUser());
        if (userWPCs == null || !userWPCs.contains(wpcID)) throw new NotYourWpcException(wpcID);
        player.setWPC(wpcID);
        changeAndNotifyObservers(new UserPickedWpcNotification(player.getUser(), wpcDB.getClientWpcByID(wpcID)));
    }

    /**
     * Chooses in a random way 3 ToolCard from the 12 available in the ToolCard data base
     */
    void extractToolCards() {
        ArrayList<String> ids = toolcardDB.getCardsIDs();
        Collections.shuffle(ids);

        ids.clear();
        ids.add("12");
        ids.add("11");
        ids.add("10");

        ArrayList<String> toolCardsExtracted = new ArrayList<>(ids.subList(0, numOfToolCards));
        ArrayList<ClientToolCard> clientToolCards = new ArrayList<>();

        for (String id : toolCardsExtracted){
            ToolCard card = toolcardDB.getCardByID(id);
            toolCards.add(card);
            clientToolCards.add(card.getClientToolcard());
        }

        changeAndNotifyObservers(new ToolcardsExtractedNotification(clientToolCards));
    }

    /**
     *Chooses in a random way 3 Public Objective Cards from the 12 available in the Private Objective Cards data base
     */
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

    /**
     * @return the object diceBag
     */
    public DiceBag getDiceBag() { return diceBag; }


    public boolean isTurnFinished() {
        return turnFinished;
    }

    public void setTurnFinished(boolean turnFinished) {
        this.turnFinished = turnFinished;
    }

    public abstract boolean isSinglePlayerGame();

    //--------------------------------------- Abstract Methods --------------------------------------

    abstract void initializeGame();
    abstract void endGame();
    abstract void nextRound();
    public abstract void endTurn(ClientEndTurnData endTurnData);
    abstract void calculateScore();
    abstract void saveScore();
    abstract void waitForWpcResponse();
    public abstract void removeToolCardIfSingleGame(ToolCard card);
    public abstract boolean addPlayer(String user) throws MaxPlayersExceededException, UserAlreadyInThisGameException, CannotCreatePlayerException;


}