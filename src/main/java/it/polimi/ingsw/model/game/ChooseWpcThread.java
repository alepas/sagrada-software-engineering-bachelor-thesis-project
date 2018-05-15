package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public class ChooseWpcThread implements Runnable {
    private PlayerInGame[] players;

    public ChooseWpcThread(PlayerInGame[] players) {
        this.players = players;
    }

    @Override
    public void run() {
        while (needToWaitPlayersWpc()){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean needToWaitPlayersWpc(){
        for (PlayerInGame player : players){
            if (player.getWPC() == null) return true;
        }
        return false;
    }
}
