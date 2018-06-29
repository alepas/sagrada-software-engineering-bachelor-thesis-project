package it.polimi.ingsw.server.model.cards.concreteToolCards;

import it.polimi.ingsw.server.constants.ToolCardConstants;
import it.polimi.ingsw.server.model.cards.ToolCard;
import it.polimi.ingsw.server.model.cards.concreteToolCards.ToolCard2;
import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.game.RoundTrack;
import it.polimi.ingsw.server.model.users.MoveData;
import it.polimi.ingsw.server.model.users.PlayerInGame;
import it.polimi.ingsw.server.model.wpc.Cell;
import it.polimi.ingsw.server.model.wpc.DiceAndPosition;
import it.polimi.ingsw.server.model.wpc.Wpc;
import it.polimi.ingsw.shared.clientInfo.*;
import it.polimi.ingsw.shared.exceptions.usersAndDatabaseExceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCard2Test {
    private ToolCard2 toolCard2;
    private Game game;
    private Dice chosenDice;
    private PlayerInGame player;
    private Position position;
    private ClientDice clientDice;
    private Wpc wpc;

    @Before
    public void Before() throws CannotPickDiceException {
        toolCard2 = new ToolCard2();
        player = mock(PlayerInGame.class);
        game = mock(Game.class);

        when(player.getGame()).thenReturn(game);
        chosenDice = mock(Dice.class);
        when(chosenDice.getId()).thenReturn(1);
        when(chosenDice.getDiceNumber()).thenReturn(4);

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        DiceAndPosition diceAndPosition1 = mock(DiceAndPosition.class);
        when(diceAndPosition1.getDice()). thenReturn(chosenDice);
        when(diceAndPosition1.getPosition()).thenReturn(null);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.WPC)).thenReturn(diceAndPosition1);

        toolCard2.setCurrentToolGame(game);
        toolCard2.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard2.setToolUser(player.getUser());

        position = mock(Position.class);
        when(position.getColumn()).thenReturn(1);
        when(position.getRow()).thenReturn(1);

        clientDice = mock(ClientDice.class);
        when(chosenDice.getClientDice()).thenReturn(clientDice);

    }

    /**
     * Tests if the tool card 2 is create in a correct way
     */
    @Test
    public void toolCardTest(){
        assertEquals("2", toolCard2.getID());
        assertEquals(ToolCardConstants.TOOL2_NAME, toolCard2.getName());
        assertEquals(ToolCardConstants.TOOL2_DESCRIPTION, toolCard2.getDescription());
        assertEquals(0, toolCard2.getCurrentStatus());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard2.getToolCardCopy();
        assertEquals(toolCard2.getID(), copy.getID());
        assertEquals(toolCard2.getName(), copy.getName());
        assertEquals(toolCard2.getDescription(), copy.getDescription());
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the game is a multi player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceNotSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard2.pickDice(chosenDice.getId());
    }

    @Test
    public void pickDiceTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard2.setSinglePlayerGame(true);
//        MoveData moveData = toolCard2.pickDice(chosenDice.getId());

