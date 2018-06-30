package server.model.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.constants.GameConstants;
import server.model.cards.PublicObjectiveCard;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.game.Game;
import server.model.game.MultiplayerGame;
import server.model.users.DatabaseUsers;
import server.model.users.PlayerInGame;
import shared.clientInfo.ClientEndTurnData;
import shared.exceptions.gameExceptions.InvalidMultiplayerGamePlayersException;
import shared.exceptions.gameExceptions.MaxPlayersExceededException;
import shared.exceptions.gameExceptions.UserAlreadyInThisGameException;
import shared.exceptions.gameExceptions.UserNotInThisGameException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplayerGameTest {

    private MultiplayerGame game;
    private String username1 = "John", username2 = "Alice", username3 = "Bob", username4 = "Eva";
    private int defaulNumPlayers = 3;
    private DatabaseUsers db;
    private PlayerInGame player1;
    private PlayerInGame player2;
    private PlayerInGame player3;
    private PlayerInGame player4;

    @Before
    public void before() throws Exception{
        game = new MultiplayerGame(defaulNumPlayers);

        db = DatabaseUsers.getInstance();

        player1 = mock(PlayerInGame.class);
        when(player1.getUser()).thenReturn(username1);
        player2 = mock(PlayerInGame.class);
        when(player2.getUser()).thenReturn(username2);
        player3 = mock(PlayerInGame.class);
        player4 = mock(PlayerInGame.class);

/*        db.login("John", "password1");
        db.login("Alice", "password2");
        db.login("Bob", "password3");
        db.login("Eva", "password4");*/
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
        Assert.assertTrue(game.getNumPlayers() >= GameConstants.MIN_NUM_PLAYERS &&
                game.getNumPlayers() <= GameConstants.MAX_NUM_PLAYERS);
        assertEquals(0, game.nextFree());
        assertEquals(GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME, game.numOfPrivateObjectivesForPlayer);
        assertEquals(GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME, game.numOfToolCards);
        assertEquals(GameConstants.NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME, game.numOfPublicObjectiveCards);
    }

    /**
     * @throws Exception if the given numPlayers is not valid, in this case it is higher than the nux number accepted
     */
    @Test(expected = InvalidMultiplayerGamePlayersException.class)
    public void checkMultiPlayerGameConstructor2() throws Exception{
        Game game2 = new MultiplayerGame(GameConstants.MAX_NUM_PLAYERS+1);
    }

    /**
     * @throws Exception if the given numPlayers is not valid, in this case it is smaller than the min number accepted
     */
    @Test(expected = InvalidMultiplayerGamePlayersException.class)
    public void checkMultiPlayerGameConstructor3() throws Exception{
        Game game2 = new MultiplayerGame(GameConstants.MULTIPLAYER_MIN_NUM_PLAYERS-1);
    }

    @Test
    public void addingAndRemovingPlayersTest() throws Exception {
        Boolean bool;
        assertEquals(0, game.numActualPlayers());

  /*      //Aggiunta fino a 3 utenti
        bool = game.addPlayer(username1);
        assertEquals(1, game.numActualPlayers());
        Assert.assertFalse(bool);

        bool = game.addPlayer(username2);
        assertEquals(2, game.numActualPlayers());
        Assert.assertFalse(bool);

        bool = game.addPlayer(username3);
        assertEquals(3, game.numActualPlayers());
        Assert.assertTrue(bool);

        //Rimozione e aggiunta dell'utente 2
        game.removePlayer(username2);
        assertEquals(2, game.numActualPlayers());
        bool = game.addPlayer(username2);
        assertEquals(3, game.numActualPlayers());
        Assert.assertTrue(bool);

        //Rimozione di tutti gli utenti
        game.removePlayer(username1);
        assertEquals(2, game.numActualPlayers());
        game.removePlayer(username2);
        assertEquals(1, game.numActualPlayers());
        game.removePlayer(username3);
        assertEquals(0, game.numActualPlayers());*/
    }

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

        game.players[1] = player2;
        when(player2.getUser()).thenReturn(username2);
        assertEquals(0, game.playerIndex(username1));
        assertEquals(1, game.playerIndex(username2));
        assertEquals(-1, game.playerIndex(username3));

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
        for(int i = 3; i < game.players.length; i++) Assert.assertNotNull(game.players[i]);

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
          Given an index out of bounds any element will be removed by the array
         */
        game.players[game.nextFree()] = player3;
        game.removeArrayIndex(game.players, 5);
        for(int i = 0; i < game.players.length; i++ )
            Assert.assertNotNull(game.players[i]);
    }

    @Test(expected = MaxPlayersExceededException.class)
    public void addPlayersIllegalTest1() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.addPlayer(username4);
    }

    @Test(expected = UserAlreadyInThisGameException.class)
    public void addPlayersIllegalTest2() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username1);
    }

    /**
     * @throws Exception because it is trying to remove a player which is not in the array
     */
    @Test(expected = UserNotInThisGameException.class)
    public void removePlayerIllegalTest() throws Exception { game.removePlayer(username2); }

    @Test
    public void extractPrivateObjectivesTest() {
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;
//todo: problema dato dall'interazione tra classi
       // game.extractPrivateObjectives();

        ArrayList<Color> colorsExtracted = new ArrayList<>();
        Color[] playerColors;

        for (PlayerInGame player : game.getPlayers()){
            playerColors = player.getPrivateObjs();
            assertEquals(GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME, playerColors.length);

            for (Color color : playerColors){
                Assert.assertFalse(colorsExtracted.contains(color));
                colorsExtracted.add(color);
            }
        }
    }

    /*//TODO
    @Test
    public void extractWpcTest(){
        game.players[game.nextFree()] = player1;
        when(player1.getUser()).thenReturn(username1);
        game.players[game.nextFree()] = player2;
        when(player1.getUser()).thenReturn(username2);
        game.players[game.nextFree()] = player3;
        when(player1.getUser()).thenReturn(username3);


        game.extractWPCs();

        //Assert.assertNotNull(game.players[0].getWPC());
        Assert.assertTrue(0 < Integer.parseInt(game.players[0].getWPC().getId()));
        Assert.assertTrue(Integer.parseInt(game.players[0].getWPC().getId()) <= 24);
        ArrayList<Wpc> wpcExtracted = new ArrayList<>();

        for (PlayerInGame player : game.getPlayers()){
            Wpc playerWPC = player.getWPC();
            Assert.assertNotNull(playerWPC);

            for (Wpc wpc : wpcExtracted){
                Assert.assertNotEquals(playerWPC.getId(), wpc.getId());
            }

            wpcExtracted.add(playerWPC);
        }
    }*/

    @Test
    public void choseWpTest() {
        ArrayList<String> wpcPlayer = new ArrayList<>();
        game.players[game.nextFree()] = player1;
        when(player1.getUser()).thenReturn(username1);
        when(player1.getWPC()).thenReturn(null);
        game.players[game.nextFree()] = player2;
        when(player1.getUser()).thenReturn(username2);
        game.players[game.nextFree()] = player3;
        when(player1.getUser()).thenReturn(username3);

        wpcPlayer.add("1" );
        wpcPlayer.add( "14" );
        wpcPlayer.add( "7" );
        wpcPlayer.add( "21" );
        game.wpcsByUser.put(username1, wpcPlayer);

        Assert.assertNull(player1.getWPC());
       // game.setPlayerWpc(player1, "7");  //todo
       // Assert.assertNotNull(player1.getWPC());
    }

    /**
     * Tests if ToolCards are extracted in a correct way
     */
    @Test
    public void extractToolCardsTest(){
        game.extractToolCards();

        Assert.assertEquals(GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME, game.getToolCards().size());

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

        Assert.assertEquals(GameConstants.NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME, game.getPublicObjectiveCards().size());

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
    public void initializeGameTest() {
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;
        Color[] color = new Color[2];
        when(player1.getPrivateObjs()).thenReturn(color);
        when(player2.getPrivateObjs()).thenReturn(color);
        when(player3.getPrivateObjs()).thenReturn(color);
        game.initializeGame();

        assertEquals(3, game.getPlayers().length);
    }

    /**
     * given three players it checks if the nextPlayer() method and all methods related to the turn change
     * work in a correct way
     */
    @Test
    public void nextPlayerTest1() {
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;

        game.setCurrentTurn(1);

        assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(0, game.nextPlayer());
    }



    /**
     * given three players it checks if the nextPlayer() method and all methods related to the turn change
     * work in a correct way. it is different from the one above because it checks if those methods still work
     * with a different order in the list.
     */
    @Test
    public void nextPlayerTest2() {
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;

        game.setCurrentTurn(1);
        game.setTurnPlayer(1);

        assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(1, game.nextPlayer());
    }

    /**
     * given three players it checks if the nextPlayer() method and all methods related to the turn change
     * work in a correct way. it is different from the one above because it checks if those methods still work
     * with a different order in the list.
     */
    @Test
    public void nextPlayerTest3() {
        game.players[game.nextFree()] = player1;
        game.players[game.nextFree()] = player2;
        game.players[game.nextFree()] = player3;

        game.setCurrentTurn(1);
        game.setTurnPlayer(2);

        assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        assertEquals(2, game.nextPlayer());
    }

    //TODO
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

    //TODO
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
