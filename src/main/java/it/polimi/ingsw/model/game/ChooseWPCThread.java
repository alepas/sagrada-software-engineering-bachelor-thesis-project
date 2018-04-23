package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.usersdb.PlayerInGame;

import java.util.ArrayList;

public class ChooseWPCThread implements Runnable {
    private Thread t;
    private PlayerInGame player;
    private ArrayList<String> WPCs;

    public ChooseWPCThread(PlayerInGame player, ArrayList<String> WPCs) {
        this.player = player;
        this.WPCs = WPCs;
    }

    @Override
    public void run() {
        player.chooseWPC(WPCs);
    }

    public void start(){
        if (t == null){
            t = new Thread(this);
            t.start();
        }
    }

    public void join() throws InterruptedException{
        t.join();
    }

    //time is passed in milliseconds
    public void join(int time) throws InterruptedException{
        t.join(time);
        if (t.isAlive()){
            t.interrupt();
        }
    }

    public boolean isInterrupted(){
        return t.isInterrupted();
    }
}
