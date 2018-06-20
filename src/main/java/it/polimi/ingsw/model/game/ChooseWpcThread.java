package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.usersdb.PlayerInGame;

public class ChooseWpcThread implements Runnable {
    private PlayerInGame[] players;

    ChooseWpcThread(PlayerInGame[] players) {
        this.players = players;
    }

    @Override
    public void run() {
        while (needToWaitPlayersWpc()){
            try {
                Thread.sleep( 500);
            } catch (InterruptedException e) {/*Do nothing*/}
        }
    }

    private boolean needToWaitPlayersWpc(){
        for (PlayerInGame player : players){
            if (player.getWPC() == null) return true;
        }
        return false;
    }
}
