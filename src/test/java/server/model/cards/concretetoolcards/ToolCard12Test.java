package server.model.cards.concretetoolcards;

import org.junit.Before;
import org.junit.Test;
import server.constants.ToolCardConstants;
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
import shared.clientInfo.*;
import shared.exceptions.usersAndDatabaseExceptions.*;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCard12Test {
    private ToolCard12 toolCard12;
    private Game game;
    private Dice chosenDice;
    private PlayerInGame player;
    private Position position;
    private Wpc wpc;
    private RoundTrack roundTrack;

    @Before
    public void Before() throws CannotPickDiceException {

        ConfigLoader.loadConfig();
        toolCard12 = new ToolCard12();
        player = mock(PlayerInGame.class);
        game = mock(Game.class);

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

        toolCard12.setCurrentToolGame(game);
        toolCard12.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard12.setToolUser(player.getUser());

        position = mock(Position.class);
        when(position.getColumn()).thenReturn(1);
        when(position.getRow()).thenReturn(1);

        ClientDice clientDice = mock(ClientDice.class);
        when(chosenDice.getClientDice()).thenReturn(clientDice);


        roundTrack = mock(RoundTrack.class);
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
        assertEquals("12", toolCard12.getID());
        assertEquals(ToolCardConstants.TOOL12_NAME, toolCard12.getName());
        assertEquals(ToolCardConstants.TOOL12_DESCRIPTION, toolCard12.getDescription());
        assertEquals(0, toolCard12.getCurrentStatus());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard12.getToolCardCopy();
        assertEquals(toolCard12.getID(), copy.getID());
        assertEquals(toolCard12.getName(), copy.getName());
        assertEquals(toolCard12.getDescription(), copy.getDescription());
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
        toolCard12.setCurrentToolPlayer(null);
        MoveData moveData = toolCard12.setCard(player);
        assertEquals(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the game is a multi player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceNotSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard12.pickDice(chosenDice.getId());
    }

    /**
     * Checks if the pickDice() method works in a correct way.
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void pickDiceTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard12.setSinglePlayerGame(true);
        MoveData moveData = toolCard12.pickDice(chosenDice.getId());
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
    }

    /**
     * @throws CannotPerformThisMoveException every time that this method is called
     */
    @Test ( expected = CannotPerformThisMoveException.class)
    public void pickNumberTest() throws CannotPerformThisMoveException {
        toolCard12.pickNumber(5);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the state is different from 1
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalStatusTest () throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard12.setCurrentToolStatus(0);
        toolCard12.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionSatusOneTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard12.setCurrentToolStatus(1);
        toolCard12.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionStatusTwoTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard12.setCurrentToolStatus(2);
        toolCard12.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException because it is not possible to place the chosen dice in the chosen position
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickPositionException.class )
    public void placeDicePositionInWpcStatusOneTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard12.setCurrentToolStatus(1);
        setSchema();
        Dice dice = mock(Dice.class);
        toolCard12.setCardRoundTrack(roundTrack);
        when(dice.getDiceColor()).thenReturn(Color.BLUE);
        ArrayList<Dice> dicesNotUsed = new ArrayList<>();
        dicesNotUsed.add(dice);
        when(roundTrack.getDicesNotUsed()).thenReturn(dicesNotUsed);
        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(false);
        toolCard12.placeDice(chosenDice.getId(), position);
    }


    /**
     * @throws CannotPickDiceException because the chosen dice color is different from all colors of dices in roundTrack
     * @throws CannotPickPositionException  in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickDiceException.class )
    public void placeDiceIllegalColorStatusOneTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard12.setCurrentToolStatus(1);
        setSchema();
        Dice dice = mock(Dice.class);
        toolCard12.setCardRoundTrack(roundTrack);
        when(dice.getDiceColor()).thenReturn(Color.RED);
        ArrayList<Dice> dicesNotUsed = new ArrayList<>();
        dicesNotUsed.add(dice);
        when(roundTrack.getDicesNotUsed()).thenReturn(dicesNotUsed);
        toolCard12.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException because the chosen dice color is different from all colors of dices in roundTrack
     * @throws CannotPickPositionException  in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException in any case because this method wants to test the other exception
     */
    @Test (expected = CannotPickDiceException.class )
    public void placeDiceIllegalColorStatusTwoTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard12.setCurrentToolStatus(2);
        setSchema();
        Dice dice = mock(Dice.class);
        toolCard12.setCardRoundTrack(roundTrack);
        when(dice.getDiceColor()).thenReturn(Color.RED);
        ArrayList<Dice> dicesNotUsed = new ArrayList<>();
        dicesNotUsed.add(dice);
        when(roundTrack.getDicesNotUsed()).thenReturn(dicesNotUsed);
        toolCard12.placeDice(chosenDice.getId(), position);
    }

    /**
     * Tests if the placeDice() method works in a correct way while the player wants to modify the position of the first
     * dice; after the placement of the first dice checks if it enters in a correct way in the status 20 and then if it
     * lets place a second dice.
     *
     * @throws CannotPerformThisMoveException isn't thrown in this test
     * @throws CannotPickPositionException isn't thrown in this test
     * @throws CannotPickDiceException isn't thrown in this test
     */
    @Test
    public void placeDiceStatusOneAndTwoTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard12.setCurrentToolStatus(1);
        setSchema();
        Dice dice = mock(Dice.class);
        toolCard12.setCardRoundTrack(roundTrack);
        when(dice.getDiceColor()).thenReturn(Color.BLUE);
        ArrayList<Dice> dicesNotUsed = new ArrayList<>();
        dicesNotUsed.add(dice);
        when(roundTrack.getDicesNotUsed()).thenReturn(dicesNotUsed);
        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(true);
        MoveData moveData = toolCard12.placeDice(chosenDice.getId(), position);

        assertEquals(20, toolCard12.getCurrentStatus());
        assertEquals("Vuoi spostare un altro dado dello stesso colore del dado appena spostato?", moveData.messageForStop );
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);

        toolCard12.setCurrentToolStatus(2);
        moveData = toolCard12.placeDice(chosenDice.getId(), position);
        assertTrue(moveData.moveFinished);

        assertNull(toolCard12.getFirstDiceInitial());
        assertNull(toolCard12.getSecondDiceInitial());
    }

    /**
     * Tests if the nextMove() method works in a correct way with status one and a status that doesn't exist in this
     */
    @Test
    public void nextMoveStatusOneTest() {
        MoveData moveData = toolCard12.getNextMove();
        assertNull(moveData);

        toolCard12.setCurrentToolStatus(1);
        moveData = toolCard12.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard12.setCurrentToolStatus(3);
        assertNull(toolCard12.getNextMove());
    }

    /**
     * Tests if the nextMove() method works in a correct way with status 2
     */
    @Test
    public void nextMoveStatusTwoTest(){
        toolCard12.setCurrentToolStatus(2);

        MoveData moveData = toolCard12.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);
    }

    /**
     * Tests if the nextMove() method works in a correct way with status 2
     */
    @Test
    public void nextMoveStatusTwentyTest(){

        toolCard12.setCurrentToolStatus(20);

        MoveData moveData = toolCard12.getNextMove();
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertEquals("Vuoi spostare un altro dado dello stesso colore del dado appena spostato?", moveData.messageForStop);
        assertNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        assertTrue(moveData.bothYesAndNo);
        assertFalse(moveData.canceledToolCard);
        assertFalse(moveData.showBackButton);
    }

    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard12.setCurrentToolStatus(3);
        toolCard12.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard12.setCurrentToolStatus(0);
        toolCard12.cancelAction(false);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusZeroTest() throws CannotCancelActionException {
        toolCard12.setCurrentToolStatus(0);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard12.cancelAction(true);
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
        toolCard12.setCurrentToolStatus(1);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        toolCard12.cancelAction(false);
        assertEquals(0, toolCard12.getCurrentStatus());
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 2.
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusTwoTest() throws CannotCancelActionException {
        //case all = false
        toolCard12.setCurrentToolStatus(2);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        MoveData moveData = toolCard12.cancelAction(false);
        assertEquals(20, toolCard12.getCurrentStatus());
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertEquals("Vuoi spostare un altro dado dello stesso colore del dado appena spostato?", moveData.messageForStop);
        assertNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        assertTrue(moveData.bothYesAndNo);
        assertFalse(moveData.canceledToolCard);
        assertFalse(moveData.showBackButton);

        //case all = true
        toolCard12.setCurrentToolStatus(2);
        moveData = toolCard12.cancelAction(true);
        assertEquals(0, toolCard12.getCurrentStatus());
        assertTrue(moveData.moveFinished);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 20.
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusTwentyTest() throws CannotCancelActionException {
        setSchema();

        toolCard12.setCurrentToolStatus(20);
        MoveData moveData = toolCard12.cancelAction(true);
        assertTrue( moveData.moveFinished);

        assertNull(moveData.roundTrack);
        assertNull(moveData.wpc);
        assertNull(moveData.extractedDices);

    }

    /**
     * @throws CannotInteruptToolCardException every time that is called
     */
    @Test(expected = CannotInteruptToolCardException.class)
    public void interruptToolCardIllegalStatusTest() throws CannotInteruptToolCardException {
        toolCard12.interruptToolCard(ToolCardInteruptValues.YES);
    }

    /**
     * @throws CannotInteruptToolCardException every time that is called
     */
    @Test(expected = CannotInteruptToolCardException.class)
    public void interruptToolCardIllegalValueTest() throws CannotInteruptToolCardException {
        toolCard12.setCurrentToolStatus(20);
        toolCard12.interruptToolCard(ToolCardInteruptValues.OK);
    }

    /**
     * checks if the interrupt method returns a correct moveData in case the value is equal to NO.
     *
     * @throws CannotInteruptToolCardException not in this method
     */
    @Test
    public void interruptToolCardValueNoTest() throws CannotInteruptToolCardException {
        setSchema();
        toolCard12.setCurrentToolStatus(20);
        MoveData moveData = toolCard12.interruptToolCard(ToolCardInteruptValues.NO);

        assertTrue( moveData.moveFinished);

        assertNull(moveData.roundTrack);
        assertNull(moveData.wpc);
        assertNull(moveData.extractedDices);
    }

    /**
     * checks if the interrupt method returns a correct moveData in case the value is equal to YES.
     *
     * @throws CannotInteruptToolCardException not in this method
     */
    @Test
    public void interruptToolCardValueYesTest() throws CannotInteruptToolCardException {
        toolCard12.setCurrentToolStatus(20);
        MoveData moveData = toolCard12.interruptToolCard(ToolCardInteruptValues.YES);

        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);

        assertNull(moveData.roundTrack);
        assertNull(moveData.wpc);
        assertNull(moveData.extractedDices);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);
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
        when(player.getWPC().copyWpc()).thenReturn(wpc);

        toolCard12.setCardWpc(wpc);

        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

    }
}
