package it.polimi.ingsw.model.clientModel;

import it.polimi.ingsw.control.network.commands.notifications.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ClientModel implements Observer, NotificationHandler {
    private static ClientModel instance;
    private ArrayList<Observer> observers = new ArrayList<>();

    //User
    private String userToken;
    private ClientUser user;

    //Game
    private ClientColor[] myPrivateObjectives;
    private boolean wpcsArrived;
    private boolean gameStarted;
    private boolean active;
    private ToolCardClientNextActionInfo toolCardClientNextActionInfo;
    private ClientGame game;

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
        this.game = null;
        this.myPrivateObjectives = null;
        this.wpcsArrived = false;
        this.gameStarted = false;
        this.active = false;
        this.toolCardClientNextActionInfo = null;
    }

    public void clean() {
        this.userToken = null;
        this.user = null;
        exitGame();
    }

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public String getUsername() {
        return (user != null) ? user.getUsername() : null;
    }

    public ClientRoundTrack getRoundTrack() {
        return game.getRoundTrack();
    }

    public void setRoundTrack(ClientRoundTrack roundTrack) {
        this.game.setRoundTrack(roundTrack);
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
        this.game.setWpcByUsername(wpcByUsername);
    }

    public HashMap<String, Integer> getFavoursByUsername() {
        return game.getFavoursByUsername();
    }

    public void setUserFavours(String username, int favours) {
        game.setUserFavours(username, favours);
    }

    public void setCurrentTurn(int currentTurn) {
        this.game.setCurrentTurn(currentTurn);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getGameID() {
        return game.getId();
    }

    public int getGameActualPlayers() {
        return game.getGameActualPlayers();
    }

    public void setGameActualPlayers(int gameActualPlayers) {
        this.game.setGameActualPlayers(gameActualPlayers);
    }

    public int getGameNumPlayers() {
        return game.getGameNumPlayers();
    }

    public void setExtractedDices(ArrayList<ClientDice> extractedDices) {
        this.game.setExtractedDices(extractedDices);
    }

    public ClientColor[] getPrivateObjectives() {
        return myPrivateObjectives;
    }

    public void setPrivateObjectives(ClientColor[] privateObjectives) {
        this.myPrivateObjectives = privateObjectives;
    }

    public ArrayList<ClientToolCard> getGameToolCards() {
        return game.getToolCards();
    }

    public void setGameToolCards(ArrayList<ClientToolCard> gameToolCards) {
        this.game.setToolCards(gameToolCards);
    }

    public ArrayList<ClientPoc> getGamePublicObjectiveCards() {
        return game.getPublicObjectiveCards();
    }

    public void setGamePublicObjectiveCards(ArrayList<ClientPoc> pocs) {
        this.game.setPublicObjectiveCards(pocs);
    }

    public ClientWpc getMyWpc() {
        return game.getWpcByUsername().get(user.getUsername());
    }

    public void setMyWpc(ClientWpc wpc) {
        game.setUserWpc(user.getUsername(), wpc);
    }

    public HashMap<String, ClientWpc> getWpcByUsername() {
        return game.getWpcByUsername();
    }

    public int getCurrentRound() {
        return game.getRoundTrack().getCurrentRound();
    }

    public int getCurrentTurn() {
        return game.getCurrentTurn();
    }

    public ArrayList<ClientDice> getExtractedDices() {
        return game.getExtractedDices();
    }

    public boolean isActive() {
        return active;
    }

    public boolean areAllPlayersInGame() {
        return (game.getGameActualPlayers() != 0 && game.getGameActualPlayers() == game.getGameNumPlayers());
    }

    public boolean arePrivateObjectivesArrived() {
        return myPrivateObjectives != null;
    }

    public boolean areAllWpcsArrived() {
        return wpcsArrived;
    }

    public boolean allPlayersChooseWpc() {
        return (game.getGameNumPlayers() != 0 && game.getWpcByUsername().size() == game.getGameNumPlayers());
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean areToolcardsArrived() {
        return game.getToolCards() != null;
    }

    public boolean arePocsArrived() {
        return game.getPublicObjectiveCards() != null;
    }

    public ToolCardClientNextActionInfo getToolCardClientNextActionInfo() {
        return toolCardClientNextActionInfo;
    }

    public void setToolCardClientNextActionInfo(ToolCardClientNextActionInfo toolCardClientNextActionInfo) {
        this.toolCardClientNextActionInfo = toolCardClientNextActionInfo;
    }

    public ClientGame getGame() {
        return game;
    }

    public void setGame(ClientGame game) {
        this.game = game;
    }

    public void changeToolcard(String id, ClientToolCard newToolCard){
        ArrayList<ClientToolCard> toolCards = game.getToolCards();

        for (ClientToolCard card : toolCards) {
            if (card.getId().equals(id))
                toolCards.set(toolCards.indexOf(card), newToolCard);
        }

        game.setToolCards(toolCards);
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
        int actualPlayers = game.getGameActualPlayers();
        if (notification.joined) game.setGameActualPlayers(actualPlayers+1);
        else game.setGameActualPlayers(actualPlayers-1);
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        myPrivateObjectives = notification.colorsByUser.get(user.getUsername());
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        wpcsArrived = true;
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        game.setUserWpc(notification.username, notification.wpc);
        game.setUserFavours(notification.username, notification.wpc.getFavours());
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        game.setToolCards(notification.cards);
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        game.setPublicObjectiveCards(notification.cards);
    }

    @Override
    public void handle(NewRoundNotification notification) {
        game.setExtractedDices(notification.extractedDices);
        game.setRoundTrack(notification.roundTrack);

        if (notification.oldUserForcedWpc != null)
            game.setUserWpc(notification.oldUserNameForced, notification.oldUserForcedWpc);
    }

    @Override
    public void handle(NextTurnNotification notification) {
        if (notification.endTurnData != null) {
            if (notification.endTurnData.extractedDices != null)
                game.setExtractedDices(notification.endTurnData.extractedDices);
            if (notification.endTurnData.roundTrack != null) game.setRoundTrack(notification.endTurnData.roundTrack);
            if (notification.endTurnData.wpcOldUser != null)
                game.setUserWpc(notification.endTurnData.oldUser, notification.endTurnData.wpcOldUser);
            if (notification.endTurnData.toolCardUsed != null) {
                changeToolcard(notification.endTurnData.toolCardUsed.getId(), notification.endTurnData.toolCardUsed);
            }

        }
        game.setCurrentTurn(notification.turnNumber);
        active = notification.activeUser.equals(user.getUsername());
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {
    }

    @Override
    public void handle(DicePlacedNotification notification) {
        game.setUserWpc(notification.username, notification.wpc);
        if (notification.newExtractedDices != null) game.setExtractedDices(notification.newExtractedDices);
        if (notification.newRoundTrack != null) game.setRoundTrack(notification.newRoundTrack);
    }


    @Override
    public void handle(ToolCardUsedNotification notification) {
        if (notification.wpc != null) game.setUserWpc(notification.username, notification.wpc);
        if (notification.newExtractedDices != null) game.setExtractedDices(notification.newExtractedDices);
        if (notification.newRoundTrack != null) game.setRoundTrack(notification.newRoundTrack);
        changeToolcard(notification.toolCard.getId(), notification.toolCard);

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

    @Override
    public void handle(ForceDisconnectionNotification notification) {
        clean();
    }
}
