package it.polimi.ingsw.model.game;

import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.gameExceptions.*;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotAddPlayerInDatabaseException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotUpdateStatsForUserException;
import it.polimi.ingsw.model.gamesdb.DatabaseGames;
import it.polimi.ingsw.model.usersdb.DatabaseUsers;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.wpc.Wpc;
import javafx.application.Platform;

import java.util.*;

import static it.polimi.ingsw.model.constants.GameConstants.NUM_OF_ROUNDS;

public class MultiplayerGame extends Game {
    private int turnPlayer;
    private int roundPlayer;
    private HashMap<String, Integer> scoreList = new HashMap<>();

    public MultiplayerGame(int numPlayers) throws InvalidMultiplayerGamePlayersException {
        super(numPlayers);

        if (numPlayers < GameConstants.MULTIPLAYER_MIN_NUM_PLAYERS || numPlayers > GameConstants.MAX_NUM_PLAYERS)
            throw new InvalidMultiplayerGamePlayersException(numPlayers);

        numOfPrivateObjectivesForPlayer = GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME;
        numOfToolCards = GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME;
        numOfPublicObjectiveCards = GameConstants.NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME;
    }




    //----------------------------- Metodi validi per entrambi i lati -----------------------------

    public int getTurnPlayer() { return turnPlayer; }

    public int getRoundPlayer() { return roundPlayer; }


    public synchronized boolean addPlayer(String user) throws MaxPlayersExceededException, UserAlreadyInThisGameException, CannotCreatePlayerException {
        //Return true if, after adding the player, the game is complete
        if (this.isFull()) throw new MaxPlayersExceededException(user, this);

        if (playerIndex(user) >= 0) throw new UserAlreadyInThisGameException(user, this);

        PlayerInGame player = null;
        try {
            player = new PlayerInGame(user, this);
        } catch (CannotAddPlayerInDatabaseException e) {
            throw new CannotCreatePlayerException(user);
        }
        players[nextFree()] = player;

        changeAndNotifyObservers(new PlayersChangedNotification(user, true, numActualPlayers(), numPlayers));

        return this.isFull();
    }

    public synchronized void removePlayer(String user) throws UserNotInThisGameException {
        int index = playerIndex(user);
        if (index < 0) throw new UserNotInThisGameException(user, this);
        removeArrayIndex(players, index);

        changeAndNotifyObservers(new PlayersChangedNotification(user, false, numActualPlayers(), numPlayers));
    }




    //------------------------------- Metodi validi solo lato server ------------------------------

    @Override
    public void run() {
        //Codice che regola il funzionamento della partita
        waitPlayers(3000);//Aspetta 3 secondi che i giocatori si connettano tutti
            /* Quando l'ultimo giocatore si connette il thread della partita viene avviato immediatamente,
               ma l'ultimo giocatore, di fatto, non è ancora in partita: lo è solo il suo playerInGame lato
               server. Occorre aspettare che l'ultimo utente riceva la partita in cui è entrato e che si metta
               in ascolto di eventuali cambiamenti. Ecco il perchè di questa attesa*/
        changeAndNotifyObservers(new GameStartedNotification());

        System.out.println("La partità è iniziata");
        initializeGame();
        playGame();
    }

    private void playGame() {
        nextRound();
    }

