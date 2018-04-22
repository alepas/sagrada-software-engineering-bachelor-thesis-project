package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.RoundTrack;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.usersdb.User;

import java.util.ArrayList;
import java.util.UUID;

public abstract class AbstractGame {
    private ArrayList<ToolCard> toolCards;
    private ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private String gameID;
    private int numPlayers;
    private RoundTrack roundTrack;
    private ArrayList<Dice> extractedDices;
    private ArrayList<PlayerInGame> players;

    AbstractGame(User user, int numPlayers) {
        toolCards = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        gameID = UUID.randomUUID().toString();
        this.numPlayers = numPlayers;
        roundTrack = new RoundTrack();
        extractedDices = new ArrayList<>();
        players = new ArrayList<>();
        PlayerInGame player = new PlayerInGame(user);
        players.add(player);
    }

    abstract void startGame();
    abstract void endGame();
    abstract void nextRound();
    abstract void nextTurn();
    abstract void extractWPC();
    abstract void calculateScore();
    abstract void saveScore();

    public ArrayList<ToolCard> getToolCards() {
        return toolCards;
    }

    public ArrayList<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    public String getGameID() {
        return gameID;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<Dice> getExtractedDices() {
        return extractedDices;
    }

    public ArrayList<PlayerInGame> getPlayers() {
        return players;
    }

    public void removeExtractedDice(Dice dice){
        extractedDices.remove(dice);
    }

}
