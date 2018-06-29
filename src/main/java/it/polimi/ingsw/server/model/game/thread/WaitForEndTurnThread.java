package it.polimi.ingsw.server.model.game.thread;

import it.polimi.ingsw.server.model.game.Game;

public class WaitForEndTurnThread extends WaiterThread {
    private Game game;

    public WaitForEndTurnThread(Game game, int timeLeft) {
        super(timeLeft);
        this.game = game;
    }

    @Override
    public void run() {
        while (!game.isTurnFinished()){
            try {
                Thread.sleep( sensibility);
                timeLeft -= sensibility;
            } catch (InterruptedException e) {/*Do nothing*/}
        }
    }
}
