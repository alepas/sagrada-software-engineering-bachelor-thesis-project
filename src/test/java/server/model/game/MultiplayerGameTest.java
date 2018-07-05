package server.model.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.constants.GameConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.configLoader.ConfigLoader;
import server.model.dicebag.Color;
import server.model.users.DatabaseUsers;
import server.model.users.PlayerInGame;
import server.model.wpc.WpcDB;
import shared.clientInfo.ClientWpc;
import shared.exceptions.gameexceptions.InvalidMultiPlayerGamePlayersException;
import shared.exceptions.gameexceptions.MaxPlayersExceededException;
import shared.exceptions.gameexceptions.UserAlreadyInThisGameException;
import shared.exceptions.gameexceptions.UserNotInThisGameException;
import shared.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import shared.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static server.constants.GameConstants.*;

public class MultiplayerGameTest {

    private MultiplayerGame game;
    private String username1 = "John", username2 = "Alice", username3 = "Bob", username4 = "Eva";
    private ArrayList<String> users = new ArrayList<>(Arrays.asList(username1, username2, username3, username4));
    private int defaulNumPlayers = 3;
    private PlayerInGame player1;
    private PlayerInGame player2;
    private PlayerInGame player3;
    private PlayerInGame player4;

    @Before
    public void before() throws Exception{

        ConfigLoader.loadConfig();
        game = new MultiplayerGame(defaulNumPlayers);

        DatabaseUsers db = DatabaseUsers.getInstance();

        player1 = mock(PlayerInGame.class);
        when(player1.getUser()).thenReturn(username1);
        player2 = mock(PlayerInGame.class);
        when(player2.getUser()).thenReturn(username2);
        player3 = mock(PlayerInGame.class);
        player4 = mock(PlayerInGame.class);

        for(String user : users){
            try {
                db.registerUser(user, "");
            } catch (CannotRegisterUserException e){
                try {
                    db.login(user, "");
                } catch (CannotLoginUserException e1){/*Do nothing*/}
            }
        }
    }

    /**
     * Checks if the constructor creates a correct multi-player Game object
     */
    @Test
    public void checkMultiPlayerGameConstructor(){
        assertEquals(0, game.getToolCards().size());
        assertEquals(0, game.getPublicObjectiveCards().size());
        Assert.assertNotNull(game.getID());
        assertEquals(0, game.getExtractedDices().size());
        Assert.assertNotNull(game.getDiceBag());
        Assert.assertNotNull(game.getRoundTrack());
        assertEquals(defaulNumPlayers, game.numPlayers);
        assertEquals(defaulNumPlayers, game.players.length);
        Assert.assertTrue(game.getNumPlayers() >= MULTIPLAYER_MIN_NUM_PLAYERS &&
                game.getNumPlayers() <= MAX_NUM_PLAYERS);
        assertEquals(0, game.nextFree());
        assertEquals(NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME, game.numOfPrivateObjectivesForPlayer);
        assertEquals(NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME, game.numOfToolCards);
        assertEquals(NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME, game.numOfPublicObjectiveCards);
    }

    /**
     * @throws Exception if the given numPlayers is not valid, in this case it is higher than the nux number accepted
     */
    @Test(expected = InvalidMultiPlayerGamePlayersException.class)
    public void checkMultiPlayerGameConstructor2() throws Exception{
        Game game2 = new MultiplayerGame(GameConstants.MAX_NUM_PLAYERS+1);
    }

    /**
     * @throws Exception if the given numPlayers is not valid, in this case it is smaller than the min number accepted
     */
    @Test(expected = InvalidMultiPlayerGamePlayersException.class)
    public void checkMultiPlayerGameConstructor3() throws Exception{
        Game game2 = new MultiplayerGame(GameConstants.MULTIPLAYER_MIN_NUM_PLAYERS-1);
    }

    @Test
    public void addingAndRemovingPlayersTest() throws Exception {
        Boolean bool;
        assertEquals(0, game.numActualPlayers());

        //Aggiunta fino a 3 utenti
        Assert.assertFalse(game.addPlayer(username1));
        assertEquals(1, game.numActualPlayers());

        Assert.assertFalse(game.addPlayer(username2));
        assertEquals(2, game.numActualPlayers());

        Assert.assertTrue(game.addPlayer(username3));
        assertEquals(3, game.numActualPlayers());

        //Rimozione e aggiunta dell'utente 2
        game.removePlayer(username2);
        assertEquals(2, game.numActualPlayers());
        Assert.assertTrue(game.addPlayer(username2));
        assertEquals(3, game.numActualPlayers());

        //Rimozione di tutti gli utenti
        game.removePlayer(username1);
        assertEquals(2, game.numActualPlayers());
        game.removePlayer(username2);
        assertEquals(1, game.numActualPlayers());
        game.removePlayer(username3);
        assertEquals(0, game.numActualPlayers());
    }

