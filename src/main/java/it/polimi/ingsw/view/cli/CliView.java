package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.view.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CliView implements Observer, NotificationHandler {
    private BufferedReader fromKeyBoard;
    private final CliRender cliRender;
    private int strNum = 0;
    private boolean isInterruptable = true;
    private Thread currentThread;

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

    private void returnToPreviousState(){
        synchronized (waiter) {
            state = state.getPrevious();
            stateChanged = true;
            waiter.notifyAll();
        }
    }

    //---------------------------- External methods ----------------------------
    public boolean launch(){
        controller.addObserver(this);
        changeState(Status.LOG_PHASE);
        start();
        controller.removeObserver(this);
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
                        //TODO
                        break;
                    case RECONNECT:
                        quit = true;
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
                    case MENU_ONLY_PLACEDICE:
                    case MENU_ONLY_TOOLCARD:
                    case MENU_ONLY_ENDTURN:
                        showMenu();
                        break;

                    case INTERRUPT_TOOLCARD:
                        //TODO
                        break;

                    case SELECT_DICE_TOOLCARD:
                        pickDiceForToolCard();
                        break;

                    case SELECT_NUMBER_TOOLCARD:
                        selectNumberForToolcard();
                        break;

                    case SELECT_DICE_TO_ACTIVE_TOOLCARD:
                        //TODO
                        break;

                    case PLACE_DICE:
                        //TODO: ha senso???
                        break;

                    case PLACE_DICE_TOOLCARD:
                        placeDiceForToolcard();
                        break;
                }
            }
        });
        currentThread.start();
        try {
            currentThread.join();
        } catch (InterruptedException e) {/*Do nothing*/}
    }


    //----------------------------------------- PRE-GAME -----------------------------------------
    private void logPhase(){
        while (true) {
            displayText(CliConstants.CHOOSE_LOG_TYPE);
            String answer = userInput();

            if (answer == null) return;
            if (answer.equals("quit")) {
                changeState(Status.QUIT_SAGRADA);
                return;
            };
            if (answer.equals(CliConstants.YES_RESPONSE)) {
                changeState(Status.LOGIN);
                return;
            };
            if (answer.equals(CliConstants.NO_RESPONSE)){
                changeState(Status.CREATE_ACCOUNT);
                return;
            }
            displayText("Scrivi 'quit', '" + CliConstants.YES_RESPONSE + "' oppure '" + CliConstants.NO_RESPONSE + "'");
        }
    }

    private void log(boolean login){
        String user = null;

        do {
            if (login) displayText(CliConstants.LOGIN_PHASE);
            else displayText(CliConstants.CREATE_USER_PHASE);

            displayText(CliConstants.INSERT_USERNAME);
            String username = userInput();
            if (username == null) return;

            if (username.equals(CliConstants.ESCAPE_RESPONSE)){
                returnToPreviousState();
                return;
            }

            displayText(CliConstants.INSERT_PASS);
            String password = userInput();
            if (password == null) return;

            if (login) user = controller.login(username, password);
            else user = controller.createUser(username, password);
        } while (user == null);

        displayText(CliConstants.LOG_SUCCESS + user);
        changeState(Status.MAIN_MENU_PHASE);
    }

    private void mainMenuPhase() {
        displayText(CliConstants.PRESENT_MAIN_MENU);
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

        printText("");
        displayText("Username: " + user.getUsername());
        displayText("Partite vinte: " + user.getWonGames());
        displayText("Partite perse: " + user.getLostGames());
        displayText("Partite abbandonate: " + user.getAbandonedGames());
        displayText("Ranking: " + user.getRanking());
        printText("");

        changeState(Status.MAIN_MENU_PHASE);
    }

    private void findGamePhase(){
        String response;
        int numPlayers;

        displayText("Seleziona numero giocatori: (digita 'back' per tornare indietro)");
        response = userInput();
        if (response == null) return;

        if (response.equals(CliConstants.ESCAPE_RESPONSE)){
            changeState(Status.MAIN_MENU_PHASE);
            return;
        }

        try {
            numPlayers = Integer.parseInt(response);
            int i = controller.findGame(numPlayers);
            if (i < 0){
                displayText("Impossibile trovare partita. Riprovare più tardi");
                changeState(Status.MAIN_MENU_PHASE);
            } else {
                changeState(Status.START_GAME_PHASE);
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
        if (state.equals(Status.START_GAME_PHASE)) changeState(Status.UNKNOWN);;
    }

    private void unknownPhase() {
        synchronized (waiter){
            try {
                while (state.equals(Status.UNKNOWN))  waiter.wait();
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
                //TODO:
//                e.printStackTrace();
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
                displayText("Seleziona la wpc che vuoi utilizzare");
                String wpcID = userInput();     //Restituisce null se viene interrotto
                isInterruptable = false;
                if (wpcID != null) controller.pickWpc(wpcID);
                isInterruptable = true;
            } while (controller.getMyWpc() == null);
            if (!controller.allPlayersChooseWpc()) displayText("Attendo che gli altri giocatori selezionino la wpc");
        });
        requestWpcThread.start();

        synchronized (timerWaiter){
            try {
                int lastTimeLeft = task.timeLeft();
                int[] steps = {0, 5, 15, 30, 50};
                Integer timeLeft;

                while (!controller.allPlayersChooseWpc()) {
                    if ((timeLeft = hasPassedStep(steps, lastTimeLeft)) != null) {
                        if (timeLeft == 0) displayText("Tempo scaduto");
                        else displayText("Rimangono " + timeLeft + " secondi per scegliere le wpc");

                        lastTimeLeft = timeLeft;
                    }
                    timerWaiter.wait();
                }

                if (isInterruptable) requestWpcThread.interrupt();
                deleteTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        waitFor("In attesa del mio turno...", ObjectToWaitFor.TURN);
    }

    private void showStartTurnInfo(){
        displayText("Questa è la tua wpc");
        displayText("Favours rimasti: " + controller.getUserFavour() + "\n"
                + cliRender.renderWpc(controller.getMyWpc(), false));

        displayText("Dadi estratti:");
        printText("\n" + cliRender.renderDices(controller.getExtractedDices()) + "\n");
    }

    private enum MenuAction{GET_PRIVATE_OBJ, GET_POCS, GET_TOOLS, SEE_ROUND_TRACK, SEE_ALL_WPCS,
        PLACE_DICE, USE_TOOLCARD, END_TURN;}

    private int showStandardActions(){
        displayText("1) Visualizza Obiettivo Privato");
        displayText("2) Visualizza Obiettivi Pubblici");
        displayText("3) Visualizza Carte Utensile");
        displayText("4) Visualizza RoundTrack");
        displayText("5) Visualizza tutte le wpc");
        printText("");

        return 5;
    }

    private MenuAction[] getActionsForState() {
        switch (state) {
            case MENU_ALL:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
                        MenuAction.PLACE_DICE, MenuAction.USE_TOOLCARD, MenuAction.END_TURN};
            case MENU_ONLY_PLACEDICE:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
                        MenuAction.PLACE_DICE, MenuAction.END_TURN};
            case MENU_ONLY_TOOLCARD:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
                        MenuAction.END_TURN};
            case MENU_ONLY_ENDTURN:
                return new MenuAction[] {MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                        MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
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
                    displayText("Cosa vuoi fare?");
                    int i = showStandardActions();
                    while (i < possibleActions.length) {
                        if (possibleActions[i].equals(MenuAction.PLACE_DICE)){
                            displayText(++i + ") Posiziona dado");
                            continue;
                        }
                        if (possibleActions[i].equals(MenuAction.USE_TOOLCARD)){
                            displayText(++i + ") Usa toolcard (Favours rimasti: " + controller.getUserFavour() + ")");
                            continue;
                        }
                        if (possibleActions[i].equals(MenuAction.END_TURN)){
                            displayText(++i + ") Passa turno");
                            continue;
                        }
                        printText("Show menu error");
                    }
                    listenToResponseAndPerformAction(possibleActions);
                } while (!stateChanged);
            });
            playTurn.start();

            synchronized (waiter){
                try {
                    while (!stateChanged) waiter.wait();
                    if (playTurn.isAlive()) playTurn.interrupt();
                    playTurn.join();
                } catch (InterruptedException e){
                    //Do nothing
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
                    displayText("Comando non riconosciuto");
                }
            } catch (NumberFormatException e) {
                displayText("Perfavore inserire il numero dell'azione che si vuole eseguire");
            }
        }
        return false;
    }

    //Restituisce true se l'azione è stata compiuta
    private boolean performMenuAction(MenuAction action){
        //TODO: fare in modo che restituisca false quando qualcosa non è andato a buon fine
        switch (action){
            case GET_PRIVATE_OBJ:
                showPrivateObjectives();
                return true;

            case GET_POCS:
                showPocs();
                return true;

            case GET_TOOLS:
                showToolcards();
                return true;

            case SEE_ROUND_TRACK:
                //TODO
                displayText("Ancora da fare");
                return false;

            case SEE_ALL_WPCS:
                //TODO
                displayText("Ancora da fare");
                return false;

            case PLACE_DICE:
                return placeDice();

            case USE_TOOLCARD:
                useToolcard();
                return true;

            case END_TURN:
                printText("\n");
                if (controller.passTurn()) {
                    changeState(Status.UNKNOWN);
                    return true;
                }
                return false;

            default:
                displayText("Passata azione non standard");
                return false;
        }
    }

    private void showPrivateObjectives() {
        ClientColor[] colors = controller.getPrivateObjectives();
        StringBuilder str = new StringBuilder();

        if (colors.length > 1) str.append("I tuoi private objective sono: ");
        else str.append("Il tuo private objective è: ");

        for (ClientColor color : colors){
            str.append(color).append("\t");
        }

        displayText(str.toString());
    }

    private void showPocs() {
        ArrayList<ClientPoc> cards = controller.getPOC();
        printText("\n");
        displayText("Gli obbiettivi pubblici della partita sono:\n");

        for (ClientPoc card : cards) {
            printText("ID: " + card.getId());
            printText("Nome: " + card.getName());
            printText("Descrizione: " + card.getDescription() + "\n");
        }
        printText("\n");
    }

    private void showToolcards() {
        ArrayList<ClientToolCard> cards = controller.getToolcards();
        printText("\n");
        displayText("Le toolcards della partita sono:\n");

        for (ClientToolCard card : cards) {
            printText("ID: " + card.getId());
            printText("Nome: " + card.getName());
            printText("Descrizione: " + card.getDescription() + "\n");
        }
        printText("\n");
    }

    //-------------------------------------- Place Dice --------------------------------------

    private boolean placeDice() {
        String response;
        NextAction nextAction = null;

        do {
            try {
                displayText("Inserisci l'ID del dado da posizionare (digita 'back' per annullare la mossa)");
                if ((response = userInput()) == null) return false;
                if (response.equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int id = Integer.parseInt(response);

                displayText("Inserisci la riga in cui posizionarlo (digita 'back' per annullare la mossa)");
                if ((response = userInput()) == null) return false;
                if (response.equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int row = Integer.parseInt(response)-strNum;
                
                displayText("Inserisci la colonna in cui posizionarlo (digita 'back' per annullare la mossa)");
                if ((response = userInput()) == null) return false;
                if (response.equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int col = Integer.parseInt(response)-strNum;
                
                Position pos = new Position(row, col);

                nextAction = controller.placeDice(id, pos);
            } catch (NumberFormatException e){
                displayText("Inserire un numero intero");
                nextAction = null;
            }
        } while (nextAction == null);

        changeState(nextAction);
        return true;
    }


    //--------------------------------------- Toolcard ---------------------------------------

    private void useToolcard(){
        NextAction nextAction = null;

        do {
            showToolcards();
            displayText("Seleziona l'ID della toolcard da utilizzare");
            String response = userInput();
            if (response == null) return;
            nextAction = controller.useToolcard(response);
        } while (nextAction == null);

        changeState(nextAction);
    }

    private void pickDiceForToolCard() {
        NextAction nextAction = null;
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
        int id;

        do {
            id = pickDice(info.wherePickNewDice);
            if (id >= 0) nextAction = controller.pickDiceForToolCard(id);
        } while (id >= 0 && nextAction == null);

        if (nextAction != null) changeState(nextAction);
    }

    private int pickDice(ClientDiceLocations location){
        switch (location){
            case WPC:
                return pickDiceFromWpc();
            case EXTRACTED:
                return pickDiceFromDices(controller.getExtractedDices());
            case ROUNDTRACK:
                return pickDiceFromDices(controller.getRoundtrackDices());
            default:
                displayText("Non so come fare una pick dalla posizione passata");
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
                displayText("Seleziona dalla wpc il dado da utilizzare");

                printText(cliRender.renderWpc(wpc, false));

                displayText("Inserisci la riga del dado");
                if ((response = userInput()) == null) return -1;
                int row = Integer.parseInt(response)-strNum;

                displayText("Indica la colonna del dado");
                if ((response = userInput()) == null) return -1;
                int col = Integer.parseInt(response)-strNum;

                Position pos = new Position(row, col);

                id = getDiceIdFromWpc(wpc, pos);
                if (id == -1)displayText("Non è presente alcun dado nella posizione inserita o la posizione non esiste");

            } catch (NumberFormatException e) {
                displayText("Inserire un numero intero");
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

    private int pickDiceFromDices(ArrayList<ClientDice> dices){
        int id = -1;
        String response;
        do {
            try {
                printText(cliRender.renderDices(dices));

                displayText("Inserisci l'ID del dado da utilizzare");
                if ((response = userInput()) == null) return -1;
                id = Integer.parseInt(response);

            } catch (NumberFormatException e) {
                displayText("Inserire un numero intero");
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

                displayText("Indica la riga in cui posizionare il dado");
                if ((response = userInput()) == null) return null;
                int row = Integer.parseInt(response)-strNum;

                displayText("Indica la colonna in cui posizionare il dado");
                if ((response = userInput()) == null) return null;
                int col = Integer.parseInt(response)-strNum;

                Position pos = new Position(row, col);

                int id = getDiceIdFromWpc(wpc, pos);
                if (id == -1) return pos;
                else displayText("É gia presente un dado nella posizione (" + pos.getRow()
                    + ", " + pos.getColumn() + ")");

            } catch (NumberFormatException e) {
                displayText("Inserire un numero intero");
            }
        } while (true);
    }

    private void placeDiceForToolcard(){
        NextAction nextAction = null;
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
        Position pos = null;
        int id;

        //TODO: Mostrare dado scelto (in attesa che mi metta il dado e non l'id)
        if (info.diceChosen == null) id = pickDice(info.wherePickNewDice);
        else {
            displayText("Stai per posizionare il dado");
            printText(cliRender.renderDice(info.diceChosen));
            id = info.diceChosen.getDiceID(); //TODO: Mostra dado scelto
        }

        do {
            if (info.wherePutNewDice.equals(ClientDiceLocations.WPC)) {
                pos = selectWpcPosition();
                if (pos == null) return;
            }
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
            displayText("Scegli il nuovo numero tra quelli mostrati sopra");

            try {
                if ((response = userInput()) == null) return;
                int num = Integer.parseInt(response);
                if (!info.numbersToChoose.contains(num)) {
                    displayText("Inserire un numero tra quelli presenti");
                    continue;
                }

                nextAction = controller.pickNumberForToolcard(num);
            } catch (NumberFormatException e) {
                displayText("Inserire un numero intero");
            }
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
            if ( (i == userWpcs.size()-1) && (num == 0) ) printText(cliRender.renderWpc(wpcs[num], true));
            if (num == 1) printText(cliRender.renderWpcs(wpcs, CliConstants.WpcSpacing));;
        }

        showPrivateObjectives();

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
//        displayText("Round: " + notification.roundNumber);
    }

    @Override
    public void handle(NextTurnNotification notification) {
        printText("");
        displayText("Turno: " + notification.turnNumber + "\tRound: "
                + controller.getCurrentRound() + "\tGiocatore attivo: " + notification.activeUser);

        if(!controller.isActive()) {
            displayText("Dadi presenti: ");
            printText("\n" + cliRender.renderDices(controller.getExtractedDices()));
        }

        startNewTask(notification.timeToCompleteTask);
        if (turnThread != null) {
            turnThread.stop();
            deleteTask();
        }
        turnThread = new TurnThread(this, task, timerWaiter);
        (new Thread(turnThread)).start();

        synchronized (waiter){
            if (controller.isActive()) changeState(Status.MENU_ALL);
            else changeState(Status.ANOTHER_PLAYER_TURN);
            stopWaiting();
        }
    }

    @Override
    public void handle(DicePlacedNotification notification) {
        printText("\n" + cliRender.renderWpc(notification.wpc, false));
        displayText(notification.username + " ha posizionato il dado " + notification.dice.getDiceID() +
                " in posizione (" + (notification.position.getRow()+strNum) + ", " + (notification.position.getColumn()+strNum) + ")");
        if (notification.newExtractedDices != null) {
            displayText("I dadi nella riserva sono: \n");
            printText(cliRender.renderDices(notification.newExtractedDices));
        }
    }

    @Override
    public void handle(ToolCardUsedNotification notification) {
        if (!notification.username.equals(controller.getUser())){
            displayText(notification.username + " ha usato la toolcard " + notification.toolCard.getId()
                + " (" + notification.toolCard.getName() + ")");
            for(Notification not : notification.movesNotifications) not.handle(this);
        }
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {

    }

    @Override
    public void handle(ToolCardDicePlacedNotification toolCardDicePlacedNotification) {

    }

    @Override
    public void handle(ToolCardExtractedDicesModifiedNotification toolCardExtractedDicesModifiedNotification) {

    }

    @Override
    public void handle(PlayerDisconnectedNotification playerDisconnectedNotification) {

    }

    @Override
    public void handle(PlayerReconnectedNotification playerReconnectedNotification) {

    }

    @Override
    public void handle(ForceDisconnectionNotification notification) {
        printText("");
        if (notification.lostConnection) {
            displayText("Connessione con il server persa");
        } else {
            displayText("Hai effettuato il login da un altro dispositivo");
            displayText("Sei pertanto stato disconesso dalla sessione");
        }
        if (currentThread != null) currentThread.interrupt();
        clean();
    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }

    @Override
    public void handle(ScoreNotification notification) {

    }
}
