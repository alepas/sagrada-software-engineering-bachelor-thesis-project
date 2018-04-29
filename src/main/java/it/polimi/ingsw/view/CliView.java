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
        displayText(CliConstants.LOGIN_PHASE);

        do {
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
        displayText(CliConstants.CREATE_USER_PHASE);

        do {
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
}
