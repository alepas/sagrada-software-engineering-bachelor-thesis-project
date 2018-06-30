package server.model.game;

import server.constants.GameConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.thread.ChooseWpcThread;
import server.model.game.thread.WaitForEndTurnThread;
import server.model.game.thread.WaiterThread;
import server.model.gamesdb.DatabaseGames;
import server.model.users.DatabaseUsers;
import server.model.users.PlayerInGame;
import server.model.wpc.Wpc;
import shared.clientInfo.ClientDice;
import shared.clientInfo.ClientEndTurnData;
import shared.clientInfo.ClientWpc;
import shared.exceptions.gameExceptions.*;
import shared.exceptions.usersAndDatabaseExceptions.CannotAddPlayerInDatabaseException;
import shared.exceptions.usersAndDatabaseExceptions.CannotFindPlayerInDatabaseException;
import shared.exceptions.usersAndDatabaseExceptions.CannotUpdateStatsForUserException;
import shared.network.commands.notifications.*;

import java.util.*;

public class MultiplayerGame extends Game {
    private int turnPlayer;
    private int roundPlayer;
    private HashMap<String, Integer> scoreList = new HashMap<>();
    private ClientEndTurnData endTurnData;
    private WaiterThread currentThread;

    /**
     * Creates a multiplayerGame
     * @param numPlayers is the number of players what will take part to the game
     * @throws InvalidMultiplayerGamePlayersException  when the num of player isn't in the range
     */
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

    public int getCurrentTaskTimeLeft(){ return currentThread.getTimeLeft(); }

    /**
     * if possible creates a new Player in game and adds it to the array of players
     *
     * @param user is the username of the player that would like to enter in the game
     * @return true if, after adding the new player, the game is full, false if there's still space
     * @throws MaxPlayersExceededException the game is full and it is not possible to add a new player
     * @throws UserAlreadyInThisGameException the player was already inside the game
     * @throws CannotCreatePlayerException if there were problems in creating the playerIn game related to the user
     */
    @Override
    public synchronized boolean addPlayer(String user) throws MaxPlayersExceededException, UserAlreadyInThisGameException, CannotCreatePlayerException {

        if (this.isFull()) throw new MaxPlayersExceededException(user, this);

        if (playerIndex(user) >= 0) throw new UserAlreadyInThisGameException(user, this);

        PlayerInGame player;
        try {
            player = new PlayerInGame(user, this);
        } catch (CannotAddPlayerInDatabaseException e) {
            throw new CannotCreatePlayerException(user);
        }
        players[nextFree()] = player;

        changeAndNotifyObservers(new PlayersChangedNotification(user, true, numActualPlayers(), numPlayers));

        return this.isFull();
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

        while (roundTrack.getCurrentRound() < GameConstants.NUM_OF_ROUNDS){
            nextRound();
        }

        calculateScore();
        saveScore();
        endGame();
    }

