package it.polimi.ingsw.model.game.gameObservers;

import it.polimi.ingsw.control.network.commands.Response;

public interface ObservedGame {

    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);

}
