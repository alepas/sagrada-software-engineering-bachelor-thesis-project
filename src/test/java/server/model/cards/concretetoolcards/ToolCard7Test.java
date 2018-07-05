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
import shared.exceptions.usersAndDatabaseExceptions.*;

import shared.clientinfo.ClientDice;
import shared.clientinfo.ClientDiceLocations;
import shared.clientinfo.Position;
import shared.clientinfo.ToolCardInterruptValues;
import shared.constants.ToolcardConstants;
import shared.exceptions.usersAndDatabaseExceptions.CannotCancelActionException;
import shared.exceptions.usersAndDatabaseExceptions.CannotInterruptToolCardException;
import shared.exceptions.usersAndDatabaseExceptions.CannotPerformThisMoveException;
import shared.exceptions.usersAndDatabaseExceptions.CannotPickDiceException;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCard7Test {
    private ToolCard7 toolCard7;
    private Dice chosenDice;
    private PlayerInGame player;
    private MoveData moveData;
    private ArrayList<Dice> extractedDices = new ArrayList<>();
    private Game game;

    @Before
    public void Before() throws CannotPickDiceException {

        ConfigLoader.loadConfig();
        toolCard7 = new ToolCard7();
        player = mock(PlayerInGame.class);
        game = mock(Game.class);

        when(player.getGame()).thenReturn(game);
        chosenDice = mock(Dice.class);
        when(chosenDice.getId()).thenReturn(1);
        when(chosenDice.getDiceColor()).thenReturn(Color.BLUE);
        when(chosenDice.getDiceNumber()).thenReturn(4);
        extractedDices.add(chosenDice);

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        DiceAndPosition diceAndPosition1 = mock(DiceAndPosition.class);
        when(diceAndPosition1.getDice()). thenReturn(chosenDice);
        when(diceAndPosition1.getPosition()).thenReturn(null);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.WPC)).thenReturn(diceAndPosition1);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.EXTRACTED)).thenReturn(diceAndPosition1);
        toolCard7.setCurrentToolGame(game);
        toolCard7.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        when (player.getTurnForRound()).thenReturn(2);
        toolCard7.setToolUser(player.getUser());

        Position position = mock(Position.class);
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
        assertEquals("7", toolCard7.getID());
        assertEquals(ToolcardConstants.TOOL7_NAME, toolCard7.getName());
        assertEquals(ToolcardConstants.TOOL7_DESCRIPTION, toolCard7.getDescription());
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


    /**
     *checks if the setCard() method works in a correct way when the game is a multi player game.
     *
     * @throws CannotUseToolCardException not in this toolcard
     */
    @Test
    public void setCardMultiPlayerGameTest() throws CannotUseToolCardException {
        setSchema();
        toolCard7.setCardExtractedDices(extractedDices);
        when(chosenDice.rollDice()).thenReturn(2);
        toolCard7.setCurrentToolStatus(0);
        toolCard7.setCurrentToolPlayer(null);
        toolCard7.setCard(player);
    }

    /**
     *checks if the setCard() method works in a correct way when the game is a single player game.
     *
     * @throws CannotUseToolCardException not in this toolcard
     */
    @Test
    public void setCardSinglePlayerGameTest() throws CannotUseToolCardException {
        setSchema();
        toolCard7.setCardExtractedDices(extractedDices);
        when(chosenDice.rollDice()).thenReturn(2);
        toolCard7.setCurrentToolStatus(0);
        when(game.isSinglePlayerGame()).thenReturn(true);
        toolCard7.setCurrentToolPlayer(null);
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
        moveData = toolCard7.pickDice(chosenDice.getId());
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
     * doesn't throw a the exception because it's a single player game
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStatusZeroSinglePlayerTest() throws CannotCancelActionException {
        toolCard7.setSinglePlayerGame(true);
        moveData = toolCard7.cancelAction(false);

        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);
    }



    /**
     * @throws CannotInterruptToolCardException every time that is called
     */
    @Test(expected = CannotInterruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInterruptToolCardException {
        toolCard7.interruptToolCard(ToolCardInterruptValues.YES);
    }


    /**
     * Mocks a schema and sets it to the current player
     */
    private void setSchema(){
        //next lines creates a 2X2 matrix as schema
        Wpc wpc = mock(Wpc.class);

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

        toolCard7.setCardWpc(wpc);

        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

    }

}
