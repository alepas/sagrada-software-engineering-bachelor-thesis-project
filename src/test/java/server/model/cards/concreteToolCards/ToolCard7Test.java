package server.model.cards.concreteToolCards;

import org.junit.Before;
import org.junit.Test;
import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.Game;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.DiceAndPosition;
import shared.clientInfo.ClientDice;
import shared.clientInfo.ClientDiceLocations;
import shared.clientInfo.Position;
import shared.clientInfo.ToolCardInteruptValues;
import shared.exceptions.usersAndDatabaseExceptions.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCard7Test {
    private ToolCard7 toolCard7;
    private Dice chosenDice;
    private PlayerInGame player;

    @Before
    public void Before() throws CannotPickDiceException {
        toolCard7 = new ToolCard7();
        player = mock(PlayerInGame.class);
        Game game = mock(Game.class);

        when(player.getGame()).thenReturn(game);
        chosenDice = mock(Dice.class);
        when(chosenDice.getId()).thenReturn(1);
        when(chosenDice.getDiceColor()).thenReturn(Color.BLUE);
        when(chosenDice.getDiceNumber()).thenReturn(4);

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        DiceAndPosition diceAndPosition1 = mock(DiceAndPosition.class);
        when(diceAndPosition1.getDice()). thenReturn(chosenDice);
        when(diceAndPosition1.getPosition()).thenReturn(null);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.WPC)).thenReturn(diceAndPosition1);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.EXTRACTED)).thenReturn(diceAndPosition1);
        toolCard7.setCurrentToolGame(game);
        toolCard7.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard7.setToolUser(player.getUser());

        Position position = mock(Position.class);
        when(position.getColumn()).thenReturn(1);
        when(position.getRow()).thenReturn(1);

        ClientDice clientDice = mock(ClientDice.class);
        when(chosenDice.getClientDice()).thenReturn(clientDice);

    }

    /**
     * Tests if the tool card 2 is create in a correct way
     */
    @Test
    public void toolCardTest(){
        assertEquals("7", toolCard7.getID());
        assertEquals(ToolCardConstants.TOOL7_NAME, toolCard7.getName());
        assertEquals(ToolCardConstants.TOOL7_DESCRIPTION, toolCard7.getDescription());
        assertEquals(0, toolCard7.getCurrentStatus());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard7.getToolCardCopy();
        assertEquals(toolCard7.getID(), copy.getID());
        assertEquals(toolCard7.getName(), copy.getName());
        assertEquals(toolCard7.getDescription(), copy.getDescription());
    }

//todo
    @Test
    public void setCardTest() throws CannotUseToolCardException {
        toolCard7.setCurrentToolStatus(0);
        toolCard7.setCard(player);
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the game is a multi player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceNotSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard7.pickDice(chosenDice.getId());
    }

    /**
     * Checks if the pickDice() method works in a correct way.
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void pickDiceTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard7.setSinglePlayerGame(true);
        MoveData moveData = toolCard7.pickDice(chosenDice.getId());
        assertNull( moveData.wpc);
        assertNull(moveData.roundTrack);
        assertTrue(moveData.moveFinished);
    }

    /**
     * @throws CannotPerformThisMoveException every time that this method is called
     */
    @Test ( expected = CannotPerformThisMoveException.class)
    public void pickNumberTest() throws CannotPerformThisMoveException {
        toolCard7.pickNumber(5);
    }


    /**
     * @throws CannotPerformThisMoveException always
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionTest () throws CannotPerformThisMoveException{
        toolCard7.placeDice(chosenDice.getId(), null);
    }


    /**
     * Tests if the nextMove() method works in a correct way with all possible status. it always returns null
     */
    @Test
    public void nextMoveTest(){
        assertNull(toolCard7.getNextMove());

        toolCard7.setCurrentToolStatus(3);
        assertNull(toolCard7.getNextMove());
    }

    /**
     * @throws CannotCancelActionException because the boolean the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard7.setCurrentToolStatus(3);
        toolCard7.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard7.cancelAction(false);
    }



    /**
     * @throws CannotInteruptToolCardException every time that is called
     */
    @Test(expected = CannotInteruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInteruptToolCardException {
        toolCard7.interruptToolCard(ToolCardInteruptValues.YES);
    }
}
