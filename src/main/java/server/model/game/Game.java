package server.model.game;

import server.constants.GameConstants;
import server.model.cards.PocDB;
import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.cards.ToolCardDB;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.dicebag.DiceBag;
import server.model.gamesdb.DatabaseGames;
import server.model.users.DatabaseUsers;
import server.model.users.PlayerInGame;
import server.model.wpc.Wpc;
import server.model.wpc.WpcDB;
import shared.clientinfo.*;
import shared.exceptions.gameexceptions.*;
import shared.network.commands.notifications.*;

import java.util.*;

public abstract class Game extends Observable implements Runnable {
    ArrayList<ToolCard> toolCards;
    ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private String id;
    ArrayList<Dice> extractedDices;
    DiceBag diceBag;
    boolean turnFinished;
    boolean allWpcsChosen;


    RoundTrack roundTrack;
    int numPlayers;
    int currentTurn;
    PlayerInGame[] players;
    boolean started;
    int turnPlayer;


    int numOfPrivateObjectivesForPlayer;
    int numOfToolCards;
    int numOfPublicObjectiveCards;

    private WpcDB wpcDB = WpcDB.getInstance();
    private ToolCardDB toolcardDB = ToolCardDB.getInstance();
    private PocDB pocDB = PocDB.getInstance();

    HashMap<String, ArrayList<String>> wpcsByUser = new HashMap<>();

