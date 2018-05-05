package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.model.exceptions.gameExceptions.MaxPlayersExceededException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserAlreadyInThisGameException;
import it.polimi.ingsw.model.exceptions.gameExceptions.UserNotInThisGameException;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.game.gameObservers.GameObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObserver extends UnicastRemoteObject implements GameObserver {

    public RemoteObserver() throws RemoteException { }

    @Override
    public void onJoin(String username) {
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        try {
            game.addPlayer(username);
        } catch (MaxPlayersExceededException e){
            //TODO
            e.printStackTrace();
        } catch (UserAlreadyInThisGameException e){
            //TODO
            e.printStackTrace();
        }

    }

    @Override
    public void onLeave(String username) {
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        try {
            game.removePlayer(username);
        } catch (UserNotInThisGameException e){
            //TODO
            e.printStackTrace();
        }
    }

    @Override
    public void onGameStarted() throws RemoteException {
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        game.setStarted(true);
    }
}
