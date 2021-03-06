package server.model.game;

import server.constants.GameConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.thread.ChooseWpcThread;
import server.model.game.thread.WaitForEndTurnThread;
import server.model.game.thread.WaitPlayersThread;
import server.model.game.thread.WaiterThread;
import server.model.users.DatabaseUsers;
import server.model.users.PlayerInGame;
import server.model.wpc.Wpc;
import shared.clientinfo.ClientDice;
import shared.clientinfo.ClientEndTurnData;
import shared.clientinfo.ClientWpc;
import shared.exceptions.gameexceptions.InvalidMultiPlayerGamePlayersException;
import shared.exceptions.gameexceptions.MaxPlayersExceededException;
import shared.exceptions.gameexceptions.UserAlreadyInThisGameException;
import shared.exceptions.gameexceptions.UserNotInThisGameException;
import shared.exceptions.usersAndDatabaseExceptions.CannotUpdateStatsForUserException;
import shared.network.commands.notifications.*;

import java.util.*;

import static server.constants.GameConstants.*;

public class MultiplayerGame extends Game {
    int roundPlayer;
    private int turnForRound;
    private HashMap<String, Integer> scoreList = new HashMap<>();
    private ClientEndTurnData endTurnData;
    private WaiterThread currentThread;
    private Thread waitPlayersThread;
    private boolean endGameForced = false;

    /**
     * Creates a multiplayerGame
     *
     * @param numPlayers is the number of players what will take part to the game
     * @throws InvalidMultiPlayerGamePlayersException when the num of player isn't in the range
     */
    public MultiplayerGame(int numPlayers) throws InvalidMultiPlayerGamePlayersException {
        super(numPlayers);

        if (numPlayers < GameConstants.MULTIPLAYER_MIN_NUM_PLAYERS || numPlayers > GameConstants.MAX_NUM_PLAYERS)
            throw new InvalidMultiPlayerGamePlayersException(numPlayers, GameConstants.MULTIPLAYER_MIN_NUM_PLAYERS, GameConstants.MAX_NUM_PLAYERS);

        numOfPrivateObjectivesForPlayer = GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME;
        numOfToolCards = GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME;
        numOfPublicObjectiveCards = GameConstants.NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME;
    }


    public int getCurrentTaskTimeLeft() {
        return currentThread.getTimeLeft();
    }

    /**
     * if possible creates a new Player in game and adds it to the array of players
     *
     * @param user is the username of the player that would like to enter in the game
     * @return true if, after adding the new player, the game is full, false if there's still space
     * @throws MaxPlayersExceededException    the game is full and it is not possible to add a new player
     * @throws UserAlreadyInThisGameException the player was already inside the game
     */
    @Override
    public synchronized boolean addPlayer(String user) throws MaxPlayersExceededException, UserAlreadyInThisGameException {

        if (this.isFull()) throw new MaxPlayersExceededException(user, this);

        if (playerIndex(user) >= 0) throw new UserAlreadyInThisGameException(user, this);

        PlayerInGame player = new PlayerInGame(user, this);

        players[nextFree()] = player;

        changeAndNotifyObservers(new PlayersChangedNotification(user, true, numActualPlayers(), numPlayers));
        started = this.isFull();
        return started;
    }

    /**
     * removes a player from the players' array
     *
     * @param user is the String related to the player in game that will be removed
     * @throws UserNotInThisGameException if the player is not in this game
     */

    public synchronized void removePlayer(String user) throws UserNotInThisGameException {
            int index = playerIndex(user);
            if (index < 0) throw new UserNotInThisGameException(user, this);

            PlayerInGame player = players[index];
            DatabaseUsers.getInstance().removePlayerInGameFromDB(player);
            removeArrayIndex(players, index);

            changeAndNotifyObservers(new PlayersChangedNotification(user, false, numActualPlayers(), numPlayers));
    }

    @Override
    public synchronized void disconnectPlayer(String username) throws UserNotInThisGameException {
        if (!started) {
            removePlayer(username);
            if (numActualPlayers() == 1) ((WaitPlayersThread) currentThread).setPause(true);
        } else {
            if (currentTurn != 0) {
                int numConnected = 0;
                for (PlayerInGame player : players) if (!player.isDisconnected()) numConnected++;
                if (numConnected == 1) forceEndGame();
            }
        }
    }

