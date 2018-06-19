package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.ToolCardDB;
import it.polimi.ingsw.model.constants.GameConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.exceptions.gameExceptions.*;
import it.polimi.ingsw.model.usersdb.PlayerInGame;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

public class MultiplayerGameTest {
    private MultiplayerGame game;
    private String username1 = "John", username2 = "Alice", username3 = "Bob", username4 = "Eva";
    int defaulNumPlayers = 3;

    @Before
    public void before() throws Exception{
        game = new MultiplayerGame(defaulNumPlayers);
    }

    /**
     * Checks if the constructor creates a correct object
     */
    @Test
    public void checkMultiPlayerGameConstructor(){
        Assert.assertEquals(0, game.getToolCards().size());
        Assert.assertEquals(0, game.getPublicObjectiveCards().size());
        Assert.assertNotNull(game.getID());
        Assert.assertEquals(0, game.getExtractedDices().size());
        Assert.assertNotNull(game.getDiceBag());
        Assert.assertNotNull(game.getRoundTrack());
        Assert.assertTrue(game.getNumPlayers() >= GameConstants.MIN_NUM_PLAYERS &&
                game.getNumPlayers() <= GameConstants.MAX_NUM_PLAYERS);
        Assert.assertEquals(0, game.nextFree());
        Assert.assertEquals(GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME, game.numOfPrivateObjectivesForPlayer);
        Assert.assertEquals(GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME, game.numOfToolCards);
        Assert.assertEquals(GameConstants.NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME, game.numOfPublicObjectiveCards);
    }

    @Test(expected = InvalidMultiplayerGamePlayersException.class)
    public void checkMultiPlayerGameConstructor2() throws Exception{
        Game game2 = new MultiplayerGame(GameConstants.MAX_NUM_PLAYERS+1);
    }

    @Test(expected = InvalidMultiplayerGamePlayersException.class)
    public void checkMultiPlayerGameConstructor3() throws Exception{
        Game game2 = new MultiplayerGame(GameConstants.MULTIPLAYER_MIN_NUM_PLAYERS-1);
    }

    @Test
    public void addingAndRemovingPlayersTest() throws Exception {
        Boolean bool;
        Assert.assertEquals(0, game.numActualPlayers());

        //Aggiunta fino a 3 utenti
        bool = game.addPlayer(username1);
        Assert.assertEquals(1, game.numActualPlayers());
        Assert.assertFalse(bool);

        bool = game.addPlayer(username2);
        Assert.assertEquals(2, game.numActualPlayers());
        Assert.assertFalse(bool);

        bool = game.addPlayer(username3);
        Assert.assertEquals(3, game.numActualPlayers());
        Assert.assertTrue(bool);

        //Rimozione e aggiunta dell'utente 2
        game.removePlayer(username2);
        Assert.assertEquals(2, game.numActualPlayers());
        bool = game.addPlayer(username2);
        Assert.assertEquals(3, game.numActualPlayers());
        Assert.assertTrue(bool);

        //Rimozione di tutti gli utenti
        game.removePlayer(username1);
        Assert.assertEquals(2, game.numActualPlayers());
        game.removePlayer(username2);
        Assert.assertEquals(1, game.numActualPlayers());
        game.removePlayer(username3);
        Assert.assertEquals(0, game.numActualPlayers());
    }

    @Test
    public void checkIsFull() throws Exception{
        Assert.assertFalse(game.isFull());

        game.addPlayer(username1);
        Assert.assertFalse(game.isFull());

        game.addPlayer(username2);
        Assert.assertFalse(game.isFull());

        game.addPlayer(username3);
        Assert.assertTrue(game.isFull());

        game.removePlayer(username1);
        Assert.assertFalse(game.isFull());
    }

    @Test
    public void checkNextFree() throws Exception{
        Assert.assertEquals(0, game.nextFree());

        game.addPlayer(username1);
        Assert.assertEquals(1, game.nextFree());

        game.addPlayer(username2);
        Assert.assertEquals(2, game.nextFree());

        game.addPlayer(username3);
        Assert.assertEquals(-1, game.nextFree());

        game.removePlayer(username1);
        Assert.assertEquals(2, game.nextFree());
    }

    @Test
    public void checkPlayerIndex() throws Exception{
        game.addPlayer(username1);
        Assert.assertEquals(0, game.playerIndex(username1));
        Assert.assertEquals(-1, game.playerIndex(username2));
        Assert.assertEquals(-1, game.playerIndex(username3));

        game.addPlayer(username2);
        Assert.assertEquals(0, game.playerIndex(username1));
        Assert.assertEquals(1, game.playerIndex(username2));
        Assert.assertEquals(-1, game.playerIndex(username3));

        game.addPlayer(username3);
        Assert.assertEquals(0, game.playerIndex(username1));
        Assert.assertEquals(1, game.playerIndex(username2));
        Assert.assertEquals(2, game.playerIndex(username3));
        Assert.assertEquals(-1, game.playerIndex(username4));
    }

