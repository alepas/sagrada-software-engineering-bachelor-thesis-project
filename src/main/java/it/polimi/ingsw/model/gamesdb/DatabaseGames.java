package it.polimi.ingsw.model.gamesdb;

import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.exceptions.databaseGameExceptions.GameNotInAvailableListException;
import it.polimi.ingsw.model.exceptions.gameExceptions.*;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiplayerGame;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseGames {
    private static DatabaseGames instance;

    private ArrayList<Game> activeGames;
    private ArrayList<MultiplayerGame> availableGames;
    private HashMap<Game, Thread> threadByGame;

    private HashMap<String, Game> gameByID;

    private DatabaseGames(){
        this.activeGames = new ArrayList<>();
        this.availableGames = new ArrayList<>();
        this.threadByGame = new HashMap<>();
        this.gameByID = new HashMap<>();
    }

    public synchronized static DatabaseGames getInstance(){
        if (instance == null) instance = new DatabaseGames();
        return instance;
    }



    //------------------------------- Database methods -------------------------------

    public synchronized Game findGameByID(String id){
        //Restituisce null se il game non è presente
        return gameByID.get(id);
    }

    public synchronized Game findGameForUser(String username, int numPlayers) throws InvalidNumOfPlayersException, CannotCreatePlayerException {
        if (numPlayers < GameConstants.MIN_NUM_PLAYERS || numPlayers > GameConstants.MAX_NUM_PLAYERS)
            throw new InvalidNumOfPlayersException(numPlayers);

        if (numPlayers != 1) {
            for (MultiplayerGame game : availableGames) {
                if (game.getNumPlayers() == numPlayers) {
                    try {
                        if (game.addPlayer(username)) startGame(game);
                        return game;

                    } catch (NotEnoughPlayersException e){
                        e.printStackTrace();
                        return game;

                    } catch (MaxPlayersExceededException e) {
                        //Do nothing -> check next game

                    } catch (UserAlreadyInThisGameException e){
                        try {
                            if (game.isFull()) startGame(game);
                        } catch (NotEnoughPlayersException exc){
                            exc.printStackTrace();
                            return game;
                        } catch (GameNotInAvailableListException exc){
                            //TODO: destroy game
                        }
                        return game;

                    } catch (GameNotInAvailableListException e){
                        e.printStackTrace();
                        //TODO: destroy game
                    }
                }
            }
        }

        return createNewGame(username, numPlayers);
    }

    private synchronized void startGame(Game game) throws GameNotInAvailableListException, NotEnoughPlayersException {

        if (!game.isFull()) throw new NotEnoughPlayersException(game);

        moveGameToActive(game);
        Thread gameThread = new Thread(game);
        threadByGame.put(game, gameThread);
        gameThread.start();     //Fa iniziare la partita
    }

    private synchronized void moveGameToActive(Game game) throws GameNotInAvailableListException {
        if (!availableGames.remove(game)) { throw new GameNotInAvailableListException(game); }
        activeGames.add(game);
    }

    private synchronized Game createNewGame(String username, int numPlayers) throws InvalidNumOfPlayersException, CannotCreatePlayerException {
        Game game = null;
        if (numPlayers == 1) {
            //Crea SingleplayerGame
        } else {
            try {
                game = new MultiplayerGame(numPlayers);
                ((MultiplayerGame) game).addPlayer(username);
                availableGames.add((MultiplayerGame) game);

            } catch (InvalidMultiplayerGamePlayersException e){
                throw new InvalidNumOfPlayersException(numPlayers);

            } catch (MaxPlayersExceededException e){
                return createNewGame(username, numPlayers);

            } catch (UserAlreadyInThisGameException e){
                if (!availableGames.contains(game)) availableGames.add((MultiplayerGame) game);
                return game;
            }

        }

        gameByID.put(game.getID(), game);

        return game;
    }

    public synchronized void removeGame(Game game){
        activeGames.remove(game);
        availableGames.remove( (MultiplayerGame) game);
        threadByGame.remove(game);
        gameByID.remove(game.getID());

        for(String id: gameByID.keySet())
        System.out.println(id);
    }
}
