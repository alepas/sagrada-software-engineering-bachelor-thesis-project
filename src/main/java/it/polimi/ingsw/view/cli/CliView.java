package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.commands.notifications.NotificationHandler;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.CliConstants;

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
    private final Object prova = new Object();

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
        synchronized (prova) {
            try {
                displayText("Stop here");
                while (stopHere==0) prova.wait();
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
        task.stop();
        timer.cancel();
        timer.purge();
        task = null;
    }

    //---------------------------- External methods ----------------------------

    public boolean logPhase(){
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

        while (controller.isInGame()){
            waitForTurn();
            playTurn();
        }

        stopHere();
    }

    private void waitForTurn() {
        synchronized (waiter){
            while (!controller.isActive()) {
                try {
                    displayText("In attesa del mio turno...");
                    waiter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void playTurn() {
        displayText("\n");
        displayText("Questa è la tua wpc");
        displayText("Favours rimasti: " + controller.getFavour() + "\n"
            + cliRender.renderWpc(controller.getMyWpc(), false));

        String response;
        loop: do{
            displayText("Cosa vuoi fare?");
            displayText("1) posiziona dado");
            displayText("2) usa toolcard");
            displayText("3) passa turno");
            response = userInput();
            try {
                int action = Integer.parseInt(response);
                switch (action){
                    case 1:
                        pickDice();
                        break;
                    case 2:
                        break;
                    case 3:
                        if (controller.passTurn()) break loop;
                        break;
                    default:
                        displayText("Comando non riconosciuto");
                }
            } catch (NumberFormatException e){
                displayText("Perfavore inserire il numero dell'azione che si vuole eseguire");
            }
        } while (true);


    }

    private void pickDice() {
        displayText("Inserisci l'ID del dado da posizionare");
        int id = Integer.parseInt(userInput());
        displayText("Inserisci la colonna in cui posizionarlo");
        int col = Integer.parseInt(userInput());
        displayText("Inserisci la riga in cui posizionarlo");
        int row = Integer.parseInt(userInput());
        controller.placeDice(id, col, row);
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
        ClientColor[] colors = notification.colorsByUser.get(controller.getUser());
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append("I tuoi private objective sono: ");
        else str.append("Il tuo private objective è: ");

        for (ClientColor color : colors){
            str.append(color + "\t");
        }

        displayText(str.toString());
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        ArrayList<ClientWpc> userWpcs = notification.wpcsByUser.get(controller.getUser());
        displayText("Le tue wpc sono:\n\n");

        ClientWpc[] wpcs = new ClientWpc[2];
        int num;

        for(int i = 0; i < userWpcs.size(); i++){
            num = i%2;
            wpcs[num] = userWpcs.get(i);
            if ( (i == userWpcs.size()-1) && (num == 0) ) System.out.println(cliRender.renderWpc(wpcs[num], true));
            if (num == 1) System.out.println(cliRender.renderWpcs(wpcs, CliConstants.WpcSpacing));;
        }

        startNewTask(notification.timeToCompleteTask);
        wpcExtracted = true;
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        displayText(notification.username + " ha scelto la wpc: " + notification.wpc.getWpcID());
        if (controller.areAllWpcsReceived()){
            wpcExtracted = true;
            displayText("Tutti i giocatori hanno scelto la wpc");
        }
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        ArrayList<ClientToolCard> cards = controller.getToolcards();
        displayText("Le toolcards della partita sono:\n");

        for (ClientToolCard card : cards) {
            displayText("ID: " + card.getId());
            displayText("Nome: " + card.getName());
            displayText("Descrizione: " + card.getDescription() + "\n");
        }

        toolExtracted = true;
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        ArrayList<ClientPoc> cards = controller.getPOC();
        displayText("Gli obbiettivi pubblici della partita sono:\n");

        for (ClientPoc card : cards) {
            displayText("ID: " + card.getId());
            displayText("Nome: " + card.getName());
            displayText("Descrizione: " + card.getDescription() + "\n");
        }

        pocExtracted = true;
    }

    @Override
    public void handle(NewRoundNotification notification) {
        displayText("Round: " + notification.roundNumber);
        displayText("Dadi estratti:");
        System.out.println("\n" + cliRender.renderDices(notification.extractedDices) + "\n");
    }

    @Override
    public void handle(NextTurnNotification notification) {
        displayText("\n");
        displayText("Turno: " + notification.turnNumber + "\tRound: " + controller.getCurrentRound());
        displayText("Turno di " + notification.activeUser);
        displayText("Dadi presenti: ");
        System.out.println("\n" + cliRender.renderDices(controller.getExtractedDices()) + "\n");

        synchronized (waiter){
            if (controller.isActive()) waiter.notifyAll();
        }
    }

    @Override
    public void handle(DiceChangedNotification notification) {

    }

    @Override
    public void handle(DicePlacedNotification notification) {
        displayText(notification.username + " ha posizionato il dado " + notification.dice.getDiceID() +
            " in posizione " + notification.position.toString());
        displayText("\n" + cliRender.renderWpc(notification.wpc, false));
        if (notification.newExtractedDices != null) {
            displayText("I dadi nella riserva sono: \n");
            System.out.println(cliRender.renderDices(notification.newExtractedDices));
        }

    }

    @Override
    public void handle(ToolCardCanceledNotification notification) {

    }

    @Override
    public void handle(ToolCardUsedNotification notification) {

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }


}
