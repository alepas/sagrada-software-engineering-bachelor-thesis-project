package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.wpc.WPC;
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

public class MultiPlayerGameTest {
    private MultiPlayerGame game;
    private String username1 = "John", username2 = "Alice", username3 = "Bob", username4 = "Eva";

    @Before
    public void before() {
        game = new MultiPlayerGame(3);
    }

    @Test
    public void checkMultiPlayerGameConstructor(){
        Assert.assertEquals(0, game.getToolCards().size());
        Assert.assertEquals(0, game.getPublicObjectiveCards().size());
        Assert.assertNotNull(game.getGameID());
        Assert.assertEquals(0, game.getExtractedDices().size());
        Assert.assertNotNull(game.getDiceBag());
        Assert.assertNotNull(game.getRoundTrack());
        Assert.assertTrue(game.getNumPlayers() >= GameConstants.MIN_NUM_PLAYERS &&
                game.getNumPlayers() <= GameConstants.MAX_NUM_PLAYERS);
        Assert.assertEquals(0, game.getPlayers().size());
        Assert.assertEquals(GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME, game.numOfPrivateObjectivesForPlayer);
        Assert.assertEquals(GameConstants.NUM_TOOL_CARDS_IN_MULTIPLAYER_GAME, game.numOfToolCards);
        Assert.assertEquals(GameConstants.NUM_PUBLIC_OBJ_IN_MULTIPLAYER_GAME, game.numOfPublicObjectiveCards);
    }

    @Test
    public void addingAndRemovingPlayersTest() {
        Boolean bool;
        Assert.assertEquals(0, game.getPlayers().size());

        //Aggiunta fino a 3 utenti
        bool = game.addPlayer(username1);
        Assert.assertEquals(1, game.getPlayers().size());
        Assert.assertFalse(bool);

        bool = game.addPlayer(username2);
        Assert.assertEquals(2, game.getPlayers().size());
        Assert.assertFalse(bool);

        bool = game.addPlayer(username3);
        Assert.assertEquals(3, game.getPlayers().size());
        Assert.assertTrue(bool);

        //Rimozione e aggiunta dell'utente 2
        game.removePlayer(username2);
        Assert.assertEquals(2, game.getPlayers().size());
        bool = game.addPlayer(username2);
        Assert.assertEquals(3, game.getPlayers().size());
        Assert.assertTrue(bool);

        //Rimozione di tutti gli utenti
        game.removePlayer(username1);
        Assert.assertEquals(2, game.getPlayers().size());
        game.removePlayer(username2);
        Assert.assertEquals(1, game.getPlayers().size());
        game.removePlayer(username3);
        Assert.assertEquals(0, game.getPlayers().size());
    }

    @Test(expected = MaxPlayersExceededException.class)
    public void addPlayersIllegalTest1() {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.addPlayer(username4);
    }

    @Test(expected = UserAlreadyInThisGameException.class)
    public void addPlayersIllegalTest2() {
        game.addPlayer(username1);
        game.addPlayer(username1);
    }

    @Test(expected = UserNotInThisGameException.class)
    public void removePlayerIllegalTest() { game.removePlayer(username2); }

    @Test
    public void extractPrivateObjectivesTest(){
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.extractPrivateObjectives();

        ArrayList<Color> colors = new ArrayList<>();

        for (PlayerInGame player : game.getPlayers()){
            Color p1 = player.getPrivateObjective1();
            Color p2 = player.getPrivateObjective2();

            if(GameConstants.NUM_PRIVATE_OBJ_FOR_PLAYER_IN_MULTIPLAYER_GAME == 1){
                Assert.assertNotNull(p1);
                Assert.assertNull(p2);

                for (Color color : colors){
                    Assert.assertFalse(colors.contains(p1));
                }

                colors.add(p1);
            } else {
                Assert.assertNotNull(p1);
                Assert.assertNotNull(p2);
                Assert.assertNotEquals(p1, p2);

                for (Color color : colors){
                    Assert.assertFalse(colors.contains(p1));
                    Assert.assertFalse(colors.contains(p2));
                }

                colors.add(p1);
                colors.add(p2);
            }
        }
    }

    @Test
    public void extractWpcTest(){
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.extractWPCs();

        ArrayList<WPC> wpcExtracted = new ArrayList<>();

        for (PlayerInGame player : game.getPlayers()){
            WPC playerWPC = player.getWPC();
            Assert.assertNotNull(playerWPC);

            for (WPC wpc : wpcExtracted){
                Assert.assertNotEquals(playerWPC.getWpcID(), wpc.getWpcID());
            }

            wpcExtracted.add(playerWPC);
        }
    }

    @Test
    public void extractToolCardsTest(){
        ToolCard.loadCards();
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
    }

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
    }

    @Test
    public void initializeGameTest() {
        game.addPlayer(username1);
        game.addPlayer(username2);
        game.addPlayer(username3);
        game.initializeGame();

        Assert.assertEquals(3, game.getPlayers().size());
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void initializeGameIllegalTest() {
        game.initializeGame();
    }

    @Test
    public void nextPlayerTest1(){
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
    public void nextPlayerTest2(){
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
    public void nextPlayerTest3(){
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

    @Test(expected = MaxNumberOfTurnsPlayedExeption.class)
    public void nextPlayerIllegalTest(){
        game.addPlayer(username2);
        game.addPlayer(username3);

        game.setCurrentTurn(game.getNumPlayers()*GameConstants.NUM_OF_TURNS_FOR_PLAYER_IN_MULTIPLAYER_GAME);

        game.nextPlayer();
    }

    @Test
    public void nextTurnAndNextRoundTest(){
        game.addPlayer(username2);
        game.addPlayer(username3);
        Assert.assertEquals(0, game.getRoundPlayer());

        Assert.assertEquals(0, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(1, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(2, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(2, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(1, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(0, game.getTurnPlayer());

        game.nextTurn();
        Assert.assertEquals(1, game.getRoundPlayer());

        Assert.assertEquals(1, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(2, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(0, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(0, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(2, game.getTurnPlayer());
        game.nextTurn();
        Assert.assertEquals(1, game.getTurnPlayer());
    }

    @Test
    public void nextRoundTest(){
        //mancano da testare i dati avanzati

        game.addPlayer(username2);
        game.addPlayer(username3);

        Assert.assertEquals(0, game.getRoundPlayer());
        Assert.assertEquals(1, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(1, game.getRoundPlayer());
        Assert.assertEquals(2, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(2, game.getRoundPlayer());
        Assert.assertEquals(3, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(0, game.getRoundPlayer());
        Assert.assertEquals(4, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(1, game.getRoundPlayer());
        Assert.assertEquals(5, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(2, game.getRoundPlayer());
        Assert.assertEquals(6, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(0, game.getRoundPlayer());
        Assert.assertEquals(7, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(1, game.getRoundPlayer());
        Assert.assertEquals(8, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(2, game.getRoundPlayer());
        Assert.assertEquals(9, game.roundTrack.getCurrentRound());

        game.nextRound();
        Assert.assertEquals(0, game.getRoundPlayer());
        Assert.assertEquals(10, game.roundTrack.getCurrentRound());

        game.nextRound();   //Non deve generare problemi
    }

}
