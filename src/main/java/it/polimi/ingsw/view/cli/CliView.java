package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.model.constants.CliConstants;

import java.util.*;

public class CliView implements Observer, NotificationHandler {
    private Scanner fromKeyBoard;
    private final CliRender cliRender;

    private boolean interruptThread = true;

    private Timer timer = new Timer();
    private Task task = null;
    private final Object waiter = new Object();
    private final Object prova = new Object();       //TODO: Attualmente utilizzato in stopHere, da eliminare

    private CliStatus state;

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

    public void changeState(NextAction action) {
        state = CliStatus.getCliState(action);
    }

    //---------------------------- External methods ----------------------------
    public void launch(){
        state = CliStatus.LOG_PHASE;
        start();
    }

    private void start(){
        boolean quit = false;
        while (!quit) {
            switch (state) {
                case QUIT_SAGRADA:
                    quit = true;
                    //TODO
                    break;
                case UNKNOWN:
                    unknownPhase();
                    break;
                case LOG_PHASE:
                    logPhase();
                    break;
                case LOGIN:
                    log(true);
                    break;
                case CREATE_ACCOUNT:
                    log(false);
                    break;
                case DISPLAY_STAT:
                    //TODO
                    break;
                case LOGOUT:
                    //TODO
                    break;
                case MAIN_MENU_PHASE:
                    mainMenuPhase();
                    break;
                case FIND_GAME_PHASE:
                    findGamePhase();
                    break;
                case START_GAME_PHASE:
                    startGamePhase();
                    break;
                case ANOTHER_PLAYER_TURN:
                    waitForTurn();
                    break;
                case MENU_ALL:
                    showMenuAll();
                    break;
                case MENU_ONLY_PLACEDICE:
                    showMenuOnlyPlaceDice();
                    break;
                case MENU_ONLY_TOOLCARD:
                    showMenuOnlyToolcard();
                    break;
                case MENU_ONLY_ENDTURN:
                    showMenuOnlyEndturn();
                    break;
                case INTERRUPT_TOOLCARD:
                    //TODO
                    break;
                case SELECT_DICE_TOOLCARD:
                    //TODO
                    break;
                case SELECT_NUMBER_TOOLCARD:
                    //TODO
                    break;
                case SELECT_DICE_TO_ACTIVE_TOOLCARD:
                    //TODO
                    break;
            }
        }
    }

    private void logPhase(){
        while (true) {
            displayText(CliConstants.CHOOSE_LOG_TYPE);
            String answer = userInput();
            if (answer.equals(CliConstants.YES_RESPONSE)) {
                state = CliStatus.LOGIN;
                return;
            };
            if (answer.equals(CliConstants.NO_RESPONSE)){
                state = CliStatus.CREATE_ACCOUNT;
                return;
            }
        }
    }

    private void log(boolean login){
        String user = null;

        do {
            if (login) displayText(CliConstants.LOGIN_PHASE);
            else displayText(CliConstants.CREATE_USER_PHASE);

            displayText(CliConstants.INSERT_USERNAME);
            String username = userInput();
            if (username.equals(CliConstants.ESCAPE_RESPONSE)){
                state = state.getPrevious();
                return;
            }

            displayText(CliConstants.INSERT_PASS);
            String password = userInput();

            if (login) user = controller.login(username, password);
            else user = controller.createUser(username, password);
        } while (user == null);

        displayText(CliConstants.LOG_SUCCESS + user);
        state = CliStatus.MAIN_MENU_PHASE;
    }

    private void mainMenuPhase() {
        displayText(CliConstants.PRESENT_MAIN_MENU);
        String response = userInput();
        switch (response){
            case "1":
                state = CliStatus.FIND_GAME_PHASE;
                break;
            case "2":
                state = CliStatus.DISPLAY_STAT;
                break;
            case "3":
                state = CliStatus.LOGOUT;
                break;
            default:
                break;
        }
    }

    private void findGamePhase(){
        String response;
        int numPlayers;

        displayText("Seleziona numero giocatori: (digita 'back' per tornare indietro)");
        response = userInput();

        if (response.equals(CliConstants.ESCAPE_RESPONSE)){
            state = CliStatus.MAIN_MENU_PHASE;
            return;
        }

        try {
            numPlayers = Integer.parseInt(response);
            int i = controller.findGame(numPlayers);
            if (i < 0){
                displayText("Impossibile trovare partita. Riprovare più tardi");
                state = CliStatus.MAIN_MENU_PHASE;
            } else {
                state = CliStatus.START_GAME_PHASE;
            }
        } catch (NumberFormatException e){
            displayText("Perfavore inserire un numero intero");
        }
    }

