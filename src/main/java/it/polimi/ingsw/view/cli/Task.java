package it.polimi.ingsw.view.cli;

import java.util.TimerTask;

public class Task extends TimerTask {
    private int taskTime;
    private final int sensibility;
    private final Object waiter;
    private boolean run = true;

    public Task(int taskTime, Object waiter) {
        this(taskTime, 1000, waiter);
    }

    public Task(int taskTime, int sensibility, Object waiter) {
        this.taskTime = taskTime;
        this.sensibility = sensibility;
        this.waiter = waiter;
    }

    @Override
    public void run() {
        if (run) {
            synchronized (waiter) {
                taskTime -= sensibility;
                waiter.notifyAll();
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
