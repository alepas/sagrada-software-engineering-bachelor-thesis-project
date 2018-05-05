package it.polimi.ingsw.model.game.gameObservers.socketNotifications;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.model.game.MultiplayerGame;

public class GameStartedNotification implements Response {

    public GameStartedNotification() { }

    @Override
    public void handle(ResponseHandler handler) {
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        game.setStarted(true);
    }
}
