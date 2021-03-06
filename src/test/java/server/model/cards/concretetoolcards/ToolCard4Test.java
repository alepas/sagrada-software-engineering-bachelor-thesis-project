package server.model.cards.concretetoolcards;

import org.junit.Before;
import org.junit.Test;
import server.model.cards.ToolCard;
import server.model.configLoader.ConfigLoader;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.game.Game;
import server.model.game.RoundTrack;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.Cell;
import server.model.wpc.DiceAndPosition;
import server.model.wpc.Wpc;
import shared.clientinfo.*;
import shared.constants.ToolcardConstants;
import shared.exceptions.usersAndDatabaseExceptions.*;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCard4Test {

    private ToolCard4 toolCard4;
    private Game game;
    private Dice chosenDice;
    private Dice diceTwo;
    private PlayerInGame player;
    private Position position;
    private Wpc wpc;
    private DiceAndPosition diceAndPosition1;

    @Before
    public void Before() throws CannotPickDiceException {
        ConfigLoader.loadConfig();
        toolCard4 = new ToolCard4();
        player = mock(PlayerInGame.class);
        game = mock(Game.class);

        when(player.getGame()).thenReturn(game);
        chosenDice = mock(Dice.class);
        when(chosenDice.getId()).thenReturn(1);
        when(chosenDice.getDiceColor()).thenReturn(Color.YELLOW);
        when(chosenDice.getDiceNumber()).thenReturn(4);

        diceTwo = mock(Dice.class);
        when(diceTwo.getId()).thenReturn(2);
        when(diceTwo.getDiceColor()).thenReturn(Color.RED);
        when(diceTwo.getDiceNumber()).thenReturn(5);

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        diceAndPosition1 = mock(DiceAndPosition.class);
        when(diceAndPosition1.getDice()). thenReturn(chosenDice);
        when(diceAndPosition1.getPosition()).thenReturn(null);

        DiceAndPosition diceAndPosition2 = mock(DiceAndPosition.class);
        when(diceAndPosition2.getDice()). thenReturn(diceTwo);
        when(diceAndPosition2.getPosition()).thenReturn(null);

        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.WPC)).thenReturn(diceAndPosition1);
        when(player.dicePresentInLocation(diceTwo.getId(), ClientDiceLocations.WPC)).thenReturn(diceAndPosition2);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.EXTRACTED)).thenReturn(diceAndPosition1);

        toolCard4.setCurrentToolGame(game);
        toolCard4.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard4.setToolUser(player.getUser());

        position = mock(Position.class);
        when(position.getColumn()).thenReturn(1);
        when(position.getRow()).thenReturn(1);

        ClientDice clientDice = mock(ClientDice.class);
        when(chosenDice.getClientDice()).thenReturn(clientDice);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);
        when(game.getRoundTrack().getCopy()).thenReturn(roundTrack);

    }

    /**
     * Tests if the tool card 2 is create in a correct way
     */
    @Test
    public void toolCardTest(){
        assertEquals("4", toolCard4.getID());
        assertEquals(ToolcardConstants.TOOL4_NAME, toolCard4.getName());
        assertEquals(ToolcardConstants.TOOL4_DESCRIPTION, toolCard4.getDescription());
        assertEquals(0, toolCard4.getCurrentStatus());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard4.getToolCardCopy();
        assertEquals(toolCard4.getID(), copy.getID());
        assertEquals(toolCard4.getName(), copy.getName());
        assertEquals(toolCard4.getDescription(), copy.getDescription());
    }

    /**
     * Tests if the tool is initialized in a correct way if the game is a single player game.
     *
     * @throws CannotUseToolCardException not in this test
     */
    @Test
    public void setCardSinglePlayerGameTest() throws CannotUseToolCardException {
        setSchema();
        when(game.isSinglePlayerGame()).thenReturn(true);
        when(player.getWPC().getNumOfDices()).thenReturn(2);
        toolCard4.setCurrentToolPlayer(null);
        MoveData moveData = toolCard4.setCard(player);
        assertEquals(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the game is a multi player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceNotSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard4.pickDice(chosenDice.getId());
    }

    @Test
    public void pickDiceTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard4.setSinglePlayerGame(true);
        MoveData moveData = toolCard4.pickDice(chosenDice.getId());
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
    }

    /**
     * @throws CannotPerformThisMoveException every time that this method is called
     */
    @Test ( expected = CannotPerformThisMoveException.class)
    public void pickNumberTest() throws CannotPerformThisMoveException {
        toolCard4.pickNumber(5);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the state is different from 1
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalStatusTest () throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard4.setCurrentToolStatus(0);
        toolCard4.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceNullPositionStatusOneTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard4.setCurrentToolStatus(1);
        toolCard4.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickPositionException.class )
    public void placeDiceIllegalPositionInWpcStatusOneTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard4.setFirstDiceInitial(diceAndPosition1);
        toolCard4.setCurrentToolStatus(1);
        setSchema();
        toolCard4.placeDice(chosenDice.getId(), position);
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
        toolCard4.setCurrentToolStatus(1);
        setSchema();

        Position position1 = mock(Position.class);
        when(position1.getColumn()).thenReturn(0);
        when(position1.getRow()).thenReturn(0);
        when(player.getWPC().addDiceWithAllRestrictions(chosenDice, position1)).thenReturn(true);

        MoveData moveData = toolCard4.placeDice(chosenDice.getId(), position1);

        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);

        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard4.getNextMove();
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

        moveData = toolCard4.cancelAction(false);
        assertEquals(1, toolCard4.getCurrentStatus());
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
        toolCard4.setCurrentToolStatus(2);
        toolCard4.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickPositionException.class )
    public void placeDiceIllegalPositionInWpcStatusTwoTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard4.setFirstDiceInitial(diceAndPosition1);
        toolCard4.setCurrentToolStatus(2);
        setSchema();
        toolCard4.placeDice(diceTwo.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickDiceException.class )
    public void placeDiceIllegalSecondDiceTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard4.setFirstDiceInitial(diceAndPosition1);
        toolCard4.setCurrentToolStatus(2);
        setSchema();
        toolCard4.placeDice(chosenDice.getId(), position);
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
        toolCard4.setFirstDiceInitial(diceAndPosition1);
        toolCard4.setCurrentToolStatus(2);
        setSchema();

        Position position2 = mock(Position.class);
        when(position2.getColumn()).thenReturn(0);
        when(position2.getRow()).thenReturn(1);
        when(player.getWPC().addDiceWithAllRestrictions(diceTwo, position2)).thenReturn(true);

        MoveData moveData = toolCard4.placeDice(diceTwo.getId(), position2);

        assertTrue(moveData.moveFinished);
        assertNull(moveData.roundTrack);
        assertNull(moveData.extractedDices);
    }


    /**
     * Tests if the nextMove() method works in a correct way with all possible status
     */
    @Test
    public void nextMoveTest(){
        MoveData moveData = toolCard4.getNextMove();
        assertNull(moveData);

        toolCard4.setCurrentToolStatus(1);
        moveData = toolCard4.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard4.setCurrentToolStatus(3);
        assertNull(toolCard4.getNextMove());
    }

    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard4.setCurrentToolStatus(3);
        toolCard4.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard4.setCurrentToolStatus(0);
        toolCard4.cancelAction(false);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusZeroTest() throws CannotCancelActionException {
        toolCard4.setCurrentToolStatus(0);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard4.cancelAction(true);
        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);
    }


    /**
     * doesn't throw a the exception because it's a single player game
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStatusZeroSinglePlayerTest() throws CannotCancelActionException {
        toolCard4.setSinglePlayerGame(true);
        MoveData moveData = toolCard4.cancelAction(false);

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
        toolCard4.setCurrentToolStatus(1);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        toolCard4.cancelAction(false);
        assertEquals(0, toolCard4.getCurrentStatus());
    }



    /**
     * @throws CannotInterruptToolCardException every time that is called
     */
    @Test(expected = CannotInterruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInterruptToolCardException {
        toolCard4.interruptToolCard(ToolCardInterruptValues.YES);
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
        when(player.getWPC().copyWpc()).thenReturn(wpc);

        toolCard4.setCardWpc(wpc);

        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

    }
}