    @Override
    public void run() {
        try {
            waitPlayersThread.join();
            if (!started) forceStarGame();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        waitPlayers();      //Aspetta 3 secondi che i giocatori si connettano tutti
                            /* Quando l'ultimo giocatore si connette il thread della partita viene avviato immediatamente,
                               ma l'ultimo giocatore, di fatto, non è ancora in partita: lo è solo il suo playerInGame lato
                               server. Occorre aspettare che l'ultimo utente riceva la partita in cui è entrato e che si metta
                               in ascolto di eventuali cambiamenti. Ecco il perchè di questa attesa*/
        changeAndNotifyObservers(new GameStartedNotification());

        initializeGame();
        allWpcsChosen = true;

        while (roundTrack.getCurrentRound() < GameConstants.NUM_OF_ROUNDS) {
            nextRound();
        }

        calculateScore();
        saveScore();
        endGame();
    }

    private void forceStarGame() {
        for (PlayerInGame player : players) {
            if (player != null && player.isDisconnected()) {
                try {
                    removePlayer(player.getUser());
                } catch (UserNotInThisGameException e) { /*Go home game you're drunk*/}
            }
        }

        PlayerInGame[] newPlayers = new PlayerInGame[numActualPlayers()];
        System.arraycopy(players, 0, newPlayers, 0, newPlayers.length);
        players = newPlayers;

        numPlayers = players.length;
        changeAndNotifyObservers(new ForceStartGameNotification(this.getClientGame()));
        started = true;
    }

    //Restituisce true se il thread è stato creato
    public boolean restartWaitPlayersTimer() {
        if (currentThread == null) {
            currentThread = new WaitPlayersThread(GameConstants.TIME_WAITING_PLAYERS_TO_ENTER_GAME, this);
            waitPlayersThread = new Thread(currentThread);
            waitPlayersThread.start();
            return true;
        }
        ((WaitPlayersThread) currentThread).setTimeLeft(GameConstants.TIME_WAITING_PLAYERS_TO_ENTER_GAME);
        return false;
    }

    /**
     * Starts the multi-player game, it calls all the extraction methods and sets some parameters to their initial values.
     * Overrides the method of the abstract class game.
     */
    @Override
    public void initializeGame() {
        turnPlayer = 0;
        roundPlayer = players.length - 1;
        currentTurn = 0;

        extractPrivateObjectives();
        extractWPCs();
        extractToolCards();
        extractPublicObjectives();
        shufflePlayers();
    }

    /**
     * shuffle the players array, the new disposition in the array will be the turn sequence.
     */
    private void shufflePlayers() {
        ArrayList<PlayerInGame> playersList = new ArrayList<>(Arrays.asList(players));
        Collections.shuffle(playersList);
        players = playersList.toArray(players);
    }

    /**
     * Waits that all players choose a wpc; if it doesn't happen in a minutes from the call of this method the schemas
     * will be chosen by the server between the 4 given to each player by calling the next method.
     */
    @Override
    public void waitForWpcResponse() {
        currentThread = new ChooseWpcThread(players, GameConstants.CHOOSE_WPC_WAITING_TIME + GameConstants.TASK_DELAY);
        Thread waitForWpcs = new Thread(currentThread);

        try {
            System.out.println("Sto aspettando che i giocatori scelgano le wpc");
            waitForWpcs.start();
            waitForWpcs.join((long) CHOOSE_WPC_WAITING_TIME + TASK_DELAY);
            System.out.println("Ho smesso di aspettare che i giocatori scelgano le wpc");
            if (waitForWpcs.isAlive()) {
                waitForWpcs.interrupt();
                selectRandomWpc(wpcsByUser);
                System.out.println("Ho estratto casualmente le wpc dei giocatori rimanenti");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void removeToolCardIfSingleGame(ToolCard card) { }

    /**
     * when all players have done their turns this method is called. It puts all left dices in the round Track,
     * extracts the new dices from the diceBag and sets the first round player. It also sends a NextRound Notification
     * containing all new information.
     */
    @Override
    public void nextRound() {
        ClientWpc oldClientWpc = null;
        String oldUser = null;
        if (endTurnData != null) {
            oldClientWpc = endTurnData.wpcOldUser;
            oldUser = endTurnData.oldUser;
        }

        for (Dice dice : extractedDices) roundTrack.addDice(dice);
        roundTrack.nextRound();

        for (PlayerInGame player : players)
            player.clearPlayerRound();

        extractedDices = diceBag.extractDices(numPlayers);
        ArrayList<ClientDice> extractedClientDices = new ArrayList<>();
        for (Dice dice : extractedDices) extractedClientDices.add(dice.getClientDice());
        changeAndNotifyObservers(new NewRoundNotification(roundTrack.getCurrentRound(), extractedClientDices,
                roundTrack.getClientRoundTrack(), oldClientWpc, oldUser));

        currentTurn = 0;
        if (roundPlayer < players.length - 1) roundPlayer++;
        else roundPlayer = 0;
        turnPlayer = roundPlayer;

        while (currentTurn < NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME * numPlayers) {
            nextTurn();
        }
    }


    /**
     * Sets the player which is ending his/her turn to !active()
     *
     * @param endTurnData are related to what happened in the last turn
     */
    @Override
    public void endTurn(ClientEndTurnData endTurnData) {
        players[turnPlayer].setNotActive();
        turnFinished = true;
        this.endTurnData = endTurnData;
    }


    /**
     * calls the nextPlayer() method, the player that will have to play the new turn is set to active. Before activate
     * the players it is necessary to check if he/she should skip the turn: if yes the method nextPlayer() will be called
     * again and a playerSkipNotification will be thrown.
     */
    private void nextTurn() {
        turnPlayer = nextPlayer();
        currentTurn++;

        while (shouldSkipTurn()) {
            PlayerInGame player = players[turnPlayer];
            String toolcardUsedID = (player.getCardUsedBlockingTurn() != null) ? player.getCardUsedBlockingTurn().getID() : null;
            changeAndNotifyObservers(new PlayerSkipTurnNotification(player.getUser(), toolcardUsedID, player.isDisconnected()));

            if (currentTurn < NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME * numPlayers) {
                turnPlayer = nextPlayer();
                currentTurn++;

            } else return;
        }

        players[turnPlayer].setActive();
        changeAndNotifyObservers(new NextTurnNotification(currentTurn, players[turnPlayer].getUser(), GameConstants.TIME_TO_PLAY_TURN_MULTIPLAYER, endTurnData));
        startTurnTimer();
    }

    /**
     * At the biginning of a new turn the timer starts. if the player doesn't end the turn before the time ends  it will
     * be force to end.
     */
    private void startTurnTimer() {
        turnFinished = false;

        currentThread = new WaitForEndTurnThread(this, GameConstants.TIME_TO_PLAY_TURN_MULTIPLAYER + GameConstants.TASK_DELAY);
        Thread waitForEndTurn = new Thread(currentThread);

        try {
            waitForEndTurn.start();
            waitForEndTurn.join((long) TIME_TO_PLAY_TURN_MULTIPLAYER + TASK_DELAY);
            if (waitForEndTurn.isAlive()) {

                waitForEndTurn.interrupt();
                players[turnPlayer].forceEndTurn();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * @return the player turn
     */
    int nextPlayer() {
        if (currentTurn == 0) {
            turnForRound = 1;
            players[turnPlayer].setTurnInRound(turnForRound);
            return turnPlayer;
        } else if (currentTurn  % numPlayers == 0) {
            turnForRound++;
            players[turnPlayer].setTurnInRound(turnForRound);
            return turnPlayer;
        } else if (turnForRound % 2 != 0) {
            int playerTemp;
            if (turnPlayer == numPlayers - 1)
                playerTemp = 0;
            else playerTemp = turnPlayer + 1;
            players[playerTemp].setTurnInRound(turnForRound);
            return playerTemp;
        } else {
            int playerTemp;
            if (turnPlayer == 0)
                playerTemp = numPlayers - 1;
            else playerTemp = turnPlayer - 1;
            players[playerTemp].setTurnInRound(turnForRound);
            return playerTemp;
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
        if (!endGameForced) {
            for (PlayerInGame player : players) {
                int score;
                if (!player.isDisconnected()) {
                    Wpc wpc = player.getWPC();
                    score = privateObjScore(player) - wpc.getNumFreeCells() + wpc.getFavours();

                    for (PublicObjectiveCard poc : publicObjectiveCards)
                        score = score + poc.calculateScore(wpc);
                } else score = GameConstants.DEFAULT_SCORE_MULTIPLAYER_GAME_LEFT;
                scoreList.put(player.getUser(), score);
            }
        }
        changeAndNotifyObservers(new ScoreNotification(scoreList));
    }


    /**
     * Counts the score made by the player with the private objective, this score is the sum of all numbers of all
     * dices with color equals to the private objective.
     *
     * @param player is the player of which the game is calculating the score
     * @return the score made with the private objective
     */
    private int privateObjScore(PlayerInGame player) {
        Color[] playerColors = player.getPrivateObjs();
        ArrayList<Dice> dices = player.getWPC().getWpcDices();

        int score = 0;

        for (Dice dice : dices) {
            for (Color color : playerColors) {
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
        for (PlayerInGame player : players) {
            try {
                if (player.isDisconnected()){
                    player.addAbandonedGame();
                } else {
                    if (player.getUser().equals(scores.get(0).getKey())) {
                        player.addPointsToRanking(numPlayers);
                        player.addWonGame();
                    } else {
                        player.addLostGame();
                    }
                }
                player.addPointsToRanking(scoreList.get(player.getUser()));
            } catch (CannotUpdateStatsForUserException e) {
                e.printStackTrace();
            }
        }
    }


    private void forceEndGame() {
        endGameForced = true;
        for (PlayerInGame player : players){
            if (player.isDisconnected()) scoreList.put(player.getUser(), DEFAULT_SCORE_MULTIPLAYER_GAME_LEFT);
            else scoreList.put(player.getUser(), DEFAULT_SCORE_MULTIPLAYER_GAME);
        }
        while (roundTrack.getCurrentRound() < GameConstants.NUM_OF_ROUNDS) roundTrack.nextRound();
        currentTurn = NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME * numPlayers;
        endTurn(null);
    }

    //------------------------------- Metodi validi solo lato client -------------------------------

    void setTurnPlayer(int turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }


}
