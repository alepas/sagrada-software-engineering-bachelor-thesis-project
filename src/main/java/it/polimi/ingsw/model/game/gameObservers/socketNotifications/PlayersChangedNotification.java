package it.polimi.ingsw.model.game.gameObservers.socketNotifications;

import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.ResponseHandler;
import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.model.game.MultiplayerGame;

public class PlayersChangedNotification implements Response {
    public final String username;
    public final boolean joined;

    public PlayersChangedNotification(String username, boolean joined) {
        this.username = username;
        this.joined = joined;
    }

    @Override
    public void handle(ResponseHandler handler) {
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        try {
            if (joined){
                game.addPlayer(username);
            } else {
                game.removePlayer(username);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
