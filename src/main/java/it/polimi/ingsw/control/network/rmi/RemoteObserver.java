package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.model.game.MultiplayerGame;
import it.polimi.ingsw.model.game.gameObservers.GameObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObserver extends UnicastRemoteObject implements GameObserver {

    public RemoteObserver() throws RemoteException { }

    @Override
    public void onJoin(String username) {
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        game.addPlayer(username);
    }

    @Override
    public void onLeave(String username) {
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        game.removePlayer(username);
    }
}
