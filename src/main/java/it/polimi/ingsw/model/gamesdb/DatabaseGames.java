package it.polimi.ingsw.model.gamesdb;

import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiplayerGame;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseGames {
    private static DatabaseGames instance;

    private ArrayList<Game> activeGames;
    private ArrayList<MultiplayerGame> availableGames;
    private HashMap<Game, Thread> threadByGame;

    private DatabaseGames(){
        this.activeGames = new ArrayList<>();
        this.availableGames = new ArrayList<>();
        this.threadByGame = new HashMap<>();
    }

    public synchronized static DatabaseGames getInstance(){
        if (instance == null) instance = new DatabaseGames();
        return instance;
    }



    //------------------------------- Database methods -------------------------------

    public synchronized Game findGame(String username, int numPlayers) throws InvalidPlayersException {
        if (numPlayers < GameConstants.MIN_NUM_PLAYERS || numPlayers > GameConstants.MAX_NUM_PLAYERS)
            throw new InvalidPlayersException(numPlayers);

        if (numPlayers != 1) {
            for (MultiplayerGame game : availableGames) {
                if (game.getNumPlayers() == numPlayers) {
                    try {
                        if (game.addPlayer(username)) {
                            moveGameToActive(game);
                            Thread gameThread = new Thread(game);
                            threadByGame.put(game, gameThread);
                            gameThread.start();     //Fa iniziare la partita
                        }
                        return game;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        return createNewGame(username, numPlayers);
    }

    private synchronized void moveGameToActive(Game game){
        availableGames.remove(game);
        activeGames.add(game);
    }

    private synchronized Game createNewGame(String username, int numPlayers) {
        Game game = null;
        if (numPlayers == 1) {
            //Crea SingleplayerGame
        } else {
            game = new MultiplayerGame(numPlayers);
            ((MultiplayerGame) game).addPlayer(username);
            availableGames.add((MultiplayerGame) game);
        }
        return game;
    }

    public synchronized void removeGame(Game game){

    }
}
