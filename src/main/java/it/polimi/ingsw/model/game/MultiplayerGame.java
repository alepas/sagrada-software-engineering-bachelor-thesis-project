package it.polimi.ingsw.model.game;

import it.polimi.ingsw.control.network.commands.notifications.NewRoundNotification;
import it.polimi.ingsw.control.network.commands.notifications.NextTurnNotification;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.exceptions.gameExceptions.*;
import it.polimi.ingsw.control.network.commands.notifications.GameStartedNotification;
import it.polimi.ingsw.control.network.commands.notifications.PlayersChangedNotification;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotAddPlayerInDatabaseException;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MultiplayerGame extends Game {
    private int turnPlayer;
    private int roundPlayer;

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

    }

    @Override
    public void nextRound() {
        for (Dice dice : extractedDices) roundTrack.addDice(dice);

        roundTrack.nextRound();
        extractedDices = diceBag.DicesExtraction(numPlayers);
        changeAndNotifyObservers(new NewRoundNotification(roundTrack.getCurrentRound(), extractedDices));

        currentTurn = 0;
        nextTurn();
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

    protected int nextPlayer(){
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

    //Da testare
    @Override
    public void calculateScore() {

    }

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

    //Da testare
    @Override
    public void saveScore() {

    }




    //------------------------------- Metodi validi solo lato client -------------------------------

    public void setTurnPlayer(int turnPlayer){
        this.turnPlayer = turnPlayer;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }


}
