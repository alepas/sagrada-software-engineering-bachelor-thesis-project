package server.model.game;

import server.constants.GameConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.thread.ChooseWpcThread;
import server.model.game.thread.WaiterThread;
import server.model.users.PlayerInGame;
import server.model.wpc.Wpc;
import shared.clientInfo.ClientDice;
import shared.clientInfo.ClientEndTurnData;
import shared.clientInfo.ClientWpc;
import shared.exceptions.gameexceptions.*;
import shared.exceptions.usersAndDatabaseExceptions.CannotUpdateStatsForUserException;
import shared.network.commands.notifications.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static server.constants.GameConstants.CHOOSE_WPC_WAITING_TIME;
import static server.constants.GameConstants.TASK_DELAY;

public class SinglePlayerGame extends Game {
    private HashMap<String, Integer> scoreList = new HashMap<>();
    private ClientEndTurnData endTurnData;
    private WaiterThread currentThread;
    private int level;

    /**
     * Creates a singlePlayerGame
     * @param numPlayers is the number of players what will take part to the game
     * @throws InvalidSinglePlayerGamePlayersException  when the num of player isn't in the range
     *
     */
    public SinglePlayerGame(int numPlayers, int level) throws InvalidGameParametersException {
        super(numPlayers);
        if (level<1||level>5)
            throw new InvalidGameParametersException(level,false);
        if (numPlayers !=1)
            throw new InvalidGameParametersException(numPlayers,true);

        numOfPrivateObjectivesForPlayer = GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_SINGLEPLAYER_GAME;
        //numOfToolCards = GameConstants.MAX_NUM_OF_TOOL_CARDS_IN_SINGLEPLAYER_GAME;
        numOfToolCards = GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME;
        numOfPublicObjectiveCards = GameConstants.NUM_PUBLIC_OBJ_IN_SINGLEPLAYER_GAME;
    }


    /**
     * if possible creates a new Player in game and adds it to the array of players
     *
     * @param user is the username of the player that would like to enter in the game
     * @return true if, after adding the new player, the game is full, false if there's still space
     * @throws MaxPlayersExceededException the game is full and it is not possible to add a new player
     * @throws UserAlreadyInThisGameException the player was already inside the game
     */
    @Override
    public synchronized boolean addPlayer(String user) throws MaxPlayersExceededException, UserAlreadyInThisGameException {

        if (this.isFull()) throw new MaxPlayersExceededException(user, this);

        if (playerIndex(user) >= 0) throw new UserAlreadyInThisGameException(user, this);

        PlayerInGame player;
            player = new PlayerInGame(user, this);
        players[nextFree()] = player;

        changeAndNotifyObservers(new PlayersChangedNotification(user, true, numActualPlayers(), numPlayers));

        return this.isFull();
    }

    @Override
    public void disconnectPlayer(String user) throws UserNotInThisGameException {

    }




    //------------------------------- Metodi validi solo lato server ------------------------------

    @Override
    public void run() {
        //Codice che regola il funzionamento della partita
        waitPlayers();
        changeAndNotifyObservers(new GameStartedNotification());

        System.out.println("La partità è iniziata");
        initializeGame();
        allWpcsChosen=true;

        while (roundTrack.getCurrentRound() < GameConstants.NUM_OF_ROUNDS){
            nextRound();
        }

        calculateScore();
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

        turnPlayer = 0;
        currentTurn = 1;
    }


    /**
     * Waits that all players choose a wpc; if it doesn't happen in a minutes from the call of this method the schemas
     * will be chosen by the server between the 4 given to each player by calling the next method.
     */
    @Override
    public void waitForWpcResponse(){
        currentThread = new ChooseWpcThread(players, CHOOSE_WPC_WAITING_TIME + TASK_DELAY);
        Thread waitForWpcs = new Thread(currentThread);

        try {
            System.out.println("Sto aspettando che il giocatore scelga la wpc");
            waitForWpcs.start();
            waitForWpcs.join((long) CHOOSE_WPC_WAITING_TIME + TASK_DELAY);
            System.out.println("Ho smesso di aspettare che il giocatore scelga la wpc");
            if (waitForWpcs.isAlive()) {
                waitForWpcs.interrupt();
                selectRandomWpc(wpcsByUser);
                System.out.println("Ho estratto casualmente le wpc dei giocatori rimanenti");
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void removeToolCardIfSingleGame(ToolCard card) {
        toolCards.remove(card);
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
        while (currentTurn < GameConstants.NUM_OF_TURNS_FOR_PLAYER_IN_SINGLEPLAYER_GAME*numPlayers) nextTurn();
    }


    /**
     * Sets the player which is ending his/her turn to !active()
     * @param endTurnData are related to what happened in the last turn
     */
    @Override
    public void endTurn(ClientEndTurnData endTurnData) {
        turnFinished = true;
        this.endTurnData = endTurnData;
    }


    /**
     * calls the nextPlayer() method, the player that will have to play the new turn is set to active. Before activate
     * the players it is necessary to check if he/she should skip the turn: if yes the method nextPlayer() will be called
     * again and a playerSkipNotification will be thrown.
     */
    private void nextTurn(){
        turnPlayer = nextPlayer();
        currentTurn++;

        while (shouldSkipTurn()) {
            PlayerInGame player = players[turnPlayer];
            String toolcardUsedID = (player.getCardUsedBlockingTurn() != null) ? player.getCardUsedBlockingTurn().getID() : null;
            changeAndNotifyObservers(new PlayerSkipTurnNotification(player.getUser(), toolcardUsedID, player.isDisconnected()));

            if (currentTurn < GameConstants.NUM_OF_TURNS_FOR_PLAYER_IN_SINGLEPLAYER_GAME*numPlayers) {
                turnPlayer = nextPlayer();
                currentTurn++;

            } else return;
        }

        players[turnPlayer].setActive();
        changeAndNotifyObservers(new NextTurnNotification(currentTurn, players[turnPlayer].getUser(), GameConstants.TIME_TO_PLAY_TURN_MULTIPLAYER, endTurnData));
    }

    /**
     * @return the
     */
    private int nextPlayer(){
            players[turnPlayer].setTurnInRound(currentTurn+1);
            return turnPlayer;
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
            System.out.println("freeCell" + wpc.getNumFreeCells());
            int score = privateObjScore(player)  - 3*wpc.getNumFreeCells();

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
                if (dice.getDiceColor() == playerColors[0]) score += dice.getDiceNumber();

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


}
