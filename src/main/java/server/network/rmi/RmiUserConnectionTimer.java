package server.network.rmi;

import shared.constants.NetworkConstants;

public class RmiUserConnectionTimer implements Runnable {
    private int timeLeft;
    private boolean run;
    private RmiClientHandler client;

    /**
     * @param client is the rmi handler
     */
    RmiUserConnectionTimer(RmiClientHandler client) {
        this.timeLeft = NetworkConstants.RMI_INITIAL_POLLING_TIME;
        this.run = true;
        this.client = client;
    }

    /**
     * reset the polling timer
     */
    void reset(){
        timeLeft = NetworkConstants.RMI_MAX_POLLS_MISSED * NetworkConstants.RMI_POLLING_TIME;
    }

    /**
     * sets run false
     */
    void stop(){
        run = false;
    }

    /**
     * while run is true and the time left is more than zero the thread sleep for the polling time, if the left
     * time is minor than zero the client will be disconnected
     */
    @Override
    public void run() {
        try {
            while (run && timeLeft > 0) {
                Thread.sleep(NetworkConstants.RMI_POLLING_TIME);
                timeLeft -= NetworkConstants.RMI_POLLING_TIME;
            }
            if (timeLeft <= 0) client.disconnect();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