    @Test(expected = MaxPlayersExceededException.class)
    public void illegalAddingAndRemovingPlayersTest1() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.addPlayer(username4);
    }

    @Test(expected = UserAlreadyInThisGameException.class)
    public void illegalAddingAndRemovingPlayersTest2() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username1);
    }

    /**
     * @throws Exception because it is trying to remove a player which is not in the array
     */
    @Test(expected = UserNotInThisGameException.class)
    public void removePlayerIllegalTest() throws Exception { game.removePlayer(username2); }

    /**
     * Tests if the is full methos works in a correct way
     */
    @Test
    public void checkIsFull(){
        Assert.assertFalse(game.isFull());

        game.players[0] = player1;
        Assert.assertEquals(1, game.numActualPlayers());
        Assert.assertFalse(game.isFull());

        game.players[1] = player2;
        Assert.assertEquals(2, game.numActualPlayers());
        Assert.assertFalse(game.isFull());

        game.players[2] = player3;
        Assert.assertEquals(3, game.numActualPlayers());
        Assert.assertTrue(game.isFull());

        game.players[2] = null;
        Assert.assertEquals(2, game.numActualPlayers());
        Assert.assertFalse(game.isFull());
    }

    /**
     *Tests if the Next Free method works in a correct way.
     */
    @Test
    public void nextFreeTest(){
        assertEquals(0, game.nextFree());

        game.players[0] = player1;
        assertEquals(1, game.nextFree());

        game.players[1] = player2;
        assertEquals(2, game.nextFree());

        game.players[2] = player3;
        assertEquals(-1, game.nextFree());

        game.players[2] = null;
        assertEquals(2, game.nextFree());
    }

    private void startGameWithMock(){
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;
    }


    /**
     * Tests if the playerIndex method return the correct index given the username of a player
     */
    @Test
    public void playerIndexTest(){
        game.players[0] = player1;
        when(player1.getUser()).thenReturn(username1);
        assertEquals(0, game.playerIndex(username1));
        assertEquals(-1, game.playerIndex(username2));
        assertEquals(-1, game.playerIndex(username3));
        assertEquals(-1, game.playerIndex(username4));

        game.players[1] = player2;
        when(player2.getUser()).thenReturn(username2);
        assertEquals(0, game.playerIndex(username1));
        assertEquals(1, game.playerIndex(username2));
        assertEquals(-1, game.playerIndex(username3));
        assertEquals(-1, game.playerIndex(username4));

        game.players[2] = player3;
        when(player3.getUser()).thenReturn(username3);
        assertEquals(0, game.playerIndex(username1));
        assertEquals(1, game.playerIndex(username2));
        assertEquals(2, game.playerIndex(username3));
        assertEquals(-1, game.playerIndex(username4));
    }

    /**
     * Tests if players are removed from the array in a correct way
     */
    @Test
    public void checkRemoveArrayIndexTest(){
        game.players[game.nextFree()] = player1;
        when(player1.getUser()).thenReturn(username1);

        game.players[game.nextFree()] = player2;
        when(player2.getUser()).thenReturn(username2);

        game.players[game.nextFree()] = player3;
        when(player3.getUser()).thenReturn(username3);

        /*
          The players' array has as many playerInGame as the num of players accepted by this game.
         */
        Assert.assertEquals(game.players.length, defaulNumPlayers);
        assertEquals(username1, game.players[0].getUser());
        assertEquals(username2, game.players[1].getUser());
        assertEquals(username3, game.players[2].getUser());

        /*
          If a player is removed by the game, the related player[] will be setted to null and will be available for a
          new player.
         */
        game.removeArrayIndex(game.players, 2);
        assertEquals(username1, game.players[0].getUser());
        assertEquals(username2, game.players[1].getUser());
        Assert.assertNull(game.players[2]);

        /*
          If a player is added in the last array's index and then the first one is removed all the others will be moved
          to leave the last index available for a new element.
         */
        game.players[game.nextFree()] = player3;
        game.removeArrayIndex(game.players, 0);
        assertEquals(username2, game.players[0].getUser());
        assertEquals(username3, game.players[1].getUser());
        Assert.assertNull(game.players[2]);

        /*
          If a player is added in the last array's index and then a player in the middle of the array is removed all
          the others will be moved to leave the last index available for a new element.
         */
        game.players[game.nextFree()] = player1;
        game.removeArrayIndex(game.players, 1);
        assertEquals(username2, game.players[0].getUser());
        assertEquals(username1, game.players[1].getUser());
        Assert.assertNull(game.players[2]);

        /*
          Given an index out of bounds doesn't modify the array
         */
        game.players[game.nextFree()] = player3;
        game.removeArrayIndex(game.players, 5);
        assertEquals(username2, game.players[0].getUser());
        assertEquals(username1, game.players[1].getUser());
        assertEquals(username3, game.players[2].getUser());
    }

    @Test
    public void extractPrivateObjectivesTest() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);

        game.extractPrivateObjectives();

        ArrayList<Color> colorsExtracted = new ArrayList<>();
        Color[] playerColors;

        for (PlayerInGame player : game.getPlayers()){
            playerColors = player.getPrivateObjs();
            assertEquals(NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME, playerColors.length);

            for (Color color : playerColors){
                Assert.assertFalse(colorsExtracted.contains(color));
                colorsExtracted.add(color);
            }
        }
    }

    private void callExtractWpcWhileTesting(){
        int val1 = TASK_DELAY;
        int val2 = CHOOSE_WPC_WAITING_TIME;

        TASK_DELAY = 1;
        CHOOSE_WPC_WAITING_TIME = 1;
        game.extractWPCs();

        TASK_DELAY = val1;
        CHOOSE_WPC_WAITING_TIME = val2;
    }

    @Test
    public void extractWpcTest() throws Exception{
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);

        callExtractWpcWhileTesting();

        ArrayList<String> wpcExtracted = new ArrayList<>();

        for(PlayerInGame player : game.players){
            Assert.assertNotNull(player.getWPC());
            Assert.assertTrue(WpcDB.getInstance().getWpcIDs().contains(player.getWPC().getId()));
            Assert.assertTrue(game.wpcsByUser.get(player.getUser()).contains(player.getWPC().getId()));

            for(String id : game.wpcsByUser.get(player.getUser())){
                Assert.assertFalse(wpcExtracted.contains(id));
                wpcExtracted.add(id);
            }
        }
    }

    @Test
    public void getWpcProposedTest() throws Exception {
        Assert.assertNull(game.getWpcProposed());

        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.started = true;
        callExtractWpcWhileTesting();

        HashMap<String, ArrayList<ClientWpc>> proposed = game.getWpcProposed();
        for(PlayerInGame player : game.players){
            ArrayList<ClientWpc> playerWpcs = proposed.get(player.getUser());
            for(ClientWpc clientWpc : playerWpcs){
                boolean found = false;
                for(String id : game.wpcsByUser.get(player.getUser())){
                    if (clientWpc.getWpcID().equals(id)) found = true;
                }
                Assert.assertTrue(found);
            }
        }

        game.setCurrentTurn(1);
        Assert.assertNull(game.getWpcProposed());
    }

    /**
     * Tests if ToolCards are extracted in a correct way
     */
    @Test
    public void extractToolCardsTest(){
        game.extractToolCards();

        Assert.assertEquals(NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME, game.getToolCards().size());

        ArrayList<ToolCard> cardsExtracted = new ArrayList<>();

        for (ToolCard gameCard : game.getToolCards()){
            Assert.assertNotNull(gameCard);
            Assert.assertTrue(Integer.parseInt(gameCard.getID()) <= 12);
            Assert.assertTrue( Integer.parseInt(gameCard.getID()) >= 1);
            for (ToolCard card : cardsExtracted) Assert.assertNotEquals(card.getID(), gameCard.getID());

            cardsExtracted.add(gameCard);
        }
    }

    /**
     *Tests if Public Objective Cards are extracted in a correct way
     */
    @Test
    public void extractPublicObjectivesTest(){
        game.extractPublicObjectives();

        Assert.assertEquals(NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME, game.getPublicObjectiveCards().size());

        ArrayList<PublicObjectiveCard> cardsExtracted = new ArrayList<>();

        for (PublicObjectiveCard gameCard : game.getPublicObjectiveCards()){
            Assert.assertNotNull(gameCard);

            for (PublicObjectiveCard card : cardsExtracted){
                Assert.assertNotEquals(card.getID(), gameCard.getID());
            }

            cardsExtracted.add(gameCard);
        }
    }

    /**
     * tests if a game is initialized in a correct way, it also tests if the wpc time works in a correct way
     */
    @Test
    public void initializeGameTest() throws Exception {
        int val1 = TASK_DELAY;
        int val2 = CHOOSE_WPC_WAITING_TIME;

        TASK_DELAY = 1;
        CHOOSE_WPC_WAITING_TIME = 1;

        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.initializeGame();

        Assert.assertNotEquals(0, game.publicObjectiveCards.size());
        Assert.assertNotEquals(0, game.toolCards.size());
        for(PlayerInGame player : game.players){
            Assert.assertNotEquals(0, player.getPrivateObjs().length);
            Assert.assertNotNull(player.getWPC());
        }

        assertEquals(0, game.turnPlayer);
        assertEquals(game.players.length-1, game.roundPlayer);
        assertEquals(1, game.currentTurn);

        TASK_DELAY = val1;
        CHOOSE_WPC_WAITING_TIME = val2;
    }

    /**
     * given three players it checks if the nextPlayer() method and all methods related to the turn change
     * work in a correct way
     */
    @Test
    public void nextPlayerTest1() throws Exception {
//        game.addPlayer(username1);
//        game.addPlayer(username2);
//        game.addPlayer(username3);
//
//        game.setCurrentTurn(1);
//
//        assertEquals(1, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(2, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(2, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(1, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(0, game.nextPlayer());
    }



    /**
     * given three players it checks if the nextPlayer() method and all methods related to the turn change
     * work in a correct way. it is different from the one above because it checks if those methods still work
     * with a different order in the list.
     */
    @Test
    public void nextPlayerTest2() {
//        game.players[game.nextFree()] = player1;
//        game.players[game.nextFree()] = player2;
//        game.players[game.nextFree()] = player3;
//
//        game.setCurrentTurn(1);
//        game.setTurnPlayer(1);
//
//        assertEquals(2, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(0, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(0, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(2, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(1, game.nextPlayer());
    }

    /**
     * given three players it checks if the nextPlayer() method and all methods related to the turn change
     * work in a correct way. it is different from the one above because it checks if those methods still work
     * with a different order in the list.
     */
    @Test
    public void nextPlayerTest3() {
//        game.players[game.nextFree()] = player1;
//        game.players[game.nextFree()] = player2;
//        game.players[game.nextFree()] = player3;
//
//        game.setCurrentTurn(1);
//        game.setTurnPlayer(2);
//
//        assertEquals(0, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(1, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(1, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(0, game.nextPlayer());
//        game.setTurnPlayer(game.nextPlayer());
//        game.setCurrentTurn(game.getCurrentTurn()+1);
//
//        assertEquals(2, game.nextPlayer());
    }


   /* @Test
    public void nextTurnAndNextRoundTest() throws Exception {
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;

        ClientEndTurnData endTurnData = mock(ClientEndTurnData.class);
        Assert.assertEquals(0, game.getRoundPlayer());

        Assert.assertEquals(0, game.getTurnPlayer());
        game.endTurn(endTurnData);
        game.nextRound();
        Assert.assertEquals(1, game.getTurnPlayer());

        game.endTurn(endTurnData);
        Assert.assertEquals(2, game.getTurnPlayer());

        game.endTurn(endTurnData);
        game.nextRound();
        Assert.assertEquals(2, game.getTurnPlayer());
      //  game.endTurn();
        Assert.assertEquals(1, game.getTurnPlayer());
     //   game.endTurn();
        Assert.assertEquals(0, game.getTurnPlayer());

      //  game.endTurn();
        Assert.assertEquals(1, game.getRoundPlayer());

        Assert.assertEquals(1, game.getTurnPlayer());
     //   game.endTurn();
        Assert.assertEquals(2, game.getTurnPlayer());
      //  game.endTurn();
        Assert.assertEquals(0, game.getTurnPlayer());
        //game.endTurn();
        Assert.assertEquals(0, game.getTurnPlayer());
       // game.endTurn();
        Assert.assertEquals(2, game.getTurnPlayer());
      //  game.endTurn();
        Assert.assertEquals(1, game.getTurnPlayer());
    }


    @Test
    public void shouldSkippTurnTest(){
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;
        when(player1.isDisconnected()).thenReturn(true);

        game.currentTurn = 0;
        assertEquals(0, game.getTurnPlayer());
        game.numPlayers = 3;
        assertTrue(game.shouldSkipTurn());

    }*/
//    @Test
//    public void nextRoundTest() throws Exception {
//        //mancano da testare i dati avanzati
//
//        game.addPlayer(username2);
//        game.addPlayer(username3);
//
//        Assert.assertEquals(0, game.getRoundPlayer());
//        Assert.assertEquals(1, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(1, game.getRoundPlayer());
//        Assert.assertEquals(2, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(2, game.getRoundPlayer());
//        Assert.assertEquals(3, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(0, game.getRoundPlayer());
//        Assert.assertEquals(4, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(1, game.getRoundPlayer());
//        Assert.assertEquals(5, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(2, game.getRoundPlayer());
//        Assert.assertEquals(6, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(0, game.getRoundPlayer());
//        Assert.assertEquals(7, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(1, game.getRoundPlayer());
//        Assert.assertEquals(8, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(2, game.getRoundPlayer());
//        Assert.assertEquals(9, game.roundTrack.getCurrentRound());
//
//        game.nextRound();
//        Assert.assertEquals(0, game.getRoundPlayer());
//        Assert.assertEquals(10, game.roundTrack.getCurrentRound());
//
//        game.nextRound();   //Non deve generare problemi
//    }

}
