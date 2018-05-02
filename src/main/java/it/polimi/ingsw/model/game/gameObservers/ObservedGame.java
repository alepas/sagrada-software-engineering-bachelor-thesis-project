package it.polimi.ingsw.model.game.gameObservers;

import it.polimi.ingsw.model.game.gameObservers.notices.Notice;

public interface ObservedGame {

    void addObserver(GameObserver o);
    void removeObserver(GameObserver o);
    void notifyObservers(Notice notice);
}
