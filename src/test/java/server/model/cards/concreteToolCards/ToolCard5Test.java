package server.model.cards.concreteToolCards;

import org.junit.Before;
import org.junit.Test;
import server.constants.ToolCardConstants;
import server.model.cards.ToolCard;
import server.model.dicebag.Dice;
import server.model.game.Game;
import server.model.game.RoundTrack;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.Cell;
import server.model.wpc.DiceAndPosition;
import server.model.wpc.Wpc;
import shared.clientInfo.*;
import shared.exceptions.usersAndDatabaseExceptions.*;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//todo
public class ToolCard5Test {


    private ToolCard5 toolCard5;
    private Game game;
    private Dice chosenDice;
    private PlayerInGame player;
    private Position position;
    private ClientDice clientDice;
    private Wpc wpc;

    @Before
    public void Before() throws CannotPickDiceException {
        toolCard5 = new ToolCard5();
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

        toolCard5.setCurrentToolGame(game);
        toolCard5.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard5.setToolUser(player.getUser());

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
        assertEquals("5", toolCard5.getID());
        assertEquals(ToolCardConstants.TOOL5_NAME, toolCard5.getName());
        assertEquals(ToolCardConstants.TOOL5_DESCRIPTION, toolCard5.getDescription());
        assertEquals(0, toolCard5.getCurrentStatus());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard5.getToolCardCopy();
        assertEquals(toolCard5.getID(), copy.getID());
        assertEquals(toolCard5.getName(), copy.getName());
        assertEquals(toolCard5.getDescription(), copy.getDescription());
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the game is a multi player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceNotSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard5.pickDice(chosenDice.getId());
    }

    @Test
    public void pickDiceTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard5.setSinglePlayerGame(true);
//        MoveData moveData = toolCard2.pickDice(chosenDice.getId());

//        assertEquals(chosenDice.getId(), moveData.diceChosen.getDiceID());
    }

    /**
     * @throws CannotPerformThisMoveException every time that this method is called
     */
    @Test ( expected = CannotPerformThisMoveException.class)
    public void pickNumberTest() throws CannotPerformThisMoveException, CannotPickNumberException {
        toolCard5.pickNumber(5);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the state is different from 1
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalStatusTest () throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard5.setCurrentToolStatus(0);
        toolCard5.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceNullPositionStatusOneTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard5.setCurrentToolStatus(1);
        toolCard5.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickPositionException.class )
    public void placeDiceIllegalPositionInWpcStatusOneTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard5.setCurrentToolStatus(1);
        setSchema();
        toolCard5.placeDice(chosenDice.getId(), position);
    }

    /**
     * Tests if the placeDice() method works in a correct way in a cell with no color restrictions
     *
     * @throws CannotPerformThisMoveException isn't thrown in this test
     * @throws CannotPickPositionException isn't thrown in this test
     * @throws CannotPickDiceException isn't thrown in this test
     */
    @Test
    public void placeDiceStatusOneTest() throws CannotPerformThisMoveException, CannotPickPositionException,
            CannotPickDiceException, CannotCancelActionException {
        toolCard5.setCurrentToolStatus(1);
        setSchema();

        Position position1 = mock(Position.class);
        when(position1.getColumn()).thenReturn(0);
        when(position1.getRow()).thenReturn(0);
        when(player.getWPC().addDiceWithAllRestrictions(chosenDice, position1)).thenReturn(true);

        MoveData moveData = toolCard5.placeDice(chosenDice.getId(), position1);

        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);

        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard5.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);

        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        //Tests if the cancelLastOperation()  goes to the old status ina correct way and sends a correct MoveData
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        moveData = toolCard5.cancelAction(false);
        assertEquals(1, toolCard5.getCurrentStatus());
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceNullPositionStatusTwoTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard5.setCurrentToolStatus(2);
        toolCard5.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickPositionException.class )
    public void placeDiceIllegalPositionInWpcStatusTwoTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard5.setCurrentToolStatus(2);
        setSchema();
        toolCard5.placeDice(chosenDice.getId(), position);
    }


    /**
     * Tests if the placeDice() method works in a correct way in a cell with no color restrictions
     *
     * @throws CannotPerformThisMoveException isn't thrown in this test
     * @throws CannotPickPositionException isn't thrown in this test
     * @throws CannotPickDiceException isn't thrown in this test
     */
    @Test
    public void placeDiceStatusTwoTest() throws CannotPerformThisMoveException, CannotPickPositionException,
            CannotPickDiceException {
        toolCard5.setCurrentToolStatus(2);
        setSchema();

        Position position1 = mock(Position.class);
        when(position1.getColumn()).thenReturn(0);
        when(position1.getRow()).thenReturn(0);
        when(player.getWPC().addDiceWithAllRestrictions(chosenDice, position1)).thenReturn(true);

        MoveData moveData = toolCard5.placeDice(chosenDice.getId(), position1);

        assertTrue(moveData.moveFinished);
        assertNull(moveData.roundTrack);
        assertNull(moveData.extractedDices);
    }


    /**
     * Tests if the nextMove() method works in a correct way with all possible status
     */
    @Test
    public void nextMoveTest(){
        MoveData moveData = toolCard5.getNextMove();
        assertNull(moveData);

        toolCard5.setCurrentToolStatus(1);
        moveData = toolCard5.getNextMove();
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard5.setCurrentToolStatus(3);
        assertNull(toolCard5.getNextMove());
    }

    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard5.setCurrentToolStatus(3);
        toolCard5.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard5.setCurrentToolStatus(0);
        toolCard5.cancelAction(false);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusZeroTest() throws CannotCancelActionException {
        toolCard5.setCurrentToolStatus(0);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard5.cancelAction(true);
        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusOneTest() throws CannotCancelActionException {
        toolCard5.setCurrentToolStatus(1);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard5.cancelAction(false);
        assertEquals(0, toolCard5.getCurrentStatus());
        //assertEquals(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, moveData.nextAction);
    }



    /**
     * @throws CannotInteruptToolCardException every time that is called
     */
    @Test(expected = CannotInteruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInteruptToolCardException {
        toolCard5.interruptToolCard(ToolCardInteruptValues.YES);
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
        when(cell1.getNumber()).thenReturn(5);

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

