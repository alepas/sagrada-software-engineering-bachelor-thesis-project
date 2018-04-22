package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exceptions.userAlreadyInThisGameException;
import it.polimi.ingsw.model.exceptions.userNotInThisGameException;
import it.polimi.ingsw.model.usersdb.User;
import it.polimi.ingsw.model.exceptions.maxPlayersExceededException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    public void checkMultiplayerGameConstructor(){
        Assert.assertNotEquals(null, game.getGameID());
        Assert.assertEquals(1, game.getUsers().size());
        Assert.assertEquals(0, game.getToolCards().size());
        Assert.assertEquals(0, game.getObjectiveCards().size());
        Assert.assertEquals(0, game.getExtractedDices().size());
        Assert.assertEquals(user1, game.getUsers().iterator().next().getUser());
        Assert.assertTrue(game.getNumPlayers() >= 2 && game.getNumPlayers() <= 4);
    }

    @Test
    public void addingAndRemovingPlayersTest() {
        Assert.assertEquals(1, game.getUsers().size());
        game.addPlayer(user2);
        Assert.assertEquals(2, game.getUsers().size());
        game.addPlayer(user3);
        Assert.assertEquals(3, game.getUsers().size());

        game.removePlayer(user2);
        Assert.assertEquals(2, game.getUsers().size());
        game.removePlayer(user1);
        Assert.assertEquals(1, game.getUsers().size());
        game.removePlayer(user3);
        Assert.assertEquals(0, game.getUsers().size());
    }

    @Test(expected = maxPlayersExceededException.class)
    public void addingPlayersIllegalTest1() {
        game.addPlayer(user2);
        game.addPlayer(user3);
        game.addPlayer(user4);
    }

    @Test(expected = userAlreadyInThisGameException.class)
    public void addingPlayersIllegalTest2() {
        game.addPlayer(user1);
    }

    @Test(expected = userNotInThisGameException.class)
    public void removePlayerIllegalTest() {
        game.removePlayer(user2);
    }
}
