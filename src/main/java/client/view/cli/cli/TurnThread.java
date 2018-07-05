package client.view.cli.cli;


import static client.constants.CliConstants.*;

public class TurnThread implements Runnable {
    private final CliView view;
    private final Task task;
    private final Object waiter;
    private boolean run;

    TurnThread(CliView view, Task task, Object waiter) {
        this.view = view;
        this.task = task;
        this.waiter = waiter;
        this.run = true;
    }

    void stop(){ run = false; }

    @Override
    public void run() {
        synchronized (waiter) {
            try {
                int lastTimeLeft = task.timeLeft();
                Integer timeLeft = null;

                while (run && (timeLeft == null || timeLeft != 0)) {
                    if ((timeLeft = hasPassedStep(lastTimeLeft)) != null) {
                        if (timeLeft == 0) view.displayText(TURN_TIME_ENDED);
                        else view.displayText(TURN_TIME_LEFT_P1 + timeLeft + TURN_TIME_LEFT_P2);

                        lastTimeLeft = timeLeft;
                    }
                    waiter.wait();
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }

    //Se è stato superato uno degli step, restituisce lo step passato
    //Altrimenti null
    private Integer hasPassedStep(int lastTimeLeft){
        for(int step : WPC_WAITING_STEPS){
            if (task.timeLeft() <= step && lastTimeLeft > step) return step;
        }
        return null;
    }

    public boolean isTerminated(){
        return !run || task.timeLeft() <= 0;
    }
}