    /**
     * @param time is the ammount of time that the thread sleeps
     */
    private void waitPlayers(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e){
            //TODO: La partita è stata sospesa forzatamente
        }
    }

    /**
     * Starts the multi-player game, it calls all the extraction methods and sets some parameters to their initial values.
     * Overrides the method of the abstract class game.
     */
    @Override
    public void initializeGame() {
        extractPrivateObjectives();
        extractWPCs();
        extractToolCards();
        extractPublicObjectives();
        shufflePlayers();

        turnPlayer = 0;
        roundPlayer = players.length-1;
        currentTurn = 1;
    }

    /**
     * shuffle the players array, the new disposition in the array will be the turn sequence.
     */
    private void shufflePlayers(){
        ArrayList<PlayerInGame> playersList = new ArrayList<>(Arrays.asList(players));
        Collections.shuffle(playersList);
        players = (PlayerInGame[]) playersList.toArray(players);
    }

    /**
     * Waits that all players choose a wpc; if it doesn't happen in a minutes from the call of this method the schemas
     * will be chosen by the server between the 4 given to each player by calling the next method.
     */
    @Override
    public void waitForWpcResponse(){
        currentThread = new ChooseWpcThread(players, GameConstants.CHOOSE_WPC_WAITING_TIME + GameConstants.TASK_DELAY);
        Thread waitForWpcs = new Thread(currentThread);

        try {
            System.out.println("Sto aspettando che i giocatori scelgano le wpc");
            waitForWpcs.start();
            waitForWpcs.join(GameConstants.CHOOSE_WPC_WAITING_TIME + GameConstants.TASK_DELAY);
            System.out.println("Ho smesso di aspettare che i giocatori scelgano le wpc");
            if (waitForWpcs.isAlive()) {
                waitForWpcs.interrupt();
                selectRandomWpc(wpcsByUser);
                System.out.println("Ho estratto casualmente le wpc dei giocatori rimanenti");
            }
        } catch (InterruptedException e){

        }
    }

    @Override
    public void removeToolCardIfSingleGame(ToolCard card) {
        return;
    }

    /**
     * Chooses for each player one of the four schemas in a random way and sets it in the player in game object
     *
     * @param wpcsByUser is the HashMap which contains for the player username (keys) and the arrayLists with the schemas'
     *                   ids
     */
    private void selectRandomWpc(HashMap<String,ArrayList<String>> wpcsByUser) {
        for (PlayerInGame player : players){
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

    /**
     * when all players have done their turns this method is called. It puts all left dices in the round Track,
     * extracts the new dices from the diceBag and sets the first round player. It also sends a NextRound Notification
     * containing all new information.
     */
    @Override
    public void nextRound() {
        ClientWpc oldClientWpc = null;
        String oldUser = null;
        if (endTurnData != null){
            oldClientWpc = endTurnData.wpcOldUser;
            oldUser = endTurnData.oldUser;
        }

        for (Dice dice : extractedDices) roundTrack.addDice(dice);
        roundTrack.nextRound();

        for (PlayerInGame player: players)
            player.clearPlayerRound();

        extractedDices = diceBag.extractDices(numPlayers);
        ArrayList<ClientDice> extractedClientDices = new ArrayList<>();
        for (Dice dice : extractedDices) extractedClientDices.add(dice.getClientDice());
        changeAndNotifyObservers(new NewRoundNotification(roundTrack.getCurrentRound(), extractedClientDices, roundTrack.getClientRoundTrack(), oldClientWpc, oldUser));

        currentTurn = 0;
        if (roundPlayer < players.length - 1) roundPlayer++;
        else roundPlayer = 0;
        turnPlayer = roundPlayer;

        while (currentTurn < GameConstants.NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME*numPlayers){
            nextTurn();
        }
    }


    /**
     * Sets the player which is ending his/her turn to !active()
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
    void nextTurn(){
        turnPlayer = nextPlayer();
        currentTurn++;

        while (shouldSkipTurn()) {
            PlayerInGame player = players[turnPlayer];
            String toolcardUsedID = (player.getCardUsedBlockingTurn() != null) ? player.getCardUsedBlockingTurn().getID() : null;
            changeAndNotifyObservers(new PlayerSkipTurnNotification(player.getUser(), toolcardUsedID, player.isDisconnected()));

            if (currentTurn < GameConstants.NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME*numPlayers) {
                turnPlayer = nextPlayer();
                currentTurn++;

            } else return;
        }

        players[turnPlayer].setActive();
        changeAndNotifyObservers(new NextTurnNotification(currentTurn, players[turnPlayer].getUser(),endTurnData));
        startTurnTimer();
    }

    /**
     * @return true if the player must skip the turn, false if not
     */
    boolean shouldSkipTurn(){
        PlayerInGame player = players[turnPlayer];
        return player.isDisconnected() ||
                (player.getCardUsedBlockingTurn() != null && player.getTurnForRound() == 2);
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
            waitForEndTurn.join(GameConstants.TIME_TO_PLAY_TURN_MULTIPLAYER + GameConstants.TASK_DELAY);
            if (waitForEndTurn.isAlive()) {
                System.out.println("Tempo per il turno scaduto");
                waitForEndTurn.interrupt();
                players[turnPlayer].forceEndTurn();
            }
            System.out.println("OK");
        } catch (InterruptedException e) {
            //TODO: partita interrotta?
            e.printStackTrace();/*Do nothing*/
        }
    }

    /**
     * @return the //todo
     */
    int nextPlayer(){
        if (currentTurn % numPlayers == 0) {
            players[turnPlayer].setTurnInRound(2);
            return turnPlayer;
        } else if ((currentTurn / numPlayers) % 2 == 0){
            if (turnPlayer != numPlayers-1){
                players[turnPlayer+1].setTurnInRound(1);
                return turnPlayer+1;
            } else {
                players[0].setTurnInRound(1);
                return 0;
            }
        } else {
            if (turnPlayer != 0){
                players[turnPlayer-1].setTurnInRound(2);
                return turnPlayer-1;
            } else {
                players[numPlayers-1].setTurnInRound(2);
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
    }

    /**
     * Removes all player in game from the database
     */
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
