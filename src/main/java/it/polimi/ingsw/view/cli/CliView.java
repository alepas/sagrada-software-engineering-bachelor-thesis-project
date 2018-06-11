package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.CliController;
import it.polimi.ingsw.control.network.commands.notifications.*;
import it.polimi.ingsw.model.clientModel.*;
import it.polimi.ingsw.view.Status;

import java.util.*;

public class CliView implements Observer, NotificationHandler {
    private Scanner fromKeyBoard;
    private final CliRender cliRender;
    private int strNum = 0;

    private boolean interruptThread = true;

    private Timer timer = new Timer();
    private Task task = null;
    private final Object waiter = new Object();
    private final Object prova = new Object();       //TODO: Attualmente utilizzato in stopHere, da eliminare

    private Status state;

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

    public void displayText(String text) { System.out.println(">>> " + text); }

    public void printText(String text) { System.out.println(text); }

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
        state = state.change(action);
    }

    //---------------------------- External methods ----------------------------
    public void launch(){
        state = Status.LOG_PHASE;
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
    }

    private void logPhase(){
        while (true) {
            displayText(CliConstants.CHOOSE_LOG_TYPE);
            String answer = userInput();
            if (answer.equals(CliConstants.YES_RESPONSE)) {
                state = Status.LOGIN;
                return;
            };
            if (answer.equals(CliConstants.NO_RESPONSE)){
                state = Status.CREATE_ACCOUNT;
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
        state = Status.MAIN_MENU_PHASE;
    }

    private void mainMenuPhase() {
        displayText(CliConstants.PRESENT_MAIN_MENU);
        String response = userInput();
        switch (response){
            case "1":
                state = Status.FIND_GAME_PHASE;
                break;
            case "2":
                state = Status.DISPLAY_STAT;
                break;
            case "3":
                state = Status.LOGOUT;
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

        state = Status.MAIN_MENU_PHASE;
    }

    private void findGamePhase(){
        String response;
        int numPlayers;

        displayText("Seleziona numero giocatori: (digita 'back' per tornare indietro)");
        response = userInput();

        if (response.equals(CliConstants.ESCAPE_RESPONSE)){
            state = Status.MAIN_MENU_PHASE;
            return;
        }

        try {
            numPlayers = Integer.parseInt(response);
            int i = controller.findGame(numPlayers);
            if (i < 0){
                displayText("Impossibile trovare partita. Riprovare più tardi");
                state = Status.MAIN_MENU_PHASE;
            } else {
                state = Status.START_GAME_PHASE;
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
        state = Status.UNKNOWN;
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

    private void showStartTurnInfo(){
        displayText("Questa è la tua wpc");
        displayText("Favours rimasti: " + controller.getFavour() + "\n"
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

    private void showMenuAll() {
        showStartTurnInfo();

        MenuAction[] possibleActions = { MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                    MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
                    MenuAction.PLACE_DICE, MenuAction.USE_TOOLCARD, MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            int i = showStandardActions();
            displayText(++i + ") Posiziona dado");
            displayText(++i + ") Usa toolcard");
            displayText(++i + ") Passa turno");
        } while (!listenToResponseAndPerformAction(possibleActions));
    }

    private void showMenuOnlyPlaceDice(){
        showStartTurnInfo();

        MenuAction[] possibleActions = { MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
                MenuAction.PLACE_DICE, MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            int i = showStandardActions();
            displayText(++i + ") Posiziona dado");
            displayText(++i + ") Passa turno");
        } while (!listenToResponseAndPerformAction(possibleActions));
    }

    private void showMenuOnlyToolcard(){
        showStartTurnInfo();

        MenuAction[] possibleActions = { MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
                MenuAction.USE_TOOLCARD, MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            int i = showStandardActions();
            displayText(++i + ") Usa toolcard");
            displayText(++i + ") Passa turno");
        } while (!listenToResponseAndPerformAction(possibleActions));
    }

    private void showMenuOnlyEndturn(){
        showStartTurnInfo();

        MenuAction[] possibleActions = { MenuAction.GET_PRIVATE_OBJ, MenuAction.GET_POCS,
                MenuAction.GET_TOOLS, MenuAction.SEE_ALL_WPCS, MenuAction.SEE_ROUND_TRACK,
                MenuAction.END_TURN};

        do{
            displayText("Cosa vuoi fare?");
            int i = showStandardActions();
            displayText(++i + ") Passa turno");
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
                return controller.passTurn();

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
                if ((response = userInput()).equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int id = Integer.parseInt(response);

                displayText("Inserisci la riga in cui posizionarlo (digita 'back' per annullare la mossa)");
                if ((response = userInput()).equals(CliConstants.ESCAPE_RESPONSE)) return false;
                int row = Integer.parseInt(response)-strNum;
                
                displayText("Inserisci la colonna in cui posizionarlo (digita 'back' per annullare la mossa)");
                if ((response = userInput()).equals(CliConstants.ESCAPE_RESPONSE)) return false;
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
            nextAction = controller.useToolcard(userInput());
        } while (nextAction == null);

        changeState(nextAction);
    }

    private void pickDiceForToolCard() {
        NextAction nextAction = null;
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();

        do {
            int id = pickDice(info.wherePickNewDice);
            nextAction = controller.pickDiceForToolCard(id);
        } while (nextAction == null);

        changeState(nextAction);
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

        do {
            try {
                displayText("Seleziona dalla wpc il dado da utilizzare");

                printText(cliRender.renderWpc(wpc, false));

                displayText("Inserisci la riga del dado");
                int row = Integer.parseInt(userInput())-strNum;

                displayText("Indica la colonna del dado");
                int col = Integer.parseInt(userInput())-strNum;

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
        do {
            try {
                printText(cliRender.renderDices(dices));

                displayText("Inserisci l'ID del dado da utilizzare");
                id = Integer.parseInt(userInput());

            } catch (NumberFormatException e) {
                displayText("Inserire un numero intero");
            }
        } while (id == -1);

        return id;
    }

    private Position selectWpcPosition(){
        ClientWpc wpc = controller.getMyWpc();

        do {
            try {
                printText(cliRender.renderWpc(wpc, false));

                displayText("Indica la riga in cui posizionare il dado");
                int row = Integer.parseInt(userInput())-strNum;

                displayText("Indica la colonna in cui posizionare il dado");
                int col = Integer.parseInt(userInput())-strNum;

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

        do {
            if (info.wherePutNewDice.equals(ClientDiceLocations.WPC)) pos = selectWpcPosition();
            nextAction = controller.placeDiceForToolCard(info.diceChosenId, pos);
        } while (nextAction == null);

        changeState(nextAction);
    }

    private void selectNumberForToolcard() {
        ToolCardClientNextActionInfo info = controller.getToolcardNextActionInfo();
        NextAction nextAction = null;
//        ClientDice dice = info.dice();

        StringBuilder str = new StringBuilder();
        for(int num : info.numbersToChoose){
            str.append(num).append("\t");
        }

        do {
            displayText(str.toString());
            displayText("Scegli il nuovo numero tra quelli mostrati sopra");

            try {
                int num = Integer.parseInt(userInput());
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
        displayText("Turno: " + notification.turnNumber + "\tRound: "
                + controller.getCurrentRound() + "\tGiocatore attivo: " + notification.activeUser);

        if(!controller.isActive()) {
            displayText("Dadi presenti: ");
            printText("\n" + cliRender.renderDices(controller.getExtractedDices()));
        }

        synchronized (waiter){
            if (controller.isActive()) state = Status.MENU_ALL;
            else state = Status.ANOTHER_PLAYER_TURN;
            stopWaiting();
        }
    }

    @Override
    public void handle(ToolCardDiceChangedNotification notification) {

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

    }

    @Override
    public void handle(PlayerSkipTurnNotification notification) {

    }

    @Override
    public void handle(ScoreNotification notification) {

    }

    @Override
    public void handle(ToolCardDicePlacedNotification toolCardDicePlacedNotification) {

    }

    @Override
    public void handle(ToolCardExtractedDicesModifiedNotification toolCardExtractedDicesModifiedNotification) {

    }
}
