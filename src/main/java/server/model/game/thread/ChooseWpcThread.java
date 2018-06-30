package server.model.game.thread;

import server.model.users.PlayerInGame;

public class ChooseWpcThread extends WaiterThread {
    private PlayerInGame[] players;

    public ChooseWpcThread(PlayerInGame[] players, int timeLeft) {
        super(timeLeft);
        this.players = players;
    }

    @Override
    public void run() {
        while (needToWaitPlayersWpc()){
            try {
                Thread.sleep( sensibility);
                timeLeft -= sensibility;
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