    @Test
    public void checkRemoveArrayIndex() throws Exception{
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        Assert.assertTrue(game.players.length == defaulNumPlayers);
        Assert.assertEquals(username1, game.players[0].getUser());
        Assert.assertEquals(username2, game.players[1].getUser());
        Assert.assertEquals(username3, game.players[2].getUser());
        for(int i = 3; i < game.players.length; i++){
            Assert.assertNull(game.players[i]);
        }

        game.removeArrayIndex(game.players, 2);
        Assert.assertEquals(username1, game.players[0].getUser());
        Assert.assertEquals(username2, game.players[1].getUser());
        for(int i = 2; i < game.players.length; i++){
            Assert.assertNull(game.players[i]);
        }

        game.addPlayer(username3);
        game.removeArrayIndex(game.players, 0);
        Assert.assertEquals(username2, game.players[0].getUser());
        Assert.assertEquals(username3, game.players[1].getUser());
        for(int i = 2; i < game.players.length; i++){
            Assert.assertNull(game.players[i]);
        }

        game.addPlayer(username1);
        game.removeArrayIndex(game.players, 1);
        Assert.assertEquals(username2, game.players[0].getUser());
        Assert.assertEquals(username1, game.players[1].getUser());
        for(int i = 2; i < game.players.length; i++){
            Assert.assertNull(game.players[i]);
        }
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

    @Test(expected = UserNotInThisGameException.class)
    public void removePlayerIllegalTest() throws Exception { game.removePlayer(username2); }

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
            Assert.assertEquals(GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME, playerColors.length);

            for (Color color : playerColors){
                Assert.assertFalse(colorsExtracted.contains(color));
                colorsExtracted.add(color);
            }
        }
    }

    //TODO
//    @Test
//    public void extractWpcTest() throws Exception {
//        game.addPlayer(username1);
//        game.addPlayer(username2);
//        game.addPlayer(username3);
//        game.extractWPCs();
//
//        ArrayList<Wpc> wpcExtracted = new ArrayList<>();
//
//        for (PlayerInGame player : game.getPlayers()){
//            Wpc playerWPC = player.getWPC();
//            Assert.assertNotNull(playerWPC);
//
//            for (Wpc wpc : wpcExtracted){
//                Assert.assertNotEquals(playerWPC.getId(), wpc.getId());
//            }
//
//            wpcExtracted.add(playerWPC);
//        }
//    }

  /*  @Test
    public void extractToolCardsTest(){
        ToolCardDB toolCardDB=ToolCardDB.getInstance();
        game.extractToolCards();

        Assert.assertEquals(GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME, game.getToolCards().size());

        ArrayList<ToolCard> cardsExtracted = new ArrayList<>();

        for (ToolCard gameCard : game.getToolCards()){
            Assert.assertNotNull(gameCard);

            for (ToolCard card : cardsExtracted){
                Assert.assertNotEquals(card.getID(), gameCard.getID());
            }

            cardsExtracted.add(gameCard);
        }
    }*/
/*
    @Test       //Prima di eseguirlo assicurarsi che loadCards(), getCardByID() e getCardsIDs() siano testati
    public void extractPublicObjectivesTest(){
        PublicObjectiveCard.loadCards();
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
    }*/

    @Test
    public void initializeGameTest() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.initializeGame();

        Assert.assertEquals(3, game.getPlayers().length);
    }

    @Test
    public void nextPlayerTest1() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);

        game.setCurrentTurn(1);

        Assert.assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(0, game.nextPlayer());
    }

    @Test
    public void nextPlayerTest2() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);

        game.setCurrentTurn(1);
        game.setTurnPlayer(1);

        Assert.assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(2, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(1, game.nextPlayer());
    }

    @Test
    public void nextPlayerTest3() throws Exception {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);

        game.setCurrentTurn(1);
        game.setTurnPlayer(2);

        Assert.assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(1, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(0, game.nextPlayer());
        game.setTurnPlayer(game.nextPlayer());
        game.setCurrentTurn(game.getCurrentTurn()+1);

        Assert.assertEquals(2, game.nextPlayer());
    }

    //TODO
//    @Test
//    public void nextTurnAndNextRoundTest() throws Exception {
//        game.addPlayer(username1);
//        game.addPlayer(username2);
//        game.addPlayer(username3);
//        Assert.assertEquals(0, game.getRoundPlayer());
//
//        Assert.assertEquals(0, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(1, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(2, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(2, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(1, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(0, game.getTurnPlayer());
//
//        game.nextTurn();
//        Assert.assertEquals(1, game.getRoundPlayer());
//
//        Assert.assertEquals(1, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(2, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(0, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(0, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(2, game.getTurnPlayer());
//        game.nextTurn();
//        Assert.assertEquals(1, game.getTurnPlayer());
//    }

    //TODO
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