    /**
     * Initializes all parameters that must be inside the game.
     *
     * @param numPlayers depending on the number of players a different type of game will be created.
     */
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
        started = false;
        allWpcsChosen=false;
    }

    /**
     * Creates a copy of the game in the client.
     *
     * @return a client game
     */
    public ClientGame getClientGame(){
        HashMap<String,ArrayList<ClientWpc>> wpcProposed = getWpcProposed();
        ClientGame clientCopy = new ClientGame(this.id, this.numPlayers, wpcProposed);
        clientCopy.setGameActualPlayers(this.numActualPlayers());
        clientCopy.setCurrentTurn(this.currentTurn);
        clientCopy.setRoundTrack(this.roundTrack.getClientRoundTrack());
        clientCopy.setStarted(started);

            Wpc tempWpc;
            for (PlayerInGame player : players) {
                tempWpc=player.getWPC();
                if (tempWpc!=null) {
                    clientCopy.setUserWpc(player.getUser(), tempWpc.getClientWpc());
                    clientCopy.setUserFavours(player.getUser(), player.getFavours());
                }
            }

            ArrayList<ClientToolCard> clientToolCards = new ArrayList<>();
            for (ToolCard card : this.toolCards) clientToolCards.add(card.getClientToolcard());
            clientCopy.setToolCards(clientToolCards);

            ArrayList<ClientPoc> clientPocs = new ArrayList<>();
            for (PublicObjectiveCard card : this.publicObjectiveCards) clientPocs.add(card.getClientPoc());
            clientCopy.setPublicObjectiveCards(clientPocs);

            ArrayList<ClientDice> clientDices = new ArrayList<>();
            for (Dice dice : this.extractedDices) clientDices.add(dice.getClientDice());
            clientCopy.setExtractedDices(clientDices);

        return clientCopy;
    }

    HashMap<String,ArrayList<ClientWpc>> getWpcProposed() {
        HashMap<String, ArrayList<ClientWpc>> wpcsProposed = null;

        if (started && currentTurn == 0){
            wpcsProposed = new HashMap<>();
            for (PlayerInGame player : players){
                ArrayList<ClientWpc> clientWpcs = new ArrayList<>();
                for (String wpc : wpcsByUser.get(player.getUser())) clientWpcs.add(wpcDB.getWpcByID(wpc).getClientWpc());
                wpcsProposed.put(player.getUser(), clientWpcs);
            }
        }

        return wpcsProposed;
    }


    //----------------------------------------------------------------------------------

    /**
     * Wait 3 seconds for players to connect
     */
    void waitPlayers() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public boolean isStarted() {
        return started;
    }

    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    public List<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public String getID() { return id; }

    public int getNumPlayers() { return numPlayers; }

    public List<Dice> getExtractedDices() { return extractedDices; }

    public PlayerInGame[] getPlayers() { return players; }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * @return true if the game has all players
     */
    public boolean isFull(){ return (players[players.length-1] != null); }

    int getCurrentTurn() {
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
        if (isFull()) return -1;
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

            for(String tempId : wpcIDs){
                userClientWpcs.add(wpcDB.getClientWpcByID(tempId));
            }

            clientWpcByUser.put(player.getUser(), new ArrayList<>(userClientWpcs));
        }

        changeAndNotifyObservers(new WpcsExtractedNotification(clientWpcByUser, GameConstants.CHOOSE_WPC_WAITING_TIME));
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
        HashMap<String, ArrayList<String>> tempWpcsByUser = new HashMap<>();

        int numOfWpcs = GameConstants.NUM_OF_WPC_PROPOSE_TO_EACH_PLAYER;

        for(int i = 0; i < players.length; i++){
            tempWpcsByUser.put(players[i].getUser(), new ArrayList<>(
                    ids.subList(numOfWpcs * i, numOfWpcs * (i + 1))));
        }

        return tempWpcsByUser;
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
        ArrayList<String> ids = new ArrayList<>(toolcardDB.getCardsIDs());
        Collections.shuffle(ids);

        ArrayList<String> toolCardsExtracted = new ArrayList<>(ids.subList(0, numOfToolCards));

        ArrayList<ClientToolCard> clientToolCards = new ArrayList<>();

        for (String cardId : toolCardsExtracted){
            ToolCard card = toolcardDB.getCardByID(cardId);
            toolCards.add(card);
            clientToolCards.add(card.getClientToolcard());
        }

        changeAndNotifyObservers(new ToolcardsExtractedNotification(clientToolCards));
    }

    /**
     *Chooses in a random way 3 Public Objective Cards from the 12 available in the Private Objective Cards data base
     */
    void extractPublicObjectives(){
        ArrayList<String> ids = new ArrayList<>(pocDB.getCardsIDs());
        Collections.shuffle(ids);
        ArrayList<String> publicCardsExtracted = new ArrayList<>(ids.subList(0, numOfPublicObjectiveCards));
        ArrayList<ClientPoc> clientPocs = new ArrayList<>();

        for (String cardId : publicCardsExtracted){
            PublicObjectiveCard card = pocDB.getCardByID(cardId);
            publicObjectiveCards.add(card);
            clientPocs.add(card.getClientPoc());
        }

        changeAndNotifyObservers(new PocsExtractedNotification(clientPocs));
    }


    /**
     * @return true if the player must skip the turn, false if not
     */
    boolean shouldSkipTurn() {
        PlayerInGame player = players[turnPlayer];
        return player.isDisconnected() ||
                (player.getCardUsedBlockingTurn() != null && player.getTurnToSkip() == player.getTurnForRound());
    }


    /**
     * @return the object diceBag
     */
    public DiceBag getDiceBag() { return diceBag; }


    /**
     * Removes all player in game from the database
     */
    void endGame() {
        DatabaseGames.getInstance().removeGame(this);
        for (PlayerInGame player : players) {
            DatabaseUsers.getInstance().removePlayerInGameFromDB(player);
        }
    }


    public boolean isTurnFinished() {
        return turnFinished;
    }

    public boolean isSinglePlayerGame(){
        return numPlayers == 1;
    }

    public void updateRoundTrack(RoundTrack roundTrack){
        this.roundTrack=roundTrack;
    }

    public void updateExtractedDices(List<Dice> extractedDices){
        this.extractedDices=new ArrayList<>(extractedDices);
    }


    /**
     * Chooses for each player one of the four schemas in a random way and sets it in the player in game object
     *
     * @param wpcsByUser is the HashMap which contains for the player username (keys) and the arrayLists with the schemas'
     *                   ids
     */
    void selectRandomWpc(HashMap<String, ArrayList<String>> wpcsByUser) {
        for (PlayerInGame player : players) {
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


    //--------------------------------------- Abstract Methods --------------------------------------

    abstract void initializeGame();
    abstract void nextRound();
    public abstract void endTurn(ClientEndTurnData endTurnData);
    abstract void calculateScore();
    abstract void saveScore();
    abstract void waitForWpcResponse();
    public abstract void removeToolCardIfSingleGame(ToolCard card);
    public abstract boolean addPlayer(String user) throws MaxPlayersExceededException, UserAlreadyInThisGameException, CannotCreatePlayerException;
    public abstract void disconnectPlayer(String user) throws UserNotInThisGameException;

}