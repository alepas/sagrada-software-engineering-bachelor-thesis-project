package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.responses.notifications.*;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.constants.CliConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.wpc.WPC;

import java.util.*;

public class CliView implements Observer, NotificationHandler {
    private Scanner fromKeyBoard;
    private final CliRender cliRender;

    private boolean gameStarted = false;
    private boolean wpcExtracted = false;
    private boolean toolExtracted = false;
    private boolean pocExtracted = false;
    private boolean isGameFull = false;

    private Timer timer = new Timer();
    private Task task = null;
    private final Object waiter = new Object();

    // ----- The view is composed with the controller (strategy)
    private final CliController controller;

    public CliView(CliController controller) {
        this.controller = controller;
        this.fromKeyBoard = new Scanner(System.in);
        this.cliRender = new CliRender();
    }

    private String userInput() {
        return fromKeyBoard.nextLine();
    }

    public void displayText(String text) {
        System.out.println(">>> " + text);
    }

    private void stopHere() {
        int stopHere = 0;
        synchronized (waiter) {
            try {
                displayText("Stop here");
                while (stopHere== 0) waiter.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void startNewTask(int taskTime){
        task = new Task(taskTime, waiter);
        timer.schedule(task, 0, task.getSensibility());
    }

    private void startNewTask(int taskTime, int sensibility){
        task = new Task(taskTime, sensibility, waiter);
        timer.schedule(task, 0, task.getSensibility());
    }

    private void deleteTask() {
        timer.purge();
        timer.cancel();
        task = null;
    }

    //---------------------------- External methods ----------------------------

    public boolean logPhase(){
//        WPC wpc = controller.getWpcByID("5");
//        Dice dice = new Dice(Color.RED, 2);
//        wpc.addDice(dice, wpc.getSchema().get(2), 4);
//        System.out.println(cliRender.renderWpc(wpc));
//        return true;
        while (true) {
            displayText(CliConstants.CHOOSE_LOG_TYPE);
            String answer = userInput();
            if (answer.equals(CliConstants.YES_RESPONSE)) return true;
            if (answer.equals(CliConstants.NO_RESPONSE)) return false;
        }
    }

    public void login() {
        String user = null;

        do {
            displayText(CliConstants.LOGIN_PHASE);
            displayText(CliConstants.INSERT_USERNAME);
            String username = userInput();
            if (username.equals(CliConstants.ESCAPE_RESPONSE)) return;

            displayText(CliConstants.INSERT_PASS);
            String password = userInput();

            user = controller.login(username, password);
        } while (user == null);

        displayText(CliConstants.LOG_SUCCESS + user);
    }

    public void createUsername() {
        String user = null;

        do {
            displayText(CliConstants.CREATE_USER_PHASE);
            displayText(CliConstants.INSERT_USERNAME);
            String username = userInput();
            if (username.equals(CliConstants.ESCAPE_RESPONSE)) return;

            displayText(CliConstants.INSERT_PASS);
            String password = userInput();

            user = controller.createUser(username, password);
        } while (user == null);

        displayText(CliConstants.LOG_SUCCESS + user);
    }

    public void mainMenuPhase() {
        do {
            displayText(CliConstants.PRESENT_MAIN_MENU);
            String response = userInput();
            switch (response){
                case "1":
                    if (findGamePhase()) playGame();
                    break;
                case "2":
                    //Visualizza stat
                    break;
                default:
                    break;
            }
        } while (true);
    }


    //Restituisce true se è riuscito a trovare una partita, altrimenti false
    //Inoltre setta 'isGameFull' a true se la partita ha raggiunto il numero di giocatori necessario,
    //a false altrimenti
    private boolean findGamePhase(){
        String response;
        int numPlayers;

        do {
            displayText(CliConstants.SELECT_NUM_PLAYERS);
            response = userInput();
            try {
                numPlayers = Integer.parseInt(response);
                int i = controller.findGame(numPlayers);
                if (i < 0){
                    displayText("Impossibile trovare partita. Riprovare più tardi");
                    return false;
                } else {
                    isGameFull = (i == 1);
                    return true;
                }
            } catch (NumberFormatException e){
                displayText("Perfavore inserire un numero intero");
            }
        } while (true);
    }

    private void playGame() {
        waitPlayers();
        waitForWpc();
        chooseWpcPhase();
        waitForToolcards();
        waitForPoc();

        stopHere();
    }

    //Attende che tutti i giocatori entrino in partita
    private void waitPlayers() {
        Thread waitingPlayers = new Thread(() -> {
            if (!isGameFull) displayText("In attesa di altri giocatori...");
            while (!gameStarted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
            };
        });

        waitingPlayers.start();

        try {
            waitingPlayers.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare");
        }
    }

    //Attende che arrivi la notifica contenente le wpc che sono proposte ai giocatori
    private void waitForWpc() {
        Thread waitingWPC = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { }

            if (!wpcExtracted) displayText("In attesa delle wpc...");
            while (!wpcExtracted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
            };
        });

        waitingWPC.start();

        try {
            waitingWPC.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare");
        }
    }

    //Attende che arrivi la notifica contenente le toolcard
    private void waitForToolcards() {
        Thread waitingToolcards = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { }

            if (!toolExtracted) displayText("In attesa delle toolcard...");
            while (!toolExtracted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
            };
        });

