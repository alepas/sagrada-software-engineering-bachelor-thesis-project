package client.view.cli.cli;

import client.constants.CliConstants;
import client.controller.CliController;
import client.view.Status;
import shared.clientInfo.*;
import shared.network.commands.notifications.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static client.constants.CliConstants.*;

public class CliView implements Observer, NotificationHandler {
    private BufferedReader fromKeyBoard;
    private final CliRender cliRender;
    private int strNum = CliConstants.COUNTER_START_NUMBER;
    private boolean isInterruptable = true;
    private Thread currentThread;
    private boolean relaunch = false;

    private Timer timer;
    private Task task = null;
    private boolean preventTaskDeletion = false;
    private TurnThread turnThread = null;
    private final Object waiter = new Object();
    private final Object timerWaiter = new Object();

    private Status state;
    private boolean stateChanged = false;

    // ----- The view is composed with the controller (strategy)
    private final CliController controller;

    public CliView(CliController controller) {
        this.controller = controller;
        this.fromKeyBoard = new BufferedReader(new InputStreamReader(System.in));
        this.cliRender = new CliRender();
    }

    private void clean(){
        isInterruptable = true;
        currentThread = null;
        if (task != null) task.stop();
        preventTaskDeletion = false;
        deleteTask();
        changeState(Status.RECONNECT);
    }

    //Restituisce null se c'è stata un'eccezione
    private String userInput() {
        try {
            // wait until we have data to complete a readLine()
            while (!fromKeyBoard.ready()) {
                Thread.sleep(200);
            }
            return fromKeyBoard.readLine();
        } catch (InterruptedException | IOException e) {
            return null;
        }
    }

    public void displayText(String text) { System.out.println(">>> " + text); }

    public void printText(String text) { System.out.println(text); }

    //Avvia un nuovo timer della durata di taskTime, notificando ogni volta che è trascorso un secondo
    private void startNewTask(int taskTime){
        preventTaskDeletion = (task != null);       /*Quando un thread viene cancellato si chiama la deleteTask
                                                      Se questa chiamata avvenisse dopo che la nuova task è stata
                                                      avviata troverebbe preventTaskDeletion a true e non la
                                                      eliminerebbe*/
        task = new Task(taskTime, timerWaiter);
        timer = new Timer();
        timer.schedule(task, 0, task.getSensibility());
    }

    //Avvia un nuovo timer della durata di taskTime, notificando quando passano 'sensibility' ms
    private void startNewTask(int taskTime, int sensibility){
        task = new Task(taskTime, sensibility, timerWaiter);
        timer.schedule(task, 0, task.getSensibility());
    }

    private void deleteTask() {
        if (preventTaskDeletion) {
            preventTaskDeletion = false;
        } else {
            timer = null;
            task = null;
        }
    }

    private void startNewTurnThread() {
        if (turnThread != null) turnThread.stop();
        turnThread = new TurnThread(this, task, timerWaiter);
        (new Thread(turnThread)).start();
    }

    private void changeState(NextAction action) {
        synchronized (waiter) {
            state = Status.change(action);
            stateChanged = true;
            waiter.notifyAll();
        }
    }

    private void changeState(Status status){
        synchronized (waiter) {
            state = status;
            stateChanged = true;
            waiter.notifyAll();
        }
    }

    private void returnToPreviousStateWhileNotInGame(){
        synchronized (waiter) {
            state = state.getPrevious();
            stateChanged = true;
            waiter.notifyAll();
        }
    }

    private void returnToMainMenu() {
        controller.exitGame();
        deleteTask();
        if (turnThread != null) turnThread.stop();
        printText(VOID_STRING);
        displayText(PRESS_ENTER_TO_MENU);
        while (userInput() == null);
        changeState(Status.MAIN_MENU_PHASE);
    }

    //---------------------------- External methods ----------------------------
    public boolean launch(){
        controller.addModelObserver(this);
        changeState(Status.LOG_PHASE);
        do {
            start();
        } while (relaunch);
        controller.removeModelObserver(this);
        return state.equals(Status.QUIT_SAGRADA);
    }

