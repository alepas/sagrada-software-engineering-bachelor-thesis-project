package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Dice;
import it.polimi.ingsw.model.RoundTrack;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.usersdb.User;

import java.util.ArrayList;
import java.util.UUID;

public abstract class AbstractGame {
    protected ArrayList<ToolCard> toolCards;
    protected ArrayList<ObjectiveCard> objectiveCards;
    protected String gameID;
    protected int numPlayers;
    protected RoundTrack roundTrack;
    protected ArrayList<Dice> extractedDices;  // annotazione ale: un set non può avere elementi uguali ma è possibile che escano due o più dadi uguali
    protected ArrayList<PlayerInGame> users;

    protected AbstractGame(User user, int numPlayers) {
        toolCards = new ArrayList<>();
        objectiveCards = new ArrayList<>();
        gameID = UUID.randomUUID().toString();
        this.numPlayers = numPlayers;
        roundTrack = new RoundTrack();
        extractedDices = new ArrayList<>();
        users = new ArrayList<>();
        PlayerInGame player = new PlayerInGame(user);
        users.add(player);
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

    public ArrayList<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
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

    public ArrayList<PlayerInGame> getUsers() {
        return users;
    }

    public void removeExtractedDice(Dice dice){
        extractedDices.remove(dice);
    }

}
