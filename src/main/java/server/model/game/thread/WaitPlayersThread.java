package server.model.game.thread;

import server.model.game.Game;

public class WaitPlayersThread extends WaiterThread {
    private Game game;
    private boolean pause;

    public WaitPlayersThread(int timeLeft, Game game) {
        super(timeLeft);
        this.game = game;
        this.pause = false;
    }

    @Override
    public void run() {
        while (!game.isStarted() && timeLeft > 0){
            try {
                Thread.sleep(sensibility);
                if (!pause) {
                    timeLeft -= sensibility;
                    System.out.println(timeLeft);
                }
            } catch (InterruptedException e) {/*Do nothing*/}
        }
    }

    public int getTimeLeft(){
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft){
        this.timeLeft = timeLeft;
        pause = false;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
}
