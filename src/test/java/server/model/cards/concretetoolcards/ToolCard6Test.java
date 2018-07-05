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

public class ToolCard6Test {

    private ToolCard6 toolCard6;
    private Dice chosenDice;
    private PlayerInGame player;
    private Position position;
    private Wpc wpc;

    @Before
    public void Before() throws CannotPickDiceException {

        ConfigLoader.loadConfig();
        toolCard6 = new ToolCard6();
        player = mock(PlayerInGame.class);
        Game game = mock(Game.class);

        when(player.getGame()).thenReturn(game);
        chosenDice = mock(Dice.class);
        when(chosenDice.getId()).thenReturn(1);
        when(chosenDice.copyDice()).thenReturn(chosenDice);
        when(chosenDice.getDiceNumber()).thenReturn(2);
        when(chosenDice.getDiceColor()).thenReturn(Color.VIOLET);

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        DiceAndPosition diceAndPosition1 = mock(DiceAndPosition.class);
        when(diceAndPosition1.getDice()). thenReturn(chosenDice);
        when(diceAndPosition1.getPosition()).thenReturn(null);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.EXTRACTED)).thenReturn(diceAndPosition1);

        toolCard6.setCurrentToolGame(game);
        toolCard6.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard6.setToolUser(player.getUser());

        position = mock(Position.class);
        when(position.getColumn()).thenReturn(1);
        when(position.getRow()).thenReturn(1);

        ClientDice clientDice = mock(ClientDice.class);
        when(chosenDice.getClientDice()).thenReturn(clientDice);


        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);
        toolCard6.setCardRoundTrack(roundTrack);
    }

    /**
     * Tests if the tool card 2 is create in a correct way
     */
    @Test
    public void toolCardTest(){
        assertEquals("6", toolCard6.getID());
        assertEquals(ToolcardConstants.TOOL6_NAME, toolCard6.getName());
        assertEquals(ToolcardConstants.TOOL6_DESCRIPTION, toolCard6.getDescription());
        assertEquals(0, toolCard6.getCurrentStatus());
        assertNull(toolCard6.getOldDice());
        assertNull(toolCard6.getToolDice());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard6.getToolCardCopy();
        assertEquals(toolCard6.getID(), copy.getID());
        assertEquals(toolCard6.getName(), copy.getName());
        assertEquals(toolCard6.getDescription(), copy.getDescription());
    }

    /**
     * Tests if the tool is initialized in a correct way if the game is a single player game.
     *
     * @throws CannotUseToolCardException not in this test
     */
    @Test
    public void setCardTest() throws CannotUseToolCardException {
        setSchema();
        toolCard6.setCurrentToolPlayer(null);
        MoveData moveData = toolCard6.setCard(player);
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the game is a multi player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceNotSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard6.pickDice(chosenDice.getId());
    }

    /**
     * Checks if the MoveData returned by the pickDice() method is correct in case of a single player game and a correct
     * dice color
     *
     * @throws CannotPickDiceException  not in this test
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void pickDiceSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard6.setSinglePlayerGame(true);
        MoveData moveData = toolCard6.pickDice(chosenDice.getId());

        assertEquals(1, toolCard6.getCurrentStatus());
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertNull(moveData.wherePutNewDice);
    }

    /**
     * Tests if the pick dice interrupt the tool card if the chosen dice isn't placeable, it also tests the nextMove() method
     * when the current status parameter is equals to 30.
     *
     * @throws CannotPickDiceException should never be trhown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     */
    @Test
    public void pickDiceNotPlaceableTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard6.setCurrentToolStatus(1);
        setSchema();
        when(player.getWPC()).thenReturn(wpc);
        when(player.getWPC().isDicePlaceable(chosenDice)).thenReturn(false);

        MoveData moveData = toolCard6.pickDice(chosenDice.getId());
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertEquals(30, toolCard6.getCurrentStatus());
        assertEquals("Il dado non può essere posizionato sulla Window " +
                "Pattern Card. È stato riposizionato nei dadi estratti.", moveData.messageForStop);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);

        moveData = toolCard6.getNextMove();
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertEquals("Il dado non può essere posizionato sulla Window " +
                "Pattern Card. È stato riposizionato nei dadi estratti.", moveData.messageForStop);
        assertEquals(30, toolCard6.getCurrentStatus());
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);
    }

    /**
     * Tests if the pickDice() method works in a correct why when any exception is thrown.
     *
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void pickDicePlaceableTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard6.setCurrentToolStatus(1);
        setSchema();
        when(player.getWPC()).thenReturn(wpc);
        when(player.getWPC().isDicePlaceable(chosenDice)).thenReturn(true);

        MoveData moveData = toolCard6.pickDice(chosenDice.getId());
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(2, toolCard6.getCurrentStatus());
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
        assertEquals(chosenDice.getClientDice(), moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);

        moveData = toolCard6.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(2, toolCard6.getCurrentStatus());
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
        assertEquals(chosenDice.getClientDice(), moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
    }

    /**
     * @throws CannotPerformThisMoveException every time that this method is called
     */
    @Test ( expected = CannotPerformThisMoveException.class)
    public void pickNumberTest() throws CannotPerformThisMoveException {
        toolCard6.pickNumber(5);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the state is different from 2
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalStatusTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard6.setCurrentToolStatus(0);
        toolCard6.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceNullPositionStatusTwoTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard6.setCurrentToolStatus(2);
        toolCard6.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it the chosen dice id is different from the id of the modified one
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickDiceException.class )
    public void placeDiceIllegalIdTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        pickDice();
        toolCard6.pickDice(chosenDice.getId());

        toolCard6.placeDice(4, position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickPositionException.class )
    public void placeDiceIllegalPositionInWpcStatusTwoTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        pickDice();
        toolCard6.pickDice(chosenDice.getId());

        when(player.getWPC()).thenReturn(wpc);
        when(player.getWPC().addDiceWithAllRestrictions(chosenDice, position)).thenReturn(false);

        toolCard6.placeDice(chosenDice.getId(), position);
    }

    /**
     * Tests if the placeDice() method works in a correct way in a cell with no color restrictions
     *
     * @throws CannotPerformThisMoveException isn't thrown in this test
     * @throws CannotPickPositionException isn't thrown in this test
     * @throws CannotPickDiceException isn't thrown in this test
     */
    @Test
    public void placeDiceTest() throws CannotPerformThisMoveException, CannotPickPositionException,
            CannotPickDiceException {
        toolCard6.setCurrentToolStatus(1);
        pickDice();
        toolCard6.pickDice(chosenDice.getId());

        Position position1 = mock(Position.class);
        when(position1.getColumn()).thenReturn(0);
        when(position1.getRow()).thenReturn(0);
        when(player.getWPC().addDiceWithAllRestrictions(chosenDice, position1)).thenReturn(true);

        MoveData moveData = toolCard6.placeDice(chosenDice.getId(), position1);

        assertTrue(moveData.moveFinished);
        assertNull(moveData.diceChosen);

        assertNull(toolCard6.getNextMove());
        assertNull(toolCard6.getOldDice());
        assertNull(toolCard6.getToolDice());

        //Tests if the cancelLastOperation()  goes to the old status ina correct way and sends a correct MoveData
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

    }


    /**
     * Tests if the nextMove() method works in a correct way with status 0 and a stauts that is not possible
     */
    @Test
    public void nextMoveTest(){
        MoveData moveData = toolCard6.getNextMove();
        assertNull(moveData);

        toolCard6.setCurrentToolStatus(1);
        moveData = toolCard6.getNextMove();
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertNull(moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard6.setCurrentToolStatus(3);
        assertNull(toolCard6.getNextMove());
    }

    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard6.setCurrentToolStatus(3);
        toolCard6.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard6.setCurrentToolStatus(0);
        toolCard6.cancelAction(false);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusZeroTest() throws CannotCancelActionException {
        toolCard6.setCurrentToolStatus(0);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        MoveData moveData = toolCard6.cancelAction(true);
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
        toolCard6.setCurrentToolStatus(1);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        toolCard6.cancelAction(false);
        assertEquals(0, toolCard6.getCurrentStatus());
    }

    /**
     * @throws CannotCancelActionException because it is not possible to delete actions while using this tool card
     */
    @Test
    public void cancelLAstOperationStatusTwoAddDiceTest() throws CannotCancelActionException {
        setSchema();
        when(wpc.autoAddDice(chosenDice)).thenReturn(true);
        toolCard6.setCurrentToolStatus(2);
        toolCard6.cancelAction(true);
        assertEquals(0, toolCard6.getCurrentStatus());
    }

    /**
     * Tests if it is possible to cancel in a correct way the action when status is 30
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLAstOperationStatusThirtyTest() throws CannotCancelActionException {
        setSchema();
        toolCard6.setCurrentToolStatus(30);
        toolCard6.cancelAction(true);

        assertEquals(0, toolCard6.getCurrentStatus());
    }


    /**
     * @throws CannotInterruptToolCardException bacause the status is different from 30
     */
    @Test(expected = CannotInterruptToolCardException.class)
    public void interruptToolCardIllegalStatusTest() throws CannotInterruptToolCardException {
        toolCard6.interruptToolCard(ToolCardInterruptValues.YES);
    }

    /**
     * @throws CannotInterruptToolCardException bacause the value is different from OK
     */
    @Test(expected = CannotInterruptToolCardException.class)
    public void interruptToolCardIllegalValue() throws CannotInterruptToolCardException {
        toolCard6.setCurrentToolStatus(30);
        toolCard6.interruptToolCard(ToolCardInterruptValues.YES);
    }

    /**
     * Checks if the interruptToolCArd method works in a correct way when the status is 30 and the answer is OK
     * @throws CannotInterruptToolCardException not in this test
     */
    @Test
    public void interruptToolCardValue() throws CannotInterruptToolCardException {
        toolCard6.setCurrentToolStatus(30);
        MoveData moveData = toolCard6.interruptToolCard(ToolCardInterruptValues.OK);
        assertTrue(moveData.moveFinished);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
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

        toolCard6.setCardWpc(wpc);

        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

    }

    private void pickDice(){
        toolCard6.setCurrentToolStatus(1);
        setSchema();
        when(player.getWPC()).thenReturn(wpc);
        when(player.getWPC().isDicePlaceable(chosenDice)).thenReturn(true);
    }
}