    private void waitPlayers(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e){
            //TODO: La partita è stata sospesa forzatamente
        }
    }

    @Override
    public void initializeGame() {
        extractPrivateObjectives();
        extractWPCs();
        extractToolCards();
        extractPublicObjectives();
        shufflePlayers();

        turnPlayer = 0;
        roundPlayer = 0;
        currentTurn = 1;
    }

    private void shufflePlayers(){
        ArrayList<PlayerInGame> playersList = new ArrayList<>(Arrays.asList(players));
        Collections.shuffle(playersList);
        players = (PlayerInGame[]) playersList.toArray(players);
    }


    @Override
    public void endGame() {
        DatabaseGames.getInstance().removeGame(this);
        for (PlayerInGame player : players) {
            try {
                DatabaseUsers.getInstance().removePlayerInGameFromDB(player);
            } catch (CannotFindPlayerInDatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void nextRound() {
        for (Dice dice : extractedDices) roundTrack.addDice(dice);

        roundTrack.nextRound();
        if(roundTrack.getCurrentRound()<= NUM_OF_ROUNDS) {
            extractedDices = diceBag.extractDices(numPlayers);
            ArrayList<ClientDice> extractedClientDices = new ArrayList<>();

            for (Dice dice : extractedDices) extractedClientDices.add(dice.getClientDice());

            changeAndNotifyObservers(new NewRoundNotification(roundTrack.getCurrentRound(), extractedClientDices));

            currentTurn = 0;

            if (roundPlayer < players.length - 1) roundPlayer++;
            else roundPlayer = 0;
            turnPlayer = roundPlayer;

            nextTurn();
        }
        else calculateScore();
    }

    @Override
    public void nextTurn() {
        players[turnPlayer].setNotActive();
        if (currentTurn < GameConstants.NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME*numPlayers) {
            turnPlayer = nextPlayer();
            players[turnPlayer].setActive();
            currentTurn++;
            changeAndNotifyObservers(new NextTurnNotification(currentTurn, players[turnPlayer].getUser()));
        } else {
            nextRound();
        }
    }

    int nextPlayer(){
        if (currentTurn % numPlayers == 0) {
            return turnPlayer;
        } else if ((currentTurn / numPlayers) % 2 == 0){
            if (turnPlayer != numPlayers-1){
                return turnPlayer+1;
            } else {
                return 0;
            }
        } else {
            if (turnPlayer != 0){
                return turnPlayer-1;
            } else {
                return numPlayers-1;
            }
        }
    }

    /**
     * Calculates the score of each player at the end of the game; the score is composed by the number of favors plus
     * the sum of the dices' number which have the same color of the private Objective plus the sum of all points given
     * by the respect of the Public Objective Card minus the number of cells without dice plaed on them.
     */
    //Da testare
    @Override
    public void calculateScore() {
        for(PlayerInGame player: players){
            Wpc wpc = player.getWPC();
            System.out.println("favor: "+ wpc.getFavours());
            System.out.println("freeCell" + wpc.getNumFreeCells());
            int score = privateObjScore(player)  - wpc.getNumFreeCells() + wpc.getFavours();

            for(PublicObjectiveCard poc: publicObjectiveCards) {
                System.out.println(poc.getID() + " score: " + poc.calculateScore(wpc));
                score = score + poc.calculateScore(wpc);
            }
            scoreList.put(player.getUser(), score);
        }
        changeAndNotifyObservers(new ScoreNotification(scoreList));
        saveScore();
    }


    /**
     * Counts the score made by the player with the private objective, this score is the sum of all numbers of all
     * dices with color equals to the private objective.
     *
     * @param player is the player of which the game is calculating the score
     * @return the score made with the private objective
     */
    private int privateObjScore(PlayerInGame player){
        Color[] playerColors = player.getPrivateObjs();
        ArrayList<Dice> dices = player.getWPC().getWpcDices();

        int score = 0;

        for(Dice dice : dices){
            for (Color color : playerColors){
                if (dice.getDiceColor() == color) score += dice.getDiceNumber();
            }
        }
        return score;
    }

    /**
     * Sets points to each player: if the player has won the game will have the amount of points that has done in the
     * game plus as many points as many players played with him (that's way because the presence of more players increase
     * the game's difficulty). Add one to the wongame to who won and add one to the lostgame to who lost.
     */
    //Da testare
    @Override
    public void saveScore() {
        ArrayList<Map.Entry<String, Integer>> scores = new ArrayList<>(scoreList.entrySet());
        scores.sort((Comparator<Map.Entry<?, Integer>>) (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for(PlayerInGame player: players){
            try {
                if(player.getUser().equals(scores.get(0).getKey())) {
                    player.addPointsToRanking(scoreList.get(player.getUser()) + numPlayers);
                    player.addWonGame();
                }
                else {
                    player.addPointsToRanking(scoreList.get(player.getUser()));
                    player.addLostGame();
                }
            } catch (CannotUpdateStatsForUserException e) {
                e.printStackTrace();
            }
        }
        endGame();
    }


    @Override
    public boolean isSinglePlayerGame() {
        return false;
    }


    //------------------------------- Metodi validi solo lato client -------------------------------

    public void setTurnPlayer(int turnPlayer){
        this.turnPlayer = turnPlayer;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }


}