        waitingToolcards.start();

        try {
            waitingToolcards.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare");
        }
    }

    //Attende che arrivi la notifica contenente gli obbiettivi pubblici
    private void waitForPoc() {
        Thread waitingPoc = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { }

            if (!pocExtracted) displayText("In attesa degli obbiettivi pubblici...");
            while (!pocExtracted) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
            };
        });

        waitingPoc.start();

        try {
            waitingPoc.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare");
        }
    }

    private void chooseWpcPhase() {
        wpcExtracted = false;

        Thread requestWpcThread = new Thread(() -> {
            boolean myWpcExtracted;
            do {
                displayText("Seleziona la wpc che vuoi utilizzare");
                String wpcID = userInput();
                myWpcExtracted = controller.pickWpc(wpcID);
                if (!wpcExtracted) displayText("Attendo che gli altri giocatori selezionino la wpc");
            } while (!myWpcExtracted);
        });
        requestWpcThread.start();

        synchronized (waiter){
            try {
                int lastTimeLeft = task.timeLeft();
                int nextStep = 50;

                while (!wpcExtracted) {
                    if (nextStep >= 0 && task.timeLeft() <= nextStep && lastTimeLeft > nextStep) {
                        displayText("Rimangono " + nextStep + " secondi per scegliere le wpc");
                        lastTimeLeft = nextStep;
                        if (nextStep == 0) displayText("Tempo scaduto");
                        if (nextStep <= 5) nextStep = 0;
                        if (nextStep == 15) nextStep = 5;
                        if (nextStep == 30) nextStep = 15;
                        if (nextStep == 50) nextStep = 30;
                    }
                    waiter.wait();
                }

                requestWpcThread.interrupt();
                deleteTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) {
        ((Notification) arg).handle(this);
    }




    //-------------------------------- Notification Handler ------------------------------------

    @Override
    public void handle(GameStartedNotification notification) {
        gameStarted = true;
        displayText("Partita iniziata");
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
        if (notification.joined){
            displayText(notification.username + " è entrato in partita.");
        } else {
            displayText(notification.username + " è uscito dalla partita.");
        }
        displayText("Giocatori presenti: " + notification.actualPlayers + " di " +
                notification.numPlayers + " necessari.");
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        Color[] colors = notification.colorsByUser.get(notification.username);
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append("I tuoi private objective sono: ");
        else str.append("Il tuo private objective è: ");

        for (Color color : colors){
            str.append(color + "\t");
        }

        displayText(str.toString());
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        ArrayList<String> userWpcs = notification.wpcsByUser.get(notification.username);
        displayText("Le tue wpc sono:\n\n");

        WPC[] wpcs = new WPC[2];
        int num;

        for(int i = 0; i < userWpcs.size(); i++){
            num = i%2;
            wpcs[num] = controller.getWpcByID(userWpcs.get(i));
            if ( (i == userWpcs.size()-1) && (num == 0) ) System.out.println(cliRender.renderWpc(wpcs[num]));
            if (num == 1) System.out.println(cliRender.renderWpcs(wpcs, CliConstants.WpcSpacing));;
        }

        startNewTask(notification.timeToCompleteTask);
        wpcExtracted = true;
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        displayText(notification.username + " ha scelto la wpc: " + notification.wpcID);
        if (controller.areAllWpcsReceived()){
            wpcExtracted = true;
            displayText("Tutti i giocatori hanno scelto la wpc");
        }
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        ArrayList<ToolCard> toolCards = controller.getToolcard();
        displayText("Le toolcards della partita sono:\n");

        for (ToolCard card : toolCards){
            displayText("ID: " + card.getID());
            displayText("Nome: " + card.getName());
            displayText("Descrizione: " + card.getDescription() + "\n");
        }
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        ArrayList<PublicObjectiveCard> pocCards = controller.getPublicObjectiveCards();
        displayText("Gli obbiettivi pubblici della partita sono:\n");

        for (PublicObjectiveCard card : pocCards){
            displayText("ID: " + card.getID());
            displayText("Nome: " + card.getName());
            displayText("Descrizione: " + card.getDescription() + "\n");
        }
    }


}
