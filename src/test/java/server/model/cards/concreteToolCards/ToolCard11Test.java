package server.model.cards.concreteToolCards;

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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCard11Test {
    private ToolCard11 toolCard11;
    private Dice chosenDiceMiddleNumber;
    private Dice chosenDice1Number;
    private Dice chosenDice6Number;
    private PlayerInGame player;
    private Game game;
    private ArrayList<Dice> extractedDices = new ArrayList<>();
    private ClientDice clientMiddleDice;
    private Position position;

    @Before
    public void Before() throws CannotPickDiceException {

        ConfigLoader.loadConfig();
        toolCard11 = new ToolCard11();

        game = mock(Game.class);

        chosenDiceMiddleNumber = mock(Dice.class);
        when(chosenDiceMiddleNumber.getId()).thenReturn(1);
        when(chosenDiceMiddleNumber.getDiceColor()).thenReturn(Color.VIOLET);
        when(chosenDiceMiddleNumber.getDiceNumber()).thenReturn(4);

        chosenDice1Number = mock(Dice.class);
        when(chosenDice1Number.getId()).thenReturn(2);
        when(chosenDice1Number.getDiceNumber()).thenReturn(1);

        chosenDice6Number = mock(Dice.class);
        when(chosenDice6Number.getId()).thenReturn(3);
        when(chosenDice6Number.getDiceNumber()).thenReturn(6);

        player = mock(PlayerInGame.class);
        extractedDices.add(chosenDiceMiddleNumber);
        extractedDices.add(chosenDice1Number);
        extractedDices.add(chosenDice6Number);

        position = mock(Position.class);
        when(position.getRow()).thenReturn(0);
        when(position.getColumn()).thenReturn(0);

        when(player.getGame()).thenReturn(game);
        when(game.getExtractedDices()).thenReturn(extractedDices);

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        DiceAndPosition diceAndPosition = mock(DiceAndPosition.class);
        when(diceAndPosition.getDice()). thenReturn(chosenDiceMiddleNumber);
        when(diceAndPosition.getPosition()).thenReturn(null);
        try {
            when(player.dicePresentInLocation(chosenDiceMiddleNumber.getId(), ClientDiceLocations.EXTRACTED)).thenReturn(diceAndPosition);
        } catch (CannotPickDiceException e) {
            e.printStackTrace();
        }

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        DiceAndPosition diceAndPosition1 = mock(DiceAndPosition.class);
        when(diceAndPosition1.getDice()). thenReturn(chosenDice1Number);
        when(diceAndPosition1.getPosition()).thenReturn(null);
        when(player.dicePresentInLocation(chosenDice1Number.getId(), ClientDiceLocations.EXTRACTED)).thenReturn(diceAndPosition1);

        toolCard11.setCurrentToolGame(game);
        toolCard11.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard11.setToolUser(player.getUser());

        clientMiddleDice = mock(ClientDice.class);
        when(chosenDiceMiddleNumber.getClientDice()).thenReturn(clientMiddleDice);
        when(chosenDiceMiddleNumber.copyDice()).thenReturn(chosenDiceMiddleNumber);

        ClientDice clientBoundDice = mock(ClientDice.class);
        when(chosenDice1Number.getClientDice()).thenReturn(clientBoundDice);
        when(chosenDice1Number.copyDice()).thenReturn(chosenDice1Number);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);
        when(game.getRoundTrack().getCopy()).thenReturn(roundTrack);
    }

    /**
     * Checks if the constructor sets all parameters in a correct way
     */
    @Test
    public void toolCard1Test(){
        assertEquals("11", toolCard11.getID());
        assertEquals(ToolCardConstants.TOOL11_NAME, toolCard11.getName());
        assertEquals(ToolCardConstants.TOOL11_DESCRIPTION, toolCard11.getDescription());
        assertEquals(0, toolCard11.getCurrentStatus());
        assertEquals(6, toolCard11.getNumberSize());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard11.getToolCardCopy();
        assertEquals(toolCard11.getID(), copy.getID());
        assertEquals(toolCard11.getName(), copy.getName());
        assertEquals(toolCard11.getDescription(), copy.getDescription());
    }

    /**
     * Tests if the tool is initialized in a correct way if the game is a multi player game.
     *
     * @throws CannotUseToolCardException not in this test
     */
    @Test
    public void setCardMultiPlayerGameTest() throws CannotUseToolCardException {
        setSchema();
        when(player.getWPC().getNumOfDices()).thenReturn(2);
        toolCard11.setCurrentToolPlayer(null);
        MoveData moveData = toolCard11.setCard(player);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
    }

    /**
     * the current state is still equals to zero but the game isn't a single player game so the cannot perform this
     * action exception will be thrown.
     *
     * @throws CannotPickDiceException when it is not possible to pick the chosen dice
     * @throws CannotPerformThisMoveException when it is not possible to perform the chosen action
     */
    @Test(expected = CannotPerformThisMoveException.class)
    public void pickDiceIllegalStatusTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard11.pickDice(chosenDiceMiddleNumber.getId());
    }


    /**
     * if the game is a single player game the pickDice() method checks if the picked dice as the correct color and
     * then goes to the next action.
     *
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void pickDiceSinglePlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard11.setSinglePlayerGame(true);
        MoveData moveData = toolCard11.pickDice(chosenDiceMiddleNumber.getId());

        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.DICEBAG, moveData.wherePutNewDice);
    }


    /**
     * @throws CannotPickDiceException isn't thrown in this test
     * @throws CannotPerformThisMoveException because this method can't be called in a multi player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceMiddleValueTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard11.pickDice(chosenDiceMiddleNumber.getId());
    }


    /**
     * @throws CannotPickNumberException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the current state is different from the expected one
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickNumberIllegalCurrentStateTest() throws CannotPickNumberException, CannotPerformThisMoveException {
        toolCard11.pickNumber(1);
    }

    /**
     * @throws CannotPickNumberException because the number isn't 1 or -1 as it should be
     * @throws CannotPerformThisMoveException in no cases because this method wants to test the other exception
     */
    @Test (expected = CannotPickNumberException.class)
    public void pickNumberIllegalNumberTest() throws CannotPickNumberException, CannotPerformThisMoveException {
        toolCard11.setCurrentToolStatus(2);
        toolCard11.pickNumber(8);
    }


    /**
     * Tests if the pickNumber() method works in a correct way with a dice with  a number between 1 and 6. It has already been
     * checked if given a dice with number 1 or 6 and the wrong param number it throws an exception, for this reason it
     * can be assumed that, if a correct param number will, be given the method will work in a proper way
     *
     * @throws CannotPickNumberException  should never be thrown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     */
    @Test
    public void pickNumberTest() throws CannotPerformThisMoveException, CannotPickNumberException, CannotPickDiceException, CannotPickPositionException {
        toolCard11.setCurrentToolStatus(2);
        MoveData moveData = toolCard11.pickNumber(1);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertEquals(clientMiddleDice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(3, toolCard11.getCurrentStatus());

        moveData = toolCard11.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertEquals(clientMiddleDice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(3, toolCard11.getCurrentStatus());
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPickPositionException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the current state is different from the expected one
     */
    @Test(expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalCurrentStateTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        Position position = mock(Position.class);
        toolCard11.placeDice(chosenDice6Number.getId(), position );
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPickPositionException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test(expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard11.setCurrentToolStatus(3);
        toolCard11.placeDice(chosenDice6Number.getId(), null );
    }

    /**
     * @throws CannotPickDiceException because the id of the chhosen dice is different from the one of the modified dice
     * @throws CannotPickPositionException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException in no cases because this method wants to test the other exception
     */
    @Test(expected = CannotPickDiceException.class)
    public void placeDiceIllegalDiceIdTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException, CannotPickNumberException {
        toolCard11.setCurrentToolStatus(1);
        toolCard11.pickDice(chosenDice1Number.getId());
        toolCard11.pickNumber(1);

        Position position = mock(Position.class);
        toolCard11.placeDice(chosenDice6Number.getId(), position );
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPickPositionException because the chosen position doens't respect the position rules
     * @throws CannotPerformThisMoveException in no cases because this method wants to test the other exception
     */
    @Test(expected = CannotPickPositionException.class)
    public void placeDiceIllegalDicePositionInWpcTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException, CannotPickNumberException {
        toolCard11.setCurrentToolStatus(1);
        toolCard11.pickDice(chosenDice1Number.getId());
        toolCard11.pickNumber(1);

        setSchema();
        Position position = mock(Position.class);
        when(position.getRow()).thenReturn(1);
        when(position.getColumn()).thenReturn(1);
        toolCard11.placeDice(chosenDice1Number.getId(), position );
    }

    /**
     * Tests if the placeDice() method works in a correct way.
     *
     * @throws CannotPickDiceException should never be thrown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     * @throws CannotPickNumberException should never be thrown in this test
     * @throws CannotPickPositionException should never be thrown in this test
     */
    @Test
    public void placeDiceTest() throws CannotPickDiceException, CannotPerformThisMoveException,
            CannotPickNumberException, CannotPickPositionException {
        toolCard11.setCurrentToolStatus(1);
        toolCard11.pickDice(chosenDice1Number.getId());
        toolCard11.pickNumber(1);

        setSchema();

        when(toolCard11.getCardWpc().addDiceWithAllRestrictions(chosenDice1Number, position)).thenReturn(true);
        MoveData moveData = toolCard11.placeDice(chosenDice1Number.getId(), position);
        assertTrue(moveData.moveFinished);

        /*at the end of placeDice() method the clean on is called, those assertions checks if it reset parameters in
          a correct way: Both dice and oldDice must be null and the arrayList size must be zero
        */
        assertNull(toolCard11.getToolDice());
        assertNull(toolCard11.getOldDice());
        assertEquals(0, toolCard11.getNumberSize());

        moveData = toolCard11.getNextMove();
        assertNull(moveData);

    }

    /**
     * Tests for status equals to 0 and 1 if the nextMove() method works in a correct way. It doesn't test status 2 and 3
     * bacuse they have been already tested in the methods above
     */
    @Test
    public void nextMoveTest(){
        MoveData moveData = toolCard11.getNextMove();
        assertNull(moveData);

        toolCard11.setCurrentToolStatus(1);
        moveData = toolCard11.getNextMove();
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertNull(moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard11.setCurrentToolStatus(4);
        assertNull(toolCard11.getNextMove());
    }

    /**
     * @throws CannotInteruptToolCardException every time that is called
     */
    @Test(expected = CannotInteruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInteruptToolCardException {
        toolCard11.interruptToolCard(ToolCardInteruptValues.OK);
    }

    /**
     * Checks if the ToolCard ends in a correct way.
     *
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationTest() throws CannotCancelActionException {
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(player.getWPC().getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        when(game.getRoundTrack()).thenReturn(roundTrack);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(game.getRoundTrack().getClientRoundTrack()).thenReturn(clientRoundTrack);

        MoveData moveData = toolCard11.cancelAction(true);
        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);
    }

    /**
     * Checks if the Tool Card goes back in a correct way from status 3 to status 2.
     *
     * @throws CannotCancelActionException not in this test
     * @throws CannotPerformThisMoveException not in this test
     * @throws CannotPickDiceException not in this test
     * @throws CannotPickNumberException not in this test
     *
     */
    @Test
    public void cancelLastOperationStateThreeTest() throws CannotCancelActionException,
            CannotPerformThisMoveException, CannotPickDiceException, CannotPickNumberException {
        toolCard11.setCurrentToolStatus(1);
        toolCard11.pickDice(chosenDiceMiddleNumber.getId());
        toolCard11.pickNumber(1);

        MoveData moveData = toolCard11.cancelAction(false);

        assertEquals(2, toolCard11.getCurrentStatus());
        assertEquals(NextAction.SELECT_NUMBER_TOOLCARD, moveData.nextAction);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
    }


    /**
     * Checks if the Tool Card goes back in a correct way from status 2 to status 1.
     *
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStateTwoTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(2);
        MoveData moveData = toolCard11.cancelAction(false);

        assertEquals(1, toolCard11.getCurrentStatus());
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
    }

    /**
     * Tests if it is possible to cancel in a correct way the action done while the status was 1
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusOneTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(1);
        setSchema();

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard11.cancelAction(false);
        assertEquals(0, toolCard11.getCurrentStatus());
        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);

    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(0);
        toolCard11.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(6);
        toolCard11.cancelAction(false);
    }

    //---------------------------------------------- Not Test Methods --------------------------------------------------

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

        toolCard11.setCardWpc(wpc);

        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);
    }

}
