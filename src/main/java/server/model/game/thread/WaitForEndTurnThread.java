package server.model.game.thread;

import server.model.game.Game;

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
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
