package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.cards.PocDB;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.cards.ToolCardDB;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.WPC;
import it.polimi.ingsw.model.wpc.WpcDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ClientModel implements Observer, NotificationHandler {
    private static ClientModel instance;
    private Observer observer;
    private WpcDB wpcDB = WpcDB.getInstance();

    //User
    private String username;
    private String userToken;

    //Game
    private String gameID;
    private int gameActualPlayers;
    private int gameNumPlayers;
    private Color[] privateObjectives;
    private HashMap<String, String> wpcByUsername;
    private ArrayList<ToolCard> gameToolCards;
    private ArrayList<PublicObjectiveCard> gamePublicObjectiveCards;
    private int currentRound;
    private ArrayList<Dice> extractedDices;
    private int currentTurn;

    private ClientModel() { }

    public static synchronized ClientModel getInstance() {
        if (instance == null) {
            instance = new ClientModel();
            instance.clean();
        }

        return instance;
    }

    public void clean(){
        this.username = null;
        this.userToken = null;
        this.gameID = null;
        gameActualPlayers = 0;
        gameNumPlayers = 0;
        privateObjectives = null;
        this.wpcByUsername = new HashMap<>();
        this.gameToolCards = new ArrayList<>();
        this.gamePublicObjectiveCards = new ArrayList<>();
        this.extractedDices = new ArrayList<>();
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserToken() {
        return userToken;
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

    public void setGameNumPlayers(int gameNumPlayers) {
        this.gameNumPlayers = gameNumPlayers;
    }

    public Color[] getPrivateObjectives() {
        return privateObjectives;
    }

    public void setPrivateObjectives(Color[] privateObjectives) {
        this.privateObjectives = privateObjectives;
    }

    public WPC getWpcByID(String id){
        return WpcDB.getWpcByID(id);
    }

    public ArrayList<ToolCard> getGameToolCards() {
        return gameToolCards;
    }

    public void setGameToolCards(ArrayList<ToolCard> gameToolCards) {
        this.gameToolCards = gameToolCards;
    }

    public ArrayList<PublicObjectiveCard> getGamePublicObjectiveCards() {
        return gamePublicObjectiveCards;
    }

    public void setGamePublicObjectiveCards(ArrayList<PublicObjectiveCard> gamePublicObjectiveCards) {
        this.gamePublicObjectiveCards = gamePublicObjectiveCards;
    }

    public HashMap<String, String> getWpcByUsername() {
        return wpcByUsername;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public ArrayList<Dice> getExtractedDices() {
        return extractedDices;
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
    public void handle(PlayersChangedNotification notification) { }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        notification.username = username;
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        notification.username = username;
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        wpcByUsername.put(notification.username, notification.wpcID);
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        for (String id : notification.ids){
            gameToolCards.add(ToolCardDB.getInstance().getCardByID(id));
        }
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        for (String id : notification.ids){
            gamePublicObjectiveCards.add(PocDB.getInstance().getCardByID(id));
        }
    }

    @Override
    public void handle(NewRoundNotification notification) {
        currentRound = notification.roundNumber;
        extractedDices = notification.extractedDices;
    }

    @Override
    public void handle(NextTurnNotification notification) {
        currentTurn = notification.turnNumber;
    }
}
