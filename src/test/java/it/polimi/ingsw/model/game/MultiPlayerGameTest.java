package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.WPC;
import it.polimi.ingsw.model.cards.PublicObjectiveCard;
import it.polimi.ingsw.model.cards.ToolCard;
import it.polimi.ingsw.model.exceptions.notEnoughPlayersException;
import it.polimi.ingsw.model.exceptions.userAlreadyInThisGameException;
import it.polimi.ingsw.model.exceptions.userNotInThisGameException;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import it.polimi.ingsw.model.usersdb.User;
import it.polimi.ingsw.model.exceptions.maxPlayersExceededException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiPlayerGameTest {
    private MultiPlayerGame game;
    private User user1, user2, user3, user4;

    @Before
    public void before() {
        user1 = mock(User.class);
        user2 = mock(User.class);
        user3 = mock(User.class);
        user4 = mock(User.class);

        when(user1.getUsername()).thenReturn("John");
        when(user2.getUsername()).thenReturn("Alice");
        when(user3.getUsername()).thenReturn("Bob");
        when(user4.getUsername()).thenReturn("Eva");

        game = new MultiPlayerGame(user1, 3);
    }

    @Test
    public void checkMultiPlayerGameConstructor(){
        Assert.assertNotNull(game.getGameID());
        Assert.assertEquals(1, game.getPlayers().size());
        Assert.assertEquals(0, game.getToolCards().size());
        Assert.assertEquals(0, game.getPublicObjectiveCards().size());
        Assert.assertEquals(0, game.getExtractedDices().size());
        Assert.assertEquals(0, game.getTurnPlayer());
        Assert.assertEquals(0, game.getRoundPlayer());
        Assert.assertEquals(1, game.getCurrentTurn());
        Assert.assertEquals(user1, game.getPlayers().iterator().next().getUser());
        Assert.assertTrue(game.getNumPlayers() >= 2 && game.getNumPlayers() <= 4);
    }

    @Test
    public void addingAndRemovingPlayersTest() {
        Assert.assertEquals(1, game.getPlayers().size());
        game.addPlayer(user2);
        Assert.assertEquals(2, game.getPlayers().size());
        game.addPlayer(user3);
        Assert.assertEquals(3, game.getPlayers().size());

        game.removePlayer(user2);
        Assert.assertEquals(2, game.getPlayers().size());
        game.addPlayer(user2);
        Assert.assertEquals(3, game.getPlayers().size());

        game.removePlayer(user1);
        Assert.assertEquals(2, game.getPlayers().size());
        game.removePlayer(user2);
        Assert.assertEquals(1, game.getPlayers().size());
        game.removePlayer(user3);
        Assert.assertEquals(0, game.getPlayers().size());
    }

    @Test(expected = maxPlayersExceededException.class)
    public void addPlayersIllegalTest1() {
        game.addPlayer(user2);
        game.addPlayer(user3);
        game.addPlayer(user4);
    }

    @Test(expected = userAlreadyInThisGameException.class)
    public void addPlayersIllegalTest2() { game.addPlayer(user1); }

    @Test(expected = userNotInThisGameException.class)
    public void removePlayerIllegalTest() { game.removePlayer(user2); }

    //è giusto utilizzare qui PlayerInGame e Colour?
    @Test
    public void startGameTest() {
        game.addPlayer(user2);
        game.addPlayer(user3);
        game.startGame();

        ArrayList<PlayerInGame> players = game.getPlayers();
        ArrayList<Colour> colorExtracted = new ArrayList<>();
        ArrayList<WPC> wpcExtracted = new ArrayList<>();

        //Verifica che i giocatori non abbiano privateObject o WPC uguali
        for (PlayerInGame player: players){
            Colour playerColor = player.getPrivateObjective1();
            Assert.assertFalse(colorExtracted.contains(playerColor));
            colorExtracted.add(playerColor);

            Assert.assertNull(player.getPrivateObjective2());

            WPC playerWPC = player.getWpc();
            Assert.assertNotNull(playerWPC);
            Assert.assertFalse(wpcExtracted.contains(playerWPC));
            wpcExtracted.add(playerWPC);
        }

        //Verifica che siano state estratte 3 toolCard
        ArrayList<ToolCard> toolCards = game.getToolCards();
        Assert.assertEquals(3, toolCards.size());
        //E che non ci siano carte uguali
        Set<ToolCard> set = new HashSet<>(toolCards);
        Assert.assertFalse(set.size() < toolCards.size());

        //Verifica che siano state estratte 3 publicObjectiveCards
        ArrayList<PublicObjectiveCard> publicObjectiveCards = game.getPublicObjectiveCards();
        Assert.assertEquals(3, publicObjectiveCards.size());
        //E che non ci siano carte uguali
        Set<PublicObjectiveCard> set2 = new HashSet<>(publicObjectiveCards);
        Assert.assertFalse(set2.size() < publicObjectiveCards.size());
    }

    @Test(expected = notEnoughPlayersException.class)
    public void starGameIllegalTest() {
        game.startGame();
    }

    //è giusto testarli assieme?
    @Test
    public void nextTurnAndNextRoundTest(){
        game.addPlayer(user2);
        game.addPlayer(user3);
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

        game.addPlayer(user2);
        game.addPlayer(user3);

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