    private void startGamePhase() {
        waitFor("In attesa di altri giocatori...", ObjectToWaitFor.PLAYERS);
        waitFor("Attendo che la partita inizi...", ObjectToWaitFor.GAME);
        waitFor("Attendo i private objectives...", ObjectToWaitFor.PRIVATE_OBJS);
        waitFor("Attendo le wpc...", ObjectToWaitFor.WPCS);
        chooseWpcPhase();
        waitFor("In attesa delle toolcard...", ObjectToWaitFor.TOOLCARDS);
        waitFor("In attesa degli obbiettivi pubblici...", ObjectToWaitFor.POC);
        state = CliStatus.UNKNOWN;
    }

    private void unknownPhase() {
        synchronized (waiter){
            try {
                while (state.equals(CliStatus.UNKNOWN))  waiter.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitFor(String message, ObjectToWaitFor obj){
        synchronized (waiter){
            try {
                if (!obj.isArrived(controller)) waiter.wait(800);
                if (!obj.isArrived(controller)) displayText(message);
                while (!obj.isArrived(controller))  waiter.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopWaiting(){
        synchronized (waiter){
            waiter.notifyAll();
        }
    }

    private void chooseWpcPhase() {
        Thread requestWpcThread = new Thread(() -> {
            do {
                interruptThread = true;
                displayText("Seleziona la wpc che vuoi utilizzare");
                String wpcID = userInput();
                interruptThread = false;
                controller.pickWpc(wpcID);
            } while (controller.getMyWpc() == null);
            if (!controller.allPlayersChooseWpc()) displayText("Attendo che gli altri giocatori selezionino la wpc");
        });
        requestWpcThread.start();

        synchronized (waiter){
            try {
                int lastTimeLeft = task.timeLeft();
                int nextStep = 50;

                while (!controller.allPlayersChooseWpc()) {
                    if (task.timeLeft() <= nextStep && lastTimeLeft > nextStep) {
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

                if (interruptThread) requestWpcThread.interrupt();
                deleteTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForTurn() {
        waitFor("In attesa del mio turno...", ObjectToWaitFor.TURN);
    }

    private void placeDice() {
        String response;
        NextAction nextAction = null;

        do {
            try {
                displayText("Inserisci l'ID del dado da posizionare (digita 'back' per annullare la mossa)");
                if ((response = userInput()).equals(CliConstants.ESCAPE_RESPONSE)) return;
                int id = Integer.parseInt(response);

                displayText("Inserisci la colonna in cui posizionarlo (digita 'back' per annullare la mossa)");
                if ((response = userInput()).equals(CliConstants.ESCAPE_RESPONSE)) return;
                int col = Integer.parseInt(response);

                displayText("Inserisci la riga in cui posizionarlo (digita 'back' per annullare la mossa)");
                if ((response = userInput()).equals(CliConstants.ESCAPE_RESPONSE)) return;
                int row = Integer.parseInt(response);

                nextAction = controller.placeDice(id, col, row);
            } catch (NumberFormatException e){
                displayText("Inserire un numero intero");
                nextAction = null;
            }
        } while (nextAction == null);
        state = CliStatus.getCliState(nextAction);
    }

    private void showStartTurnInfo(){
        displayText("Questa è la tua wpc");
        displayText("Favours rimasti: " + controller.getFavour() + "\n"
                + cliRender.renderWpc(controller.getMyWpc(), false));

        displayText("Dadi estratti:");
        System.out.println("\n" + cliRender.renderDices(controller.getExtractedDices()) + "\n");
    }

    private enum MenuAction{PLACE_DICE, USE_TOOLCARD, END_TURN;}

    private void showMenuAll() {
        showStartTurnInfo();

        MenuAction[] possibleActions = {MenuAction.PLACE_DICE, MenuAction.USE_TOOLCARD, MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            displayText("1) posiziona dado");
            displayText("2) usa toolcard");
            displayText("3) passa turno");
        } while (!listenToResponseAndPerformAction(possibleActions));
    }

    private void showMenuOnlyPlaceDice(){
        showStartTurnInfo();

        MenuAction[] possibleActions = {MenuAction.PLACE_DICE, MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            displayText("1) posiziona dado");
            displayText("2) passa turno");
        } while (!listenToResponseAndPerformAction(possibleActions));
    }

    private void showMenuOnlyToolcard(){
        showStartTurnInfo();

        MenuAction[] possibleActions = {MenuAction.USE_TOOLCARD, MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            displayText("1) usa toolcard");
            displayText("2) passa turno");
        } while (!listenToResponseAndPerformAction(possibleActions));
    }

    private void showMenuOnlyEndturn(){
        showStartTurnInfo();

        MenuAction[] possibleActions = {MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            displayText("1) passa turno");
        } while (!listenToResponseAndPerformAction(possibleActions));
    }

    //Restituisce true se è stato possibile eseguire la mossa
    private boolean listenToResponseAndPerformAction(MenuAction[] possibleActions){
        String response = userInput();
        try {
            int action = Integer.parseInt(response) - 1;
            if (action >= 0 && action < possibleActions.length) {
                return performMenuAction(possibleActions[action]);
            }
            else {
                displayText("Comando non riconosciuto");
                return false;
            }
        } catch (NumberFormatException e){
            displayText("Perfavore inserire il numero dell'azione che si vuole eseguire");
            return false;
        }
    }

    //Restituisce true se l'azione è stata compiuta
    private boolean performMenuAction(MenuAction action){
        //TODO: fare in modo che restituisca false quando qualcosa non è andato a buon fine
        switch (action){
            case PLACE_DICE:
                placeDice();
                return true;
            case USE_TOOLCARD:
                displayText("Usata toolcard");
                return true;
            case END_TURN:
                System.out.println("\n");
                return controller.passTurn();
            default:
                displayText("Passata azione non standard");
                return false;
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
        displayText("Partita iniziata");
        stopWaiting();
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
        stopWaiting();
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

        ClientColor[] colors = controller.getPrivateObjectives();
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append("I tuoi private objective sono: ");
        else str.append("Il tuo private objective è: ");

        for (ClientColor color : colors){
            str.append(color + "\t");
        }

        displayText(str.toString());

        startNewTask(notification.timeToCompleteTask);
        stopWaiting();
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        displayText(notification.username + " ha scelto la wpc: " + notification.wpc.getWpcID());
        if (controller.allPlayersChooseWpc()){
            displayText("Tutti i giocatori hanno scelto la wpc");
            stopWaiting();
        }
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        ArrayList<ClientToolCard> cards = controller.getToolcards();
        System.out.println("\n\n");
        displayText("Le toolcards della partita sono:\n");

        for (ClientToolCard card : cards) {
            System.out.println("ID: " + card.getId());
            System.out.println("Nome: " + card.getName());
            System.out.println("Descrizione: " + card.getDescription() + "\n");
        }

        stopWaiting();
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        ArrayList<ClientPoc> cards = controller.getPOC();
        System.out.println("\n");
        displayText("Gli obbiettivi pubblici della partita sono:\n");

        for (ClientPoc card : cards) {
            System.out.println("ID: " + card.getId());
            System.out.println("Nome: " + card.getName());
            System.out.println("Descrizione: " + card.getDescription() + "\n");
        }
        System.out.println("\n");

        stopWaiting();
    }

    @Override
    public void handle(NewRoundNotification notification) {
//        displayText("Round: " + notification.roundNumber);
    }

    @Override
    public void handle(NextTurnNotification notification) {
        displayText("Turno: " + notification.turnNumber + "\tRound: "
                + controller.getCurrentRound() + "\tGiocatore attivo: " + notification.activeUser);

        if(!controller.isActive()) {
            displayText("Dadi presenti: ");
            System.out.println("\n" + cliRender.renderDices(controller.getExtractedDices()));
        }

        synchronized (waiter){
            if (controller.isActive()) state = CliStatus.MENU_ALL;
            else state = CliStatus.ANOTHER_PLAYER_TURN;
            stopWaiting();
        }
    }

    @Override
    public void handle(DiceChangedNotification notification) {

    }

    @Override
    public void handle(DicePlacedNotification notification) {
        System.out.println("\n" + cliRender.renderWpc(notification.wpc, false));
        displayText(notification.username + " ha posizionato il dado " + notification.dice.getDiceID() +
                " in posizione (" + notification.position.getColumn() + ", " + notification.position.getRow() + ")");
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

    @Override
    public void handle(ScoreNotification notification) {

    }
}
