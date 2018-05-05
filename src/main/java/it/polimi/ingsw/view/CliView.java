package it.polimi.ingsw.view;

import it.polimi.ingsw.control.ClientController;
import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.model.constants.CliConstants;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.MultiplayerGame;

import java.rmi.RemoteException;
import java.util.Scanner;

public class CliView implements AbstractView {
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

        ClientContext.get().setUsername(user);
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

        ClientContext.get().setUsername(user);
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
                    Game game = controller.findGame(numPlayers);
                    if (game != null) {
                        game.addObserver(this);
                        displayText("Aggiunto alla partita: " + game.getID());
                    }
                    return (game != null);
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
        Thread waitingPlayers = new Thread(
                () -> {
                    do {}
                    while (!ClientContext.get().isGameStarted());
                }
        );

        waitingPlayers.start();

        try {
            waitingPlayers.join();
        } catch (InterruptedException e){
            displayText("Ho smesso di aspettare la partita");
        }

    }




    //------------------------------- Game observer -------------------------------

    @Override
    public void onJoin(String username) {
        displayText(username + " è entrato nella partita");
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        displayText("Giocatori in partita: " + game.getPlayers().size() + " di " +
                game.getNumPlayers() + " necessari");
    }

    @Override
    public void onLeave(String username) {
        displayText(username + " è uscito dalla partita");
        MultiplayerGame game = (MultiplayerGame) ClientContext.get().getCurrentGame();
        displayText("Giocatori in partita: " + game.getPlayers().size() + " di " +
                game.getNumPlayers() + " necessari");
    }

    @Override
    public void onGameStarted() throws RemoteException {
        displayText("La partita è iniziata");
    }
}