//        assertEquals(chosenDice.getId(), moveData.diceChosen.getDiceID());
    }

    /**
     * @throws CannotPerformThisMoveException every time that this method is called
     */
    @Test ( expected = CannotPerformThisMoveException.class)
    public void pickNumberTest() throws CannotPerformThisMoveException {
        toolCard2.pickNumber(5);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the state is different from 1
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalStatusTest () throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard2.setCurrentToolStatus(0);
        toolCard2.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard2.setCurrentToolStatus(1);
        toolCard2.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickPositionException.class )
    public void placeDiceDicePositionInWpcTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard2.setCurrentToolStatus(1);
        setSchema();
        toolCard2.placeDice(chosenDice.getId(), position);
    }

    /**
     * Tests if the placeDice() method works in a correct way in a cell with no color restrictions
     *
     * @throws CannotPerformThisMoveException isn't thrown in this test
     * @throws CannotPickPositionException isn't thrown in this test
     * @throws CannotPickDiceException isn't thrown in this test
     */
    @Test
    public void placeDiceTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard2.setCurrentToolStatus(1);
        setSchema();
        Position position1 = mock(Position.class);
        when(position1.getColumn()).thenReturn(0);
        when(position1.getRow()).thenReturn(0);
        when(player.getWPC().addDicePersonalizedRestrictions(chosenDice, position1, false, true, true, true, false)).thenReturn(true);
        MoveData moveData = toolCard2.placeDice(chosenDice.getId(), position1);

        assertTrue(moveData.moveFinished);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);

        moveData = toolCard2.getNextMove();
        assertNull(moveData);

    }

    /**
     * Tests if the placeDice() method works in a correct way in a cell with a color restriction which is different
     * from the dice color
     *
     * @throws CannotPerformThisMoveException isn't thrown in this test
     * @throws CannotPickPositionException isn't thrown in this test
     * @throws CannotPickDiceException isn't thrown in this test
     */
    @Test
    public void placeDiceDifferentColorTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard2.setCurrentToolStatus(1);
        when(chosenDice.getDiceColor()).thenReturn(Color.GREEN);
        setSchema();
        Position position1 = mock(Position.class);
        when(position1.getColumn()).thenReturn(1);
        when(position1.getRow()).thenReturn(0);
        when(player.getWPC().addDicePersonalizedRestrictions(chosenDice, position1, false, true, true, true, false)).thenReturn(true);
        MoveData moveData = toolCard2.placeDice(chosenDice.getId(), position1);

        assertTrue(moveData.moveFinished);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);

        moveData = toolCard2.getNextMove();
        assertNull(moveData);

    }

    /**
     * Tests if the nextMove() method works in a correct way with all possible status
     */
    @Test
    public void nextMoveTest(){
        MoveData moveData = toolCard2.getNextMove();
        assertNull(moveData);

        toolCard2.setCurrentToolStatus(1);
        moveData = toolCard2.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard2.setCurrentToolStatus(3);
        assertNull(toolCard2.getNextMove());
    }

    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard2.setCurrentToolStatus(3);
        toolCard2.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard2.setCurrentToolStatus(0);
        toolCard2.cancelAction(false);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusZeroTest() throws CannotCancelActionException {
        toolCard2.setCurrentToolStatus(0);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard2.cancelAction(true);
        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationOneStatusTest() throws CannotCancelActionException {
        toolCard2.setCurrentToolStatus(1);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard2.cancelAction(false);
        assertEquals(0, toolCard2.getCurrentStatus());
//todo: devo finire la cancel
//        assertEquals(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, moveData.nextAction);
    }

    /**
     * @throws CannotInteruptToolCardException every time that is called
     */
    @Test(expected = CannotInteruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInteruptToolCardException {
        toolCard2.interruptToolCard(ToolCardInteruptValues.YES);
    }

    //---------------------------------------------- Not Test Methods --------------------------------------------------

    /**
     * Mocks a schema and sets it to the current player
     */
    private void setSchema(){
        //next lines creates a 2X2 matrix as schema
        wpc = mock(Wpc.class);

        Cell cell1 = mock(Cell.class);
        Position position1 = mock(Position.class);
        when(position1.getColumn()).thenReturn(0);
        when(position1.getRow()).thenReturn(0);
        when(cell1.getCellPosition()).thenReturn(position1);


        Cell cell2 = mock(Cell.class);
        Position position2 = mock(Position.class);
        when(position2.getColumn()).thenReturn(1);
        when(position2.getRow()).thenReturn(0);
        when(cell1.getCellPosition()).thenReturn(position2);
        when(cell1.getColor()).thenReturn(Color.VIOLET);

        Cell cell3 = mock(Cell.class);
        Position position3 = mock(Position.class);
        when(position3.getColumn()).thenReturn(0);
        when(position3.getRow()).thenReturn(1);
        when(cell1.getCellPosition()).thenReturn(position3);

        Cell cell4 = mock(Cell.class);
        Position position4 = mock(Position.class);
        when(position4.getColumn()).thenReturn(1);
        when(position4.getRow()).thenReturn(1);
        when(cell1.getCellPosition()).thenReturn(position4);

        ArrayList<Cell> schema = new ArrayList<>();
        schema.add(cell1);
        schema.add(cell2);
        schema.add(cell3);
        schema.add(cell4);

        when(wpc.getSchema()).thenReturn(schema);
        when(player.getWPC()).thenReturn(wpc);

    }

}
