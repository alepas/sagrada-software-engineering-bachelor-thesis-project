package it.polimi.ingsw.view.cli;

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
                int[] steps = {0, 5, 15, 30, 60};
                Integer timeLeft = null;

                while (run && (timeLeft == null || timeLeft != 0)) {
                    if ((timeLeft = hasPassedStep(steps, lastTimeLeft)) != null) {
                        if (timeLeft == 0) view.displayText("Tempo per il turno scaduto");
                        else view.displayText("Rimangono " + timeLeft + " secondi per terminare il turno");

                        lastTimeLeft = timeLeft;
                    }
                    waiter.wait();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    //Se è stato superato uno degli step, restituisce lo step passato
    //Altrimenti null
    private Integer hasPassedStep(int[] steps, int lastTimeLeft){
        for(int step : steps){
            if (task.timeLeft() <= step && lastTimeLeft > step) return step;
        }
        return null;
    }

    public boolean isTerminated(){
        return !run || task.timeLeft() <= 0;
    }
}
