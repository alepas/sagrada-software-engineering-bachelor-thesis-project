package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.control.network.commands.notifications.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ClientModel implements Observer, NotificationHandler {
    private static ClientModel instance;
    private ArrayList<Observer> observers;

    //User
    private String userToken;
    private ClientUser user;

    //Game
    private String gameID;
    private boolean gameStarted;
    private int gameActualPlayers;
    private int gameNumPlayers;
    private ClientColor[] privateObjectives;
    private boolean wpcsArrived;
    private HashMap<String, ClientWpc> wpcByUsername;
    private HashMap<String, Integer> favoursByUsername;
    private ArrayList<ClientToolCard> gameToolCards;
    private ArrayList<ClientPoc> gamePublicObjectiveCards;
    private int currentRound;
    private ArrayList<ClientDice> extractedDices;
    private int currentTurn;
    private boolean active;
    private ClientRoundTrack roundTrack;
    private ToolCardClientNextActionInfo toolCardClientNextActionInfo;

    private ClientModel() {
    }

    public static synchronized ClientModel getInstance() {
        if (instance == null) {
            instance = new ClientModel();
            instance.clean();
        }

        return instance;
    }

    public void exitGame() {
        this.gameID = null;
        this.gameStarted = false;
        this.gameActualPlayers = 0;
        this.gameNumPlayers = 0;
        this.privateObjectives = null;
        this.wpcsArrived = false;
        this.wpcByUsername = new HashMap<>();
        this.favoursByUsername = new HashMap<>();
        this.gameToolCards = new ArrayList<>();
        this.gamePublicObjectiveCards = new ArrayList<>();
        this.currentRound = 0;
        this.extractedDices = new ArrayList<>();
        this.currentTurn = 0;
        this.active = false;
        this.roundTrack = null;
        this.toolCardClientNextActionInfo = null;
    }

    public void clean() {
        this.userToken = null;
        this.user = null;
        this.observers = new ArrayList<>();
        exitGame();
    }

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public String getUsername() {
        if (user != null) return user.getUsername();
        return null;
    }

    public ClientRoundTrack getRoundTrack() {
        return roundTrack;
    }

    public void setRoundTrack(ClientRoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void setUsername(String username) {
        this.user = new ClientUser(username);
    }

    public ClientUser getUser() {
        return user;
    }

    public void setUser(ClientUser user) {
        this.user = user;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setWpcByUsername(HashMap<String, ClientWpc> wpcByUsername) {
        this.wpcByUsername = wpcByUsername;
    }

    public HashMap<String, Integer> getFavoursByUsername() {
        return favoursByUsername;
    }

    public void setUserFavours(String username, int favours) {
        favoursByUsername.put(username, favours);
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

    public ClientWpc getMyWpc() {
        return wpcByUsername.get(user.getUsername());
    }

    public void setMyWpc(ClientWpc wpc) {
        wpcByUsername.put(user.getUsername(), wpc);
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

    public boolean areAllPlayersInGame() {
        return (gameActualPlayers != 0 && gameActualPlayers == gameNumPlayers);
    }

    public boolean arePrivateObjectivesArrived() {
        return privateObjectives != null;
    }

    public boolean areAllWpcsArrived() {
        return wpcsArrived;
    }

    public boolean allPlayersChooseWpc() {
        return (gameNumPlayers != 0 && wpcByUsername.size() == gameNumPlayers);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean areToolcardsArrived() {
        return gameToolCards != null;
    }

    public boolean arePocsArrived() {
        return gamePublicObjectiveCards != null;
    }

    public ToolCardClientNextActionInfo getToolCardClientNextActionInfo() {
        return toolCardClientNextActionInfo;
    }

    public void setToolCardClientNextActionInfo(ToolCardClientNextActionInfo toolCardClientNextActionInfo) {
        this.toolCardClientNextActionInfo = toolCardClientNextActionInfo;
    }


    //----------------------------------- Notification Handler -------------------------------------
    @Override
    public void update(Observable o, Object arg) {
        ((Notification) arg).handle(this);
        for (Observer observer : observers) {
            observer.update(o, arg);
        }
    }

    @Override
    public void handle(GameStartedNotification notification) {
        gameStarted = true;
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
        if (notification.joined) gameActualPlayers++;
        else gameActualPlayers--;
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        privateObjectives = notification.colorsByUser.get(user.getUsername());
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        wpcsArrived = true;
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        wpcByUsername.put(notification.username, notification.wpc);
        favoursByUsername.put(notification.username, notification.wpc.getFavours());
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
        roundTrack = notification.roundTrack;
        if (notification.oldUserForcedWpc != null)
            wpcByUsername.put(notification.oldUserNameForced, notification.oldUserForcedWpc);
    }

    @Override
    public void handle(NextTurnNotification notification) {
        if (notification.endTurnData != null) {
            if (notification.endTurnData.extractedDices != null)
                extractedDices = notification.endTurnData.extractedDices;
            if (notification.endTurnData.roundTrack != null) roundTrack = notification.endTurnData.roundTrack;
            if (notification.endTurnData.wpcOldUser != null)
                wpcByUsername.put(notification.endTurnData.oldUser, notification.endTurnData.wpcOldUser);
            if (notification.endTurnData.toolCardUsed != null) {
                String id = notification.endTurnData.toolCardUsed.getId();
                for (ClientToolCard card : gameToolCards) {
                    if (card.getId().equals(id))
                        gameToolCards.set(gameToolCards.indexOf(card), notification.endTurnData.toolCardUsed);
                }
            }

        }
        currentTurn = notification.turnNumber;
        active = notification.activeUser.equals(user.getUsername());
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {
    }

    @Override
    public void handle(DicePlacedNotification notification) {
        wpcByUsername.put(notification.username, notification.wpc);
        if (notification.newExtractedDices != null) extractedDices = notification.newExtractedDices;
        if (notification.newRoundTrack != null) roundTrack = notification.newRoundTrack;
    }


    @Override
    public void handle(ToolCardUsedNotification notification) {
        String id = notification.toolCard.getId();
        if (notification.wpc != null) wpcByUsername.put(notification.username, notification.wpc);
        if (notification.newExtractedDices != null) extractedDices = notification.newExtractedDices;
        if (notification.newRoundTrack != null) roundTrack = notification.newRoundTrack;
        for (ClientToolCard card : gameToolCards) {
            if (card.getId().equals(id)) gameToolCards.set(gameToolCards.indexOf(card), notification.toolCard);
        }

        for (Notification not : notification.movesNotifications) not.handle(this);
    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }

    @Override
    public void handle(ScoreNotification notification) {

    }

    @Override
    public void handle(ToolCardDicePlacedNotification notification) {

    }

    @Override
    public void handle(ToolCardExtractedDicesModifiedNotification notification) {
    }

    @Override
    public void handle(PlayerDisconnectedNotification playerDisconnectedNotification) {

    }

    @Override
    public void handle(PlayerReconnectedNotification playerReconnectedNotification) {

    }
}
