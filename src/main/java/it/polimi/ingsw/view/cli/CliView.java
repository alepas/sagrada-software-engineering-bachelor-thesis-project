package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.commands.NotificationHandler;
import it.polimi.ingsw.control.network.commands.responses.notifications.GameStartedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.Notification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PlayersChangedNotification;
import it.polimi.ingsw.control.network.commands.responses.notifications.PrivateObjExtractedNotification;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.constants.CliConstants;
import it.polimi.ingsw.model.dicebag.Color;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class CliView implements Observer, NotificationHandler {
    private Scanner fromKeyBoard;

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
            if (password.equals(CliConstants.ESCAPE_RESPONSE)) return;

            user = controller.login(username, password);
        } while (user == null);

        ClientModel.getInstance().setUsername(user);
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
            if (password.equals(CliConstants.ESCAPE_RESPONSE)) return;

            user = controller.createUser(username, password);
        } while (user == null);

        ClientModel.getInstance().setUsername(user);
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
                    String gameID = controller.findGame(numPlayers);
                    return (gameID != null);
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
        displayText("In attesa di altri giocatori...");
        Thread waitingPlayers = new Thread(
                () -> {
                    do {}
                    while (true);
                }
        );

        waitingPlayers.start();

        try {
            waitingPlayers.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare la partita");
        }

    }

    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) {
        ((Notification) arg).handle(this);
    }




    //-------------------------------- Notification Handler ------------------------------------

    @Override
    public void handle(GameStartedNotification response) {
        displayText("Partita iniziata");
    }

    @Override
    public void handle(PlayersChangedNotification response) {
        if (response.joined){
            displayText(response.username + " è entrato in partita.");
        } else {
            displayText(response.username + " è uscito dalla partita.");
        }
        displayText("Giocatori presenti: " + response.actualPlayers + " di " +
                response.numPlayers + " necessari.");
    }

    @Override
    public void handle(PrivateObjExtractedNotification response) {
        Color[] colors = response.colorsByUser.get(response.username);
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append("I tuoi private objective sono: ");
        else str.append("Il tuo private objective è: ");

        for (Color color : colors){
            str.append(color + "\t");
        }

        displayText(str.toString());
    }


}
