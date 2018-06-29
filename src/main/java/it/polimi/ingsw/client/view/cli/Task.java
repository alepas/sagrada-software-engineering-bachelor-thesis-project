package it.polimi.ingsw.client.view.cli;

import java.util.TimerTask;

public class Task extends TimerTask {
    private int taskTime;
    private final int sensibility;
    private final Object timerWaiter;
    private boolean run = true;

    public Task(int taskTime, Object timerWaiter) {
        this(taskTime, 1000, timerWaiter);
    }

    public Task(int taskTime, int sensibility, Object timerWaiter) {
        this.taskTime = taskTime;
        this.sensibility = sensibility;
        this.timerWaiter = timerWaiter;
    }

    @Override
    public void run() {
        if (run) {
            synchronized (timerWaiter) {
                taskTime -= sensibility;
                timerWaiter.notifyAll();
            }
        }
    }

    public void stop(){
        run = false;
    }

    public int timeLeft() {
        return taskTime/1000; //Restituisce il tempo in secondi
    }

    public int getSensibility() {
        return sensibility;
    }
}
