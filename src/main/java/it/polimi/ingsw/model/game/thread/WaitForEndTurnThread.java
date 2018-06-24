package it.polimi.ingsw.model.game.thread;

import it.polimi.ingsw.model.game.MultiplayerGame;

public class WaitForEndTurnThread extends WaiterThread {
    private MultiplayerGame game;

    public WaitForEndTurnThread(MultiplayerGame game, int timeLeft) {
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
