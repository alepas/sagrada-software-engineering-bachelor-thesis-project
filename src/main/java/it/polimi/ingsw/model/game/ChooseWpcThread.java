package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public class ChooseWpcThread implements Runnable {
    private PlayerInGame[] players;

    public ChooseWpcThread(PlayerInGame[] players) {
        this.players = players;
    }

    @Override
    public void run() {
        int startTime = GameConstants.CHOOSE_WPC_WAITING_TIME + GameConstants.TASK_DELAY;
        while (needToWaitPlayersWpc()){
            try {
                Thread.sleep( startTime/ 20);
            } catch (InterruptedException e) {}
        }
    }

    private boolean needToWaitPlayersWpc(){
        for (PlayerInGame player : players){
            if (player.getWPC() == null) return true;
        }
        return false;
    }
}
