package it.polimi.ingsw.control.network.rmi;

import it.polimi.ingsw.model.constants.NetworkConstants;

public class RmiUserConnectionTimer implements Runnable {
    private final String userToken;
    private int timeLeft;
    private boolean run;
    private DisconnectionHandler handler;

    public RmiUserConnectionTimer(String userToken, DisconnectionHandler handler) {
        this.userToken = userToken;
        this.timeLeft = NetworkConstants.RMI_INITIAL_POLLING_TIME;
        this.run = true;
        this.handler = handler;
    }

    public void reset(){
        timeLeft = NetworkConstants.RMI_MAX_POLLS_MISSED * NetworkConstants.RMI_POLLING_TIME;
    }

    public void stop(){
        run = false;
    }

    @Override
    public void run() {
        try {
            while (run && timeLeft > 0) {
                Thread.sleep(NetworkConstants.RMI_POLLING_TIME);
                timeLeft -= NetworkConstants.RMI_POLLING_TIME;
            }
            if (timeLeft <= 0) handler.notifyDisconnection(userToken);
        } catch (InterruptedException e){ /*Do nothing*/}
    }
}
