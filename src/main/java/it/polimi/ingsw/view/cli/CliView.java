package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.responses.notifications.*;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.constants.CliConstants;
import it.polimi.ingsw.model.dicebag.Color;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class CliView implements Observer, NotificationHandler {
    private Scanner fromKeyBoard;

    private boolean gameStarted = false;
    private boolean wpcExtracted = false;
    private boolean isGameFull = false;

    // ----- The view is composed with the controller (strategy)
    private final CliController controller;

    public CliView(CliController controller) {
        this.controller = controller;
        this.fromKeyBoard = new Scanner(System.in);
    }

    private String userInput() {
        return fromKeyBoard.nextLine();
    }

    public void displayText(String text) {
        System.out.println(">>> " + text);
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



    private boolean findGamePhase(){
        String response;
        int numPlayers;

        do {
            displayText(CliConstants.SELECT_NUM_PLAYERS);
            response = userInput();
            try {
                numPlayers = Integer.parseInt(response);
                try {
                    isGameFull = controller.findGame(numPlayers);
                    return true;
                } catch (Exception e){
                    displayText("Impossibile trovare partita. Riprovare più tardi");
                    e.printStackTrace();
                    return false;
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


        int stopHere = 0;
        while (stopHere == 0){

        };
    }

    private void waitPlayers() {
        Thread waitingPlayers = new Thread(
                () -> {
                    if (!isGameFull) displayText("In attesa di altri giocatori...");
                    while (!gameStarted){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e){

                        }
                    };
                }
        );

        waitingPlayers.start();

        try {
            waitingPlayers.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare la partita");
        }
    }

    private void waitForWpc() {
        Thread waitingWPC = new Thread(
                () -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e){

                    }
                    if (!wpcExtracted) displayText("In attesa delle wpc...");
                    while (!wpcExtracted){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e){

                        }
                    };
                }
        );

        waitingWPC.start();

        try {
            waitingWPC.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare la partita");
        }
    }

    private void chooseWpcPhase() {
        boolean ok;
        do {
            displayText("Seleziona la wpc che vuoi utilizzare");
            String wpcID = userInput();
            ok = controller.pickWpc(wpcID);
        } while (!ok);
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
        StringBuilder str = new StringBuilder();
        str.append("Le tue wpc sono: ");

        for (String wpcID : userWpcs){
            str.append(wpcID + "\t");
        }

        wpcExtracted = true;
        displayText(str.toString());
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        displayText(notification.username + " ha scelto la wpc: " + notification.wpcID);
    }


}
