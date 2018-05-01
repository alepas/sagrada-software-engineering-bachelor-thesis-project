package it.polimi.ingsw.view;

import it.polimi.ingsw.control.ClientController;
import it.polimi.ingsw.model.constants.CliConstants;

import java.util.Scanner;

public class CliView {
    private Scanner fromKeyBoard;

    // ----- The view is composed with the controller (strategy)
    private final ClientController controller;

    public CliView(ClientController controller) {
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
        String nickname = null;

        do {
            displayText(CliConstants.LOGIN_PHASE);
            displayText(CliConstants.INSERT_USERNAME);
            String username = userInput();
            if (username.equals(CliConstants.ESCAPE_RESPONSE)) return;

            displayText(CliConstants.INSERT_PASS);
            String password = userInput();
            if (password.equals(CliConstants.ESCAPE_RESPONSE)) return;

            nickname = controller.login(username, password);
        } while (nickname == null);

        displayText(CliConstants.LOG_SUCCESS + nickname);
    }

    public void createUsername() {
        String nickname = null;

        do {
            displayText(CliConstants.CREATE_USER_PHASE);
            displayText(CliConstants.INSERT_USERNAME);
            String username = userInput();
            if (username.equals(CliConstants.ESCAPE_RESPONSE)) return;

            displayText(CliConstants.INSERT_PASS);
            String password = userInput();
            if (password.equals(CliConstants.ESCAPE_RESPONSE)) return;

            nickname = controller.createUser(username, password);
        } while (nickname == null);

        displayText(CliConstants.LOG_SUCCESS + nickname);
    }

    //------------------------------- ADD HERE ALL NEW METHODS -------------------------------

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
                    controller.findGame(numPlayers);
                    return true;
                } catch (Exception e){
                    displayText("Impossibile trovare partita. Riprovare più tardi");
                    return false;
                }
            } catch (NumberFormatException e){
                displayText("Perfavore inserire un numero intero");
            }
        } while (true);
    }

    private void playGame() {
        displayText("In attesa di altri giocatori...");

        displayText("\n>>>Da qui in poi è ancora da fare quindi non funziona più\n\n");
    }
}
