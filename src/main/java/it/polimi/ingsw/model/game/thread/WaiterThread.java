package it.polimi.ingsw.model.game.thread;

public abstract class WaiterThread implements Runnable {
    int timeLeft;
    int sensibility = 500;

    public WaiterThread(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
