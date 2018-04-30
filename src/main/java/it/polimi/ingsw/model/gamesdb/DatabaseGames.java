package it.polimi.ingsw.model.gamesdb;

import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.exceptions.gameExceptions.InvalidPlayersException;
import it.polimi.ingsw.model.game.AbstractGame;
import it.polimi.ingsw.model.game.MultiPlayerGame;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseGames {
    private static DatabaseGames instance;

    private ArrayList<AbstractGame> activeGames;
    private ArrayList<MultiPlayerGame> availableGames;
    private HashMap<AbstractGame, Thread> threadByGame;

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

    public synchronized AbstractGame findGame(String username, int numPlayers) throws InvalidPlayersException {
        if (numPlayers < GameConstants.MIN_NUM_PLAYERS || numPlayers > GameConstants.MAX_NUM_PLAYERS)
            throw new InvalidPlayersException(numPlayers);

        if (numPlayers != 1) {
            for (MultiPlayerGame game : availableGames) {
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

    private synchronized void moveGameToActive(AbstractGame game){
        availableGames.remove(game);
        activeGames.add(game);
    }

    private synchronized AbstractGame createNewGame(String username, int numPlayers) {
        AbstractGame game = null;
        if (numPlayers == 1) {
            //Crea SingleplayerGame
        } else {
            game = new MultiPlayerGame(numPlayers);
            ((MultiPlayerGame) game).addPlayer(username);
            availableGames.add((MultiPlayerGame) game);
        }
        return game;
    }

    public synchronized void removeGame(AbstractGame game){

    }
}
