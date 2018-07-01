package shared.clientInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientGame implements Serializable {
    private final String id;
    private int gameActualPlayers;
    private int gameNumPlayers;
    private final HashMap<String, ArrayList<ClientWpc>> wpcsProposedByUsername;
    private HashMap<String, ClientWpc> wpcByUsername;
    private HashMap<String, Integer> favoursByUsername;
    private ArrayList<ClientToolCard> toolCards;
    private ArrayList<ClientPoc> publicObjectiveCards;
    private ArrayList<ClientDice> extractedDices;
    private int currentTurn;
    private ClientRoundTrack roundTrack;
    private boolean started;

    public ClientGame(String id, int gameNumPlayers, HashMap<String, ArrayList<ClientWpc>> wpcsProposedByUsername) {
        this.id = id;
        this.gameNumPlayers = gameNumPlayers;

        this.wpcByUsername = new HashMap<>();
        this.favoursByUsername = new HashMap<>();
        this.wpcsProposedByUsername = wpcsProposedByUsername;
    }

    public ClientGame(String id, int gameNumPlayers) {
        this(id, gameNumPlayers, null);
    }

    public String getId() {
        return id;
    }

    public void setGameNumPlayers(int gameNumPlayers) {
        this.gameNumPlayers = gameNumPlayers;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
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

    public HashMap<String, ClientWpc> getWpcByUsername() {
        return wpcByUsername;
    }

    public void setWpcByUsername(HashMap<String, ClientWpc> wpcByUsername) {
        this.wpcByUsername = wpcByUsername;
    }

    public void setUserWpc(String username, ClientWpc wpc){
        wpcByUsername.put(username, wpc);
    }

    public HashMap<String, Integer> getFavoursByUsername() {
        return favoursByUsername;
    }

    public void setUserFavours(String username, int favours) {
        favoursByUsername.put(username, favours);
    }

    public void setFavoursByUsername(HashMap<String, Integer> favoursByUsername) {
        this.favoursByUsername = favoursByUsername;
    }

    public ArrayList<ClientToolCard> getToolCards() {
        return toolCards;
    }

    public void setToolCards(ArrayList<ClientToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    public ArrayList<ClientPoc> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public void setPublicObjectiveCards(ArrayList<ClientPoc> publicObjectiveCards) {
        this.publicObjectiveCards = publicObjectiveCards;
    }

    public ArrayList<ClientDice> getExtractedDices() {
        return extractedDices;
    }

    public void setExtractedDices(ArrayList<ClientDice> extractedDices) {
        this.extractedDices = extractedDices;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public ClientRoundTrack getRoundTrack() {
        return roundTrack;
    }

    public void setRoundTrack(ClientRoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }

    public HashMap<String, ArrayList<ClientWpc>> getWpcsProposedByUsername() {
        return wpcsProposedByUsername;
    }
}