    private void start(){
        currentThread = new Thread(() -> {
            boolean quit = false;
            while (!quit) {
                stateChanged = false;
                switch (state) {
                    case QUIT_SAGRADA:
                        quit = true;
                        break;
                    case RECONNECT:
                        quit = true;
                        break;
                    case RETURN_TO_MAIN_MENU:
                        returnToMainMenu();
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
                        showStat();
                        break;

                    case LOGOUT:
                        controller.logout();
                        changeState(Status.RECONNECT);
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

                    case MENU_ALL: case MENU_ONLY_PLACEDICE: case MENU_ONLY_TOOLCARD: case MENU_ONLY_ENDTURN:
                        showMenu();
                        break;

                    case INTERRUPT_TOOLCARD:
                        interruptToolcard();
                        break;

                    case SELECT_DICE_TOOLCARD:
                        pickDiceForToolCard();
                        break;

                    case SELECT_NUMBER_TOOLCARD:
                        selectNumberForToolcard();
                        break;

                    case SELECT_DICE_TO_ACTIVE_TOOLCARD:
                        System.out.println("SCEGLI UN DADO PER ATTIVARE LA TOOLCARD. DEVE AVERE IL COLORE CORRETTO");
                        pickDiceForToolCard();
                        break;

                    case PLACE_DICE_TOOLCARD:
                        placeDiceForToolcard();
                        break;
                }
                relaunch = Thread.currentThread().isInterrupted();
                if (!quit) quit = Thread.currentThread().isInterrupted();
            }
        });
        currentThread.start();
        try {
            currentThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //----------------------------------------- PRE-GAME -----------------------------------------
    private void logPhase(){
        while (true) {
            displayText(CliConstants.CHOOSE_LOG_TYPE);
            String answer = userInput();

            if (answer == null) return;
            if (answer.equals(QUIT_RESPONSE)) {
                changeState(Status.QUIT_SAGRADA);
                return;
            };
            if (answer.equals(YES_RESPONSE)) {
                changeState(Status.LOGIN);
                return;
            };
            if (answer.equals(NO_RESPONSE)){
                changeState(Status.CREATE_ACCOUNT);
                return;
            }
            displayText(LOG_PHASE_INVALID_INPUT_ERROR_MESSAGE);
        }
    }

    private void log(boolean login){
        String user = null;

        do {
            if (login) displayText(CliConstants.LOGIN_PHASE);
            else displayText(CliConstants.CREATE_USER_PHASE);

            displayText(INSERT_USERNAME + PRESENT_RETURN_BACK);
            String username = userInput();
            if (username == null) return;

            if (username.equals(CliConstants.ESCAPE_RESPONSE)){
                returnToPreviousStateWhileNotInGame();
                return;
            }

            displayText(CliConstants.INSERT_PASS);
            String password = userInput();
            if (password == null) return;

            if (login) user = controller.login(username, password);
            else user = controller.createUser(username, password);
        } while (user == null);

        displayText(CliConstants.LOG_SUCCESS + user);

        NextAction nextAction = controller.checkIfInGame();
        if (controller.getGame() != null) resumeGame(nextAction);
        else changeState(Status.MAIN_MENU_PHASE);
    }

    private void resumeGame(NextAction nextAction) {
        ClientGame game = controller.getGame();
        displayText(RECONNECTED_TO_GAME + game.getId());

        startNewTask(controller.getTimeToCompleteTask());

        if (!controller.isGameStarted()) {
            changeState(Status.START_GAME_PHASE);
            return;
        }

        if (game.getWpcsProposedByUsername() != null){
            showPrivateObjectives();
            printWpcs(game.getWpcsProposedByUsername().get(controller.getUser()));
            changeState(Status.START_GAME_PHASE);
        } else {
            showPrivateObjectives();
            showPocs();
            showToolcards();

            switch (nextAction) {
                case WAIT_FOR_TURN:
                case MENU_ALL:
                case MENU_ONLY_PLACE_DICE:
                case MENU_ONLY_TOOLCARD:
                case MENU_ONLY_ENDTURN:
                    break;

                case PLACE_DICE_TOOLCARD:
                case SELECT_DICE_TOOLCARD:
                case INTERRUPT_TOOLCARD:
                case SELECT_NUMBER_TOOLCARD:
                case SELECT_DICE_TO_ACTIVATE_TOOLCARD:
                case CANCEL_ACTION_TOOLCARD:
                    showStartTurnInfo();
                    ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
                    if (info.diceChosen != null) {
                        displayText(WERE_PLACING_DICE_STRING);
                        printText(cliRender.renderDice(info.diceChosen));
                    }
                    if (info.wherePickNewDice != null) displayText(DICE_COME_FROM + info.wherePickNewDice);
                    if (info.wherePutNewDice != null)
                        displayText(DICE_SHOULD_BE_PUT + info.wherePutNewDice);
                    break;
            }

            startNewTurnThread();
            changeState(nextAction);
        }
    }

    private void mainMenuPhase() {
        displayText(PRESENT_MAIN_MENU);
        String response = userInput();
        if (response == null) return;

        switch (response){
            case "1":
                changeState(Status.FIND_GAME_PHASE);
                break;
            case "2":
                changeState(Status.DISPLAY_STAT);
                break;
            case "3":
                changeState(Status.LOGOUT);
                break;
            default:
                break;
        }
    }

    private void showStat() {
        ClientUser user = controller.getUserStat();

        printText(VOID_STRING);
        displayText(USERNAME + user.getUsername());
        displayText(WON_GAMES + user.getWonGames());
        displayText(LOST_GAMES + user.getLostGames());
        displayText(ABANDONED_GAMES + user.getAbandonedGames());
        displayText(RANKING + user.getRanking());
        printText(VOID_STRING);

        changeState(Status.MAIN_MENU_PHASE);
    }

    private void findGamePhase(){
        String response;
        int numPlayers;

        displayText(SELECT_NUM_PLAYERS + PRESENT_RETURN_BACK);
        response = userInput();
        if (response == null) return;

        if (response.equals(ESCAPE_RESPONSE)){
            changeState(Status.MAIN_MENU_PHASE);
            return;
        }

        try {
            numPlayers = Integer.parseInt(response);
            int i = controller.findGame(numPlayers);
            if (i < 0){
                displayText(IMPOSSIBLE_TO_FIND_GAME);
                changeState(Status.MAIN_MENU_PHASE);
            } else {
                changeState(Status.START_GAME_PHASE);
            }
        } catch (NumberFormatException e){
            displayText(INSERT_INT_NUMBER);
        }
    }

    private void startGamePhase() {
        waitFor(WAITING_PLAYERS, ObjectToWaitFor.PLAYERS);
        waitFor(WAITING_GAME, ObjectToWaitFor.GAME);
        waitFor(WAITING_PRIVATE_OBJECTIVE, ObjectToWaitFor.PRIVATE_OBJS);
        waitFor(WAITING_WPCS, ObjectToWaitFor.WPCS);
        chooseWpcPhase();
        waitFor(WAITING_TOOLCARD, ObjectToWaitFor.TOOLCARDS);
        waitFor(WAITING_POCS, ObjectToWaitFor.POC);
        if (state.equals(Status.START_GAME_PHASE)) changeState(Status.UNKNOWN);;
    }

    private void unknownPhase() {
        synchronized (waiter){
            try {
                while (state.equals(Status.UNKNOWN))  waiter.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void waitFor(String message, ObjectToWaitFor obj){
        synchronized (waiter){
            try {
                if (!obj.isArrived(controller)) waiter.wait(800);
                if (!obj.isArrived(controller)) displayText(message);
                while (!obj.isArrived(controller))  waiter.wait();
            } catch (NullPointerException e) {
                /*Do nothing*/
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
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
                displayText(SELECT_WPC);
                String wpcID = userInput();
                isInterruptable = false;
                if (wpcID != null) controller.pickWpc(wpcID);
                isInterruptable = true;
            } while (controller.getMyWpc() == null);
            if (!controller.allPlayersChooseWpc()) displayText(WAITING_PLAYERS_TO_SELECT_WPC);
        });
        requestWpcThread.start();

        synchronized (timerWaiter){
            try {
                int lastTimeLeft = task.timeLeft();
                int[] steps = WPC_WAITING_STEPS;
                Integer timeLeft;

                while (!controller.allPlayersChooseWpc()) {
                    if ((timeLeft = hasPassedStep(steps, lastTimeLeft)) != null) {
                        if (timeLeft == 0) displayText(TIMES_UP);
                        else displayText(TIME_LEFT_TO_CHOOSE_WPC_P1 + timeLeft + TIME_LEFT_TO_CHOOSE_WPC_P2);

                        lastTimeLeft = timeLeft;
                    }
                    timerWaiter.wait();
                }

                if (isInterruptable) requestWpcThread.interrupt();
                deleteTask();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //Se è stato superato uno degli step, restituisce lo step passato
    //Altrimenti null
    private Integer hasPassedStep(int[] steps, int lastTimeLeft){
        for(int step : steps){
            if (task.timeLeft() <= step && lastTimeLeft > step) return step;
        }
        return null;
    }

    private void waitForTurn() {
        waitFor(WAITING_TURN, ObjectToWaitFor.TURN);
    }

    private void showStartTurnInfo(){
        displayText(PRESENT_WPC);
        displayText(FAVOURS_LEFT + controller.getUserFavour() + RETURN
                + cliRender.renderWpc(controller.getMyWpc(), false));

        displayText(EXTRACTED_DICES);
        printText(RETURN + cliRender.renderDices(controller.getExtractedDices()) + RETURN);
    }

    private enum MenuAction{GET_PRIVATE_OBJ, GET_POCS, GET_TOOLS, SEE_ROUND_TRACK, SEE_ALL_WPCS,
        PLACE_DICE, USE_TOOLCARD, END_TURN;}

    private int showStandardActions(){
        int i = 1;
        displayText(i++ + SHOW_PRIVATE_OBJECTIVE);
        displayText(i++ + SHOW_POCS);
        displayText(i++ + SHOW_TOOLCARDS);
        displayText(i++ + SHOW_ROUNDTRACK);
        displayText(i + SHOW_ALL_WPCS);
        printText(VOID_STRING);

        return i;
    }

    private MenuAction[] getActionsForState() {
        switch (state) {
            case MENU_ALL:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ROUND_TRACK, MenuAction.SEE_ALL_WPCS,
                        MenuAction.PLACE_DICE, MenuAction.USE_TOOLCARD, MenuAction.END_TURN};
            case MENU_ONLY_PLACEDICE:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ROUND_TRACK, MenuAction.SEE_ALL_WPCS,
                        MenuAction.PLACE_DICE, MenuAction.END_TURN};
            case MENU_ONLY_TOOLCARD:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ROUND_TRACK, MenuAction.SEE_ALL_WPCS,
                        MenuAction.USE_TOOLCARD, MenuAction.END_TURN};
            case MENU_ONLY_ENDTURN:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ROUND_TRACK, MenuAction.SEE_ALL_WPCS,
                        MenuAction.END_TURN};
            default:
                return null;
        }
    }

    private void showMenu(){
        MenuAction[] possibleActions = getActionsForState();
        if (possibleActions != null){
            showStartTurnInfo();

            Thread playTurn = new Thread(() -> {
                do{
                    displayText(ASK_WHAT_TO_DO);
                    int i = showStandardActions();
                    while (i < possibleActions.length) {
                        if (possibleActions[i].equals(MenuAction.PLACE_DICE)){
                            displayText(++i + PLACE_DICE);
                            continue;
                        }
                        if (possibleActions[i].equals(MenuAction.USE_TOOLCARD)){
                            displayText(++i + USE_TOOLCARD + controller.getUserFavour() + CLOSE_PARENTHESIS);
                            continue;
                        }
                        if (possibleActions[i].equals(MenuAction.END_TURN)){
                            displayText(++i + END_TURN);
                        }
                    }
                    listenToResponseAndPerformAction(possibleActions);
                } while (!(stateChanged || state.equals(Status.RECONNECT) || state.equals(Status.RETURN_TO_MAIN_MENU)));
            });
            playTurn.start();

            synchronized (waiter){
                try {
                    while (!stateChanged) waiter.wait();
                    if (playTurn.isAlive()) playTurn.interrupt();
                    playTurn.join();
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    //Restituisce true se è stato possibile eseguire la mossa
    private boolean listenToResponseAndPerformAction(MenuAction[] possibleActions){
        String response = userInput();
        if (response != null) {
            try {
                int action = Integer.parseInt(response) - 1;
                if (action >= 0 && action < possibleActions.length) {
                    return performMenuAction(possibleActions[action]);
                } else {
                    displayText(COMMAND_NOT_RECOGNIZED);
                }
            } catch (NumberFormatException e) {
                displayText(INSERT_ACTION_NUMBER);
            }
        }
        return false;
    }

    //Restituisce true se l'azione è stata compiuta
    private boolean performMenuAction(MenuAction action){
        switch (action){
            case GET_PRIVATE_OBJ:
                showPrivateObjectives();
                printText(VOID_STRING);
                return true;

            case GET_POCS:
                showPocs();
                return true;

            case GET_TOOLS:
                showToolcards();
                return true;

            case SEE_ROUND_TRACK:
                printRoundtrack();
                return false;

            case SEE_ALL_WPCS:
                printText(RETURN);
                printText(cliRender.renderWpcs(controller.getWpcByUsername(), WPC_SPACING, controller.getGameUsers()));
                return false;

            case PLACE_DICE:
                return placeDice();

            case USE_TOOLCARD:
                return useToolcard();

            case END_TURN:
                printText(RETURN);
                if (controller.passTurn()) {
                    changeState(Status.UNKNOWN);
                    return true;
                }
                return false;

            default:
                displayText(NO_STANDARD_ACTION_PASSED);
                return false;
        }
    }

    private void printRoundtrack() {
        ClientDice[][] dicesByRound = controller.getRoundtrackDices();
        printText(VOID_STRING);

        ClientDice[][] diceInverted = new ClientDice[dicesByRound[0].length][dicesByRound.length];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                diceInverted[i][j] = dicesByRound[j][i];
            }
        }


        for(ClientDice[] dices : diceInverted)
            if (!allElementsAreNull(dices)) printText(cliRender.renderDices(dices));
    }

    private boolean allElementsAreNull(Object[] array) {
        for(Object obj : array) if (obj != null) return false;
        return true;
    }

    private void showPrivateObjectives() {
        ClientColor[] colors = controller.getPrivateObjectives();
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append(PRESENT_PRIVATE_OBJS);
        else str.append(PRESENT_PRIVATE_OBJ);

        for (ClientColor color : colors){
            str.append(color).append("\t");
        }

        displayText(str.toString());
    }

    private void showPocs() {
        ArrayList<ClientPoc> cards = controller.getPOC();
        printText(RETURN);
        displayText(PRESENT_POCS);

        for (ClientPoc card : cards) {
            printText(ID + card.getId());
            printText(NAME + card.getName());
            printText(DESCRIPTION + card.getDescription() + RETURN);
        }
        printText(RETURN);
    }

    private void showToolcards() {
        ArrayList<ClientToolCard> cards = controller.getToolcards();
        printText(RETURN);
        displayText(PRESENT_TOOLCARDS);

        for (ClientToolCard card : cards) {
            printText(ID + card.getId());
            printText(NAME + card.getName());
            printText(DESCRIPTION + card.getDescription() + "\n");
        }
        printText(RETURN);
    }

    private void printWpcs(ArrayList<ClientWpc> userWpcs){
        displayText(PRESENT_WPCS);

        ClientWpc[] wpcs = new ClientWpc[2];
        int num;

        for(int i = 0; i < userWpcs.size(); i++){
            num = i%2;
            wpcs[num] = userWpcs.get(i);
            if ( (i == userWpcs.size()-1) && (num == 0) ) printText(cliRender.renderWpc(wpcs[num], true));
            if (num == 1) printText(cliRender.renderWpcs(wpcs, WPC_SPACING));;
        }
    }

    //-------------------------------------- Place Dice --------------------------------------

    private boolean placeDice() {
        String response;
        NextAction nextAction = null;

        do {
            try {
                displayText(INSERT_DICE_ID_TO_PLACE + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return false;
                if (response.equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int id = Integer.parseInt(response);

                displayText(INSERT_ROW_TO_PLACE_DICE + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return false;
                if (response.equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int row = Integer.parseInt(response)-strNum;

                displayText(INSERT_COL_TO_PLACE_DICE + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return false;
                if (response.equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int col = Integer.parseInt(response)-strNum;

                Position pos = new Position(row, col);

                nextAction = controller.placeDice(id, pos);
            } catch (NumberFormatException e){
                displayText(INSERT_INT_NUMBER);
                nextAction = null;
            }
        } while (nextAction == null);

        changeState(nextAction);
        return true;
    }


    //--------------------------------------- Toolcard ---------------------------------------

    private boolean useToolcard(){
        NextAction nextAction = null;

        do {
            showToolcards();
            displayText(SELECT_TOOLCARD_ID + PRESENT_RETURN_BACK);
            String response = userInput();
            if (response == null) return false;
            if (response.equals(CliConstants.ESCAPE_RESPONSE)) return false;
            nextAction = controller.useToolcard(response);
        } while (nextAction == null);

        changeState(nextAction);
        return true;
    }

    private void pickDiceForToolCard() {
        NextAction nextAction = null;
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
        int id;

        do {
            id = pickDice(info.wherePickNewDice);
            if (id >= 0) nextAction = controller.pickDiceForToolCard(id);
        } while (!stateChanged && id >= 0 && nextAction == null);

        if (nextAction != null) changeState(nextAction);
    }

    private int pickDice(ClientDiceLocations location){
        switch (location){
            case WPC:
                return pickDiceFromWpc();
            case EXTRACTED:
                return pickDiceFromDices(controller.getExtractedDices(), false);
            case ROUNDTRACK:
                return pickDiceFromDices(null, true);
            default:
                displayText(CANT_DO_PICK_FROM_THIS_POSITION);
                displayText(location.toString());
                return -1;
        }
    }

    private int pickDiceFromWpc(){
        ClientWpc wpc = controller.getMyWpc();
        int id = -1;
        String response;

        do {
            try {
                displayText(SELECT_DICE_FROM_WPC);

                printText(cliRender.renderWpc(wpc, false));

                displayText(INSERT_DICE_ROW + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return -1;
                checkToolBack(response);
                if (stateChanged) return -1;
                int row = Integer.parseInt(response)-strNum;

                displayText(INSERT_DICE_COL + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return -1;
                checkToolBack(response);
                if (stateChanged) return -1;
                int col = Integer.parseInt(response)-strNum;

                Position pos = new Position(row, col);

                id = getDiceIdFromWpc(wpc, pos);
                if (id == -1)displayText(DICE_NOT_FOUND);

            } catch (NumberFormatException e) {
                displayText(INSERT_INT_NUMBER);
            }
        } while (id == -1);

        return id;
    }

    //Restituisce l'ID del dado presente nella posizione passata
    //Restituisce -1 se non è presente un dado nella posizione o se la posizione non esiste
    private int getDiceIdFromWpc(ClientWpc wpc, Position pos){
        for(ClientCell cell : wpc.getSchema()){
            if (cell.getCellPosition().equals(pos)) {
                if (cell.getCellDice() == null) break;
                else return cell.getCellDice().getDiceID();
            }
        }

        return -1;
    }

    private int pickDiceFromDices(ClientDice[] dices, boolean roundTrack){
        int id = -1;
        String response;
        do {
            try {
                if (roundTrack) printRoundtrack();
                else printText(cliRender.renderDices(dices));

                displayText(INSERT_DICE_ID_TO_USE + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return -1;
                checkToolBack(response);
                if (stateChanged) return -1;
                id = Integer.parseInt(response);

            } catch (NumberFormatException e) {
                displayText(INSERT_INT_NUMBER);
            }
        } while (id == -1);

        return id;
    }

    private Position selectWpcPosition(){
        ClientWpc wpc = controller.getMyWpc();
        String response;

        do {
            try {
                printText(cliRender.renderWpc(wpc, false));

                displayText(INSERT_ROW_TO_PLACE_DICE + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return null;
                checkToolBack(response);
                if (stateChanged) return null;

                int row = Integer.parseInt(response)-strNum;

                displayText(INSERT_COL_TO_PLACE_DICE + PRESENT_RETURN_BACK);
                if ((response = userInput()) == null) return null;
                checkToolBack(response);
                if (stateChanged) return null;
                int col = Integer.parseInt(response)-strNum;

                Position pos = new Position(row, col);

                int id = getDiceIdFromWpc(wpc, pos);
                if (id == -1) return pos;
                else displayText(DICE_ALREADY_PRESENT + pos.getRow()
                    + ", " + pos.getColumn() + CLOSE_PARENTHESIS);

            } catch (NumberFormatException e) {
                displayText(INSERT_INT_NUMBER);
            }
        } while (true);
    }

    private void checkToolBack(String response) {
        if (response.equals(CliConstants.ESCAPE_RESPONSE)) {
            NextAction nextAction = controller.cancelToolcardAction();
            if (nextAction != null) changeState(nextAction);
        }
    }

    private void placeDiceForToolcard(){
        NextAction nextAction = null;
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
        Position pos = null;
        int id;

        do {
            if (info.diceChosen == null) {
                id = pickDice(info.wherePickNewDice);
                if (stateChanged) return;
            }
            else {
                displayText(GOING_TO_PLACE_DICE);
                printText(cliRender.renderDice(info.diceChosen));
                id = info.diceChosen.getDiceID();
            }

            if (info.wherePutNewDice.equals(ClientDiceLocations.WPC)) {
                pos = selectWpcPosition();
                if (pos == null) return;
                if (stateChanged) return;
            }
            if (info.wherePutNewDice.equals(ClientDiceLocations.DICEBAG))
                displayText(GOING_TO_PLACE_DICE_INTO_DICEBAG);

            nextAction = controller.placeDiceForToolCard(id, pos);
        } while (nextAction == null);

        changeState(nextAction);
    }

    private void selectNumberForToolcard() {
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
        NextAction nextAction = null;
        String response;

        StringBuilder str = new StringBuilder();
        for(int num : info.numbersToChoose){
            str.append(num).append("\t");
        }

        do {
            displayText(str.toString());
            displayText(INCREMENT_DECREMENT_DICE + PRESENT_RETURN_BACK);

            try {
                if ((response = userInput()) == null) return;
                checkToolBack(response);
                if (stateChanged) return;
                int num = Integer.parseInt(response);
                if (!info.numbersToChoose.contains(num)) {
                    displayText(INSERT_NUMBER_FROM_PRESENTED);
                    continue;
                }

                nextAction = controller.pickNumberForToolcard(num);
            } catch (NumberFormatException e) {
                displayText(INSERT_INT_NUMBER);
            }
        } while (nextAction == null);

        changeState(nextAction);
    }

    private void interruptToolcard() {
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
        NextAction nextAction = null;

        do {
            if (info.diceChosen != null) {
                displayText(PRESENT_NEW_DICE);
                printText(cliRender.renderDice(info.diceChosen));
            }
            displayText(info.stringForStopToolCard);
            displayText(info.bothYesAndNo ?
                    INSERT_YES_OR_NO : PRESS_ENTER_TO_CONTINUE);
            if (info.showBackButton)
                displayText(PRESENT_RETURN_BACK);

            String response = userInput();

            if (response == null) return;
            if (response.equals(CliConstants.YES_RESPONSE)) nextAction = controller.interruptToolcard(ToolCardInterruptValues.YES);
            if (response.equals(CliConstants.NO_RESPONSE)) nextAction = controller.interruptToolcard(ToolCardInterruptValues.NO);
            if (response.equals(VOID_STRING)) nextAction = controller.interruptToolcard(ToolCardInterruptValues.OK);
            if (response.equals(CliConstants.ESCAPE_RESPONSE)) nextAction = controller.cancelToolcardAction();

        } while (nextAction == null);

        changeState(nextAction);
    }




    //-------------------------------------- Observer ------------------------------------------

    @Override
    public void update(Observable o, Object arg) {
        ((Notification) arg).handle(this);
    }




    //-------------------------------- Notification Handler ------------------------------------

    @Override
    public void handle(GameStartedNotification notification) {
        displayText(GAME_STARTED);
        stopWaiting();
    }

    @Override
    public void handle(PlayersChangedNotification notification) {
        if (notification.joined){
            displayText(notification.username + PLAYER_ENTER_GAME);
        } else {
            displayText(notification.username + PLAYER_EXIT_GAME);
        }
        displayText(IN_GAME_PLAYERS + notification.actualPlayers + OF +
                notification.numPlayers + NEEDED);
    }

    @Override
    public void handle(PrivateObjExtractedNotification notification) {
        stopWaiting();
    }

    @Override
    public void handle(WpcsExtractedNotification notification) {
        ArrayList<ClientWpc> userWpcs = notification.wpcsByUser.get(controller.getUser());
        printWpcs(userWpcs);

        showPrivateObjectives();

        startNewTask(notification.timeToCompleteTask);
        stopWaiting();
    }

    @Override
    public void handle(UserPickedWpcNotification notification) {
        displayText(notification.username + PLAYER_CHOSE_WPC + notification.wpc.getWpcID());
        if (controller.allPlayersChooseWpc()){
            displayText(ALL_PLAYERS_CHOOSE_WPC);
            stopWaiting();
        }
    }

    @Override
    public void handle(ToolcardsExtractedNotification notification) {
        showToolcards();
        stopWaiting();
    }

    @Override
    public void handle(PocsExtractedNotification notification) {
        showPocs();
        stopWaiting();
    }

    @Override
    public void handle(NewRoundNotification notification) {

    }

    @Override
    public void handle(NextTurnNotification notification) {
        printText(VOID_STRING);
        displayText(TURN + notification.turnNumber + ROUND
                + controller.getCurrentRound() + ACTIVE_PLAYER + notification.activeUser);

        if(!controller.isActive()) {
            displayText(EXTRACTED_DICES);
            printText(RETURN + cliRender.renderDices(controller.getExtractedDices()));
        }

        startNewTask(notification.timeToCompleteTask);
        startNewTurnThread();

        synchronized (waiter){
            if (controller.isActive()) changeState(Status.MENU_ALL);
            else changeState(Status.ANOTHER_PLAYER_TURN);
            stopWaiting();
        }
    }

    @Override
    public void handle(DicePlacedNotification notification) {
        printText(RETURN + cliRender.renderWpc(notification.wpc, false));
        displayText(notification.username + PLAYER_PLACED_DICE + notification.dice.getDiceID() +
                IN_POSITION + (notification.position.getRow()+strNum) + ", " + (notification.position.getColumn()+strNum) + CLOSE_PARENTHESIS);
        if (notification.newExtractedDices != null) {
            displayText(EXTRACTED_DICES);
            ClientDice[] extracted = new ClientDice[notification.newExtractedDices.size()];
            for(int i = 0; i < extracted.length; i++){
                extracted[i] = notification.newExtractedDices.get(i);
            }
            printText(cliRender.renderDices(extracted));
        }
    }

    @Override
    public void handle(ToolCardUsedNotification notification) {
        if (!notification.username.equals(controller.getUser())){
            displayText(notification.username + PLAYER_USED_TOOLCARD + notification.toolCard.getId()
                + " (" + notification.toolCard.getName() + CLOSE_PARENTHESIS);
            for(Notification not : notification.movesNotifications) not.handle(this);
        }
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {
        if (notification.oldPosition.equals(notification.newPosition)){
            displayText(notification.username + CHANGED_DICE + cliRender.renderDice(notification.oldDice)
                    + INTO_DICE + cliRender.renderDice(notification.newDice));
            displayText(POSITION + notification.oldPosition);
        } else {
            displayText(notification.username + PLAYER_REPLACED_DICE + cliRender.renderDice(notification.oldDice)
                    + PLACED_IN + notification.oldPosition + WITH_DICE + cliRender.renderDice(notification.newDice)
                    + PLACED_IN + notification.newPosition);
        }
    }

    @Override
    public void handle(ToolCardDicePlacedNotification notification) {
        displayText(notification.username + PLAYER_PLACED_DICE + notification.dice.getDiceID() +
                IN_POSITION + (notification.position.getRow()+strNum) + ", " + (notification.position.getColumn()+strNum) + CLOSE_PARENTHESIS);
    }

    @Override
    public void handle(ToolCardExtractedDicesModifiedNotification notification) {
        displayText(EXTRACTED_DICES_REPLACED);
        printText(cliRender.renderDices(controller.getExtractedDices()));
    }

    @Override
    public void handle(PlayerDisconnectedNotification notification) {
        printText(VOID_STRING);
        displayText(notification.username + PLAYER_DISCONNECTED);
    }

    @Override
    public void handle(PlayerReconnectedNotification notification) {
        printText(VOID_STRING);
        displayText(notification.username + PLAYER_RECONNECTED);
    }

    @Override
    public void handle(ForceDisconnectionNotification notification) {
        printText(VOID_STRING);
        if (notification.lostConnection) {
            displayText(LOST_CONNECTION);
        } else {
            displayText(LOGIN_FROM_ANOTHER_DEVICE);
            displayText(YOU_HAVE_BEEN_DISCONNECTED);
        }
        changeState(Status.RECONNECT);
        if (currentThread != null) currentThread.interrupt();
        clean();
    }

    @Override
    public void handle(ForceStartGameNotification notification) {
        displayText(TIME_TO_WAIT_PLAYER_ENDED);
    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {
        printText(VOID_STRING);
        String disconnectedString = notification.username + PLAYER_SKIPPED_TURN;
        String usedToolcardString = notification.username +
                PLAYER_SKIPPED_TURN_DUE_TO_TOOLCARD + notification.cardId;
        displayText(notification.disconnected ? disconnectedString : usedToolcardString);
    }

    @Override
    public void handle(ScoreNotification notification) {
        printText(VOID_STRING);
        displayText(GAME_ENDED);
        displayText(FINAL_TABLE);

        ArrayList<Map.Entry<String, Integer>> scores = new ArrayList<>(notification.scoreList.entrySet());
        scores.sort((Comparator<Map.Entry<?, Integer>>) (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        int i = 1;
        for (Map.Entry<String, Integer> entry : scores) {
            System.out.println(i++ + ") " + entry.getKey() + ": "
                    + entry.getValue() + POINTS);
        }

        changeState(Status.RETURN_TO_MAIN_MENU);
        currentThread.interrupt();
    }
}
