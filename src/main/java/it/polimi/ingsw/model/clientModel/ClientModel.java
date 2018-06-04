package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.control.network.commands.notifications.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ClientModel implements Observer, NotificationHandler {
    private static ClientModel instance;
    private Observer observer;

    //User
    private String username;
    private String userToken;

    //Game
    private String gameID;
    private int gameActualPlayers;
    private int gameNumPlayers;
    private ClientColor[] privateObjectives;
    private HashMap<String, ClientWpc> wpcByUsername;
    private ArrayList<ClientToolCard> gameToolCards;
    private ArrayList<ClientPoc> gamePublicObjectiveCards;
    private int currentRound;
    private ArrayList<ClientDice> extractedDices;
    private int currentTurn;
    private boolean active;
    private int favour;
    private ClientRoundTrack roundTrack;

    private ClientModel() { }

    public static synchronized ClientModel getInstance() {
        if (instance == null) {
            instance = new ClientModel();
            instance.clean();
        }

        return instance;
    }

    public void exitGame(){
        this.gameID = null;
        this.gameActualPlayers = 0;
        this.gameNumPlayers = 0;
        this.privateObjectives = null;
        this.wpcByUsername = new HashMap<>();
        this.gameToolCards = new ArrayList<>();
        this.gamePublicObjectiveCards = new ArrayList<>();
        this.currentRound = 0;
        this.extractedDices = new ArrayList<>();
        this.currentTurn = 0;
        this.active = false;
        this.favour = 0;
        this.roundTrack=null;
    }

    public void clean(){
        this.username = null;
        this.userToken = null;
        exitGame();
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public String getUsername() {
        return username;
    }

    public ClientRoundTrack getRoundTrack() {
        return roundTrack;
    }

    public void setRoundTrack(ClientRoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setWpcByUsername(HashMap<String, ClientWpc> wpcByUsername) {
        this.wpcByUsername = wpcByUsername;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFavour(int favour) {
        this.favour = favour;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getGameActualPlayers() {
        return gameActualPlayers;
    }

    public void setGameActualPlayers(int gameActualPlayers) {
        this.gameActualPlayers = gameActualPlayers;
    }

    public int getGameNumPlayers() {
        return gameNumPlayers;
    }

    public void setExtractedDices(ArrayList<ClientDice> extractedDices) {
        this.extractedDices = extractedDices;
    }

    public void setGameNumPlayers(int gameNumPlayers) {
        this.gameNumPlayers = gameNumPlayers;
    }

    public ClientColor[] getPrivateObjectives() {
        return privateObjectives;
    }

    public void setPrivateObjectives(ClientColor[] privateObjectives) {
        this.privateObjectives = privateObjectives;
    }

    public ArrayList<ClientToolCard> getGameToolCards() {
        return gameToolCards;
    }

    public void setGameToolCards(ArrayList<ClientToolCard> gameToolCards) {
        this.gameToolCards = gameToolCards;
    }

    public ArrayList<ClientPoc> getGamePublicObjectiveCards() {
        return gamePublicObjectiveCards;
    }

    public void setGamePublicObjectiveCards(ArrayList<ClientPoc> gamePublicObjectiveCards) {
        this.gamePublicObjectiveCards = gamePublicObjectiveCards;
    }

    public ClientWpc getMyWpc(){
        return wpcByUsername.get(username);
    }

    public HashMap<String, ClientWpc> getWpcByUsername() {
        return wpcByUsername;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public ArrayList<ClientDice> getExtractedDices() {
        return extractedDices;
    }

    public boolean isActive() {
        return active;
    }

    public int getFavour() {
        return favour;
    }


    public boolean areAllPlayersInGame(){
        return (gameActualPlayers != 0 && gameActualPlayers == gameNumPlayers);
    }

    public boolean arePrivateObjectivesArrived(){
        return privateObjectives != null;
    }

    public boolean areAllWpcsArrived(){
        return wpcByUsername.size() == gameNumPlayers;
    }

    public boolean areToolcardsArrived(){
        return gameToolCards != null;
    }

    public boolean arePocsArrived(){
        return gamePublicObjectiveCards != null;
    }



    //----------------------------------- Notification Handler -------------------------------------
    @Override
    public void update(Observable o, Object arg) {
        ((Notification) arg).handle(this);
        observer.update(o, arg);
    }

    @Override
    public void handle(GameStartedNotification notification) { }

    @Override
    public void handle(PlayersChangedNotification notification) {
        if (notification.joined) gameActualPlayers++;
        else gameActualPlayers--;
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        privateObjectives = notification.colorsByUser.get(username);
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {

    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        wpcByUsername.put(notification.username, notification.wpc);
        if (notification.username.equals(username)) favour = notification.wpc.getFavours();
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        gameToolCards = notification.cards;
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        gamePublicObjectiveCards = notification.cards;
    }

    @Override
    public void handle(NewRoundNotification notification) {
        currentRound = notification.roundNumber;
        extractedDices = notification.extractedDices;
    }

    @Override
    public void handle(NextTurnNotification notification) {
        currentTurn = notification.turnNumber;
        active = notification.activeUser.equals(username);
    }

    @Override
    public void handle(DiceChangedNotification notification) {

    }

    @Override
    public void handle(DicePlacedNotification notification) {
        wpcByUsername.put(notification.username, notification.wpc);
        if (notification.newExtractedDices != null) extractedDices = notification.newExtractedDices;
        if (notification.newRoundTrack != null) roundTrack = notification.newRoundTrack;
    }

    @Override
    public void handle(ToolCardCanceledNotification notification) {

    }

    @Override
    public void handle(ToolCardUsedNotification notification) {

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }
}
