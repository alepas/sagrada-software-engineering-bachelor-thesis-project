package it.polimi.ingsw.view;

import it.polimi.ingsw.control.socketNetworking.SocketClientController;

import java.util.Scanner;

public class CliView {
    private Scanner fromKeyBoard;

    // ----- The view is composed with the controller (strategy)
    private final SocketClientController controller;

    public CliView(SocketClientController controller) {
        this.controller = controller;
        this.fromKeyBoard = new Scanner(System.in);
    }

    private String userInput() {
        return fromKeyBoard.nextLine();
    }

    public void displayText(String text) {
        System.out.println(">>> " + text);
    }

    public void createUsernamePhase() {
        String nickname = null;
        do {
            displayText("Provide username:");
            String username = userInput();
            displayText("Provide password:");
            String password = userInput();

            nickname = controller.createUser(username, password);
        } while (nickname == null);

        displayText("You are connected as: " + nickname);
    }
}
