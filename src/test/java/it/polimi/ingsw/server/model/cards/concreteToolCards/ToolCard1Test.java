package it.polimi.ingsw.server.model.cards.concreteToolCards;

import it.polimi.ingsw.server.constants.ToolCardConstants;
import it.polimi.ingsw.server.model.cards.ToolCard;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Game;
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

public class ToolCard1Test {
    private ToolCard1 toolCard1;
    private Dice chosenDiceMiddleNumber;
    private Dice chosenDice1Number;
    private Dice chosenDice6Number;
    private PlayerInGame player;
    private Game game;
    private ArrayList<Dice> extractedDices = new ArrayList<>();
    private ClientDice clientMiddleDice;

    @Before
    public void Before() throws CannotPickDiceException {
        toolCard1 = new ToolCard1();

        game = mock(Game.class);

        chosenDiceMiddleNumber = mock(Dice.class);
        when(chosenDiceMiddleNumber.getId()).thenReturn(1);
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

        toolCard1.setCurrentToolGame(game);
        toolCard1.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard1.setToolUser(player.getUser());

        clientMiddleDice = mock(ClientDice.class);
        when(chosenDiceMiddleNumber.getClientDice()).thenReturn(clientMiddleDice);
        when(chosenDiceMiddleNumber.copyDice()).thenReturn(chosenDiceMiddleNumber);

        ClientDice clientBoundDice = mock(ClientDice.class);
        when(chosenDice1Number.getClientDice()).thenReturn(clientBoundDice);
        when(chosenDice1Number.copyDice()).thenReturn(chosenDice1Number);
    }

    /**
     * Checks if the constructor sets all parameters in a correct way
     */
    @Test
    public void toolCard1Test(){
        assertEquals("1", toolCard1.getID());
        assertEquals(ToolCardConstants.TOOL1_NAME, toolCard1.getName());
        assertEquals(ToolCardConstants.TOOL1_DESCRIPTION, toolCard1.getDescription());
        assertEquals(0, toolCard1.getCurrentStatus());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard1.getToolCardCopy();
        assertEquals(toolCard1.getID(), copy.getID());
        assertEquals(toolCard1.getName(), copy.getName());
        assertEquals(toolCard1.getDescription(), copy.getDescription());
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
        toolCard1.pickDice(chosenDiceMiddleNumber.getId());
    }


    /**
     * Tests if the pick dice works in a correct way with a dice with a number different from both 1 and 6
     * @throws CannotPickDiceException should never be trhown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     */
    @Test
    public void pickDiceMiddleValueTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        /*in the abstract class there are some setter that are used only by tests to verify the correctness of every
         single step in the use of toolCards
        */
        toolCard1.setCurrentToolPlayer(player);
        toolCard1.setCurrentToolGame(game);
        toolCard1.setCurrentToolStatus(1);

        MoveData moveData = toolCard1.pickDice(chosenDiceMiddleNumber.getId());
        assertEquals(NextAction.SELECT_NUMBER_TOOLCARD, moveData.nextAction);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
        assertNull(moveData.wherePickNewDice);
        assertEquals(2, moveData.numbersToChoose.size());
        assertEquals(2, toolCard1.getCurrentStatus());

        moveData = toolCard1.getNextMove();
        assertEquals(NextAction.SELECT_NUMBER_TOOLCARD, moveData.nextAction);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
        assertNull(moveData.wherePickNewDice);
        assertEquals(2, moveData.numbersToChoose.size());
        assertEquals(2, toolCard1.getCurrentStatus());
    }

    /**
     * Tests if the pickDice method works in a correct way with a dice with number 1 or 6.
     *
     * @throws CannotPickDiceException should never be trhown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     */
    @Test
    public void pickDiceInBoundTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        /*in the abstract class there are some setter that are used only by tests to verify the correctness of every
         single step in the use of toolCards
        */
        toolCard1.setCurrentToolPlayer(player);
        toolCard1.setCurrentToolGame(game);
        toolCard1.setCurrentToolStatus(1);

        MoveData moveData = toolCard1.pickDice(chosenDice1Number.getId());
        assertEquals(NextAction.SELECT_NUMBER_TOOLCARD, moveData.nextAction);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
        assertNull(moveData.wherePickNewDice);
        assertEquals(1, moveData.numbersToChoose.size());
        assertEquals(2, toolCard1.getCurrentStatus());

        moveData = toolCard1.getNextMove();
        assertEquals(NextAction.SELECT_NUMBER_TOOLCARD, moveData.nextAction);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);
        assertNull(moveData.wherePickNewDice);
        assertEquals(1, moveData.numbersToChoose.size());
        assertEquals(2, toolCard1.getCurrentStatus());
    }

    /**
     * @throws CannotPickNumberException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the current state is different from the expected one
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickNumberIllegalCurrentStateTest() throws CannotPickNumberException, CannotPerformThisMoveException {
        toolCard1.pickNumber(1);
    }

    /**
     * @throws CannotPickNumberException because the number isn't 1 or -1 as it should be
     * @throws CannotPerformThisMoveException in no cases because this method wants to test the other exception
     */
    @Test (expected = CannotPickNumberException.class)
    public void pickNumberIllegalNumberTest() throws CannotPickNumberException, CannotPerformThisMoveException {
        toolCard1.setCurrentToolStatus(2);
        toolCard1.pickNumber(4);
    }


    /**
     * @throws CannotPickNumberException because the number is -1 or +1 but the player try to apply a -1 to a dice with number 1
     *          or a +1 to a dice with number 6 (those two are forbidden by the game rules).
     *
     * @throws CannotPerformThisMoveException in no cases because this method wants to test the other exception
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     */
    @Test (expected = CannotPickNumberException.class)
    public void pickNumberIllegalAddToDiceTest() throws CannotPickNumberException,
            CannotPerformThisMoveException, CannotPickDiceException {
        toolCard1.setCurrentToolStatus(1);

        toolCard1.pickDice(chosenDice1Number.getId());
        toolCard1.pickNumber(-1);
    }

    /**
     * Tests if the pickNumber() method works in a correct way with a dice with  a number between 1 and 6. It has already been
     * checked if given a dice with number 1 or 6 and the wrong param number it throws an exception, for this reason it
     * can be assumed that, if a correct param number will, be given the method will work in a proper way
     *
     * @throws CannotPickNumberException  should never be thrown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     * @throws CannotPickDiceException should never be thrown in this test
     */
    @Test
    public void pickNumberTest() throws CannotPickDiceException, CannotPerformThisMoveException, CannotPickNumberException {
        toolCard1.setCurrentToolStatus(1);
        toolCard1.pickDice(chosenDiceMiddleNumber.getId());
        MoveData moveData = toolCard1.pickNumber(1);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertEquals(clientMiddleDice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(3, toolCard1.getCurrentStatus());

        moveData = toolCard1.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertEquals(clientMiddleDice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(3, toolCard1.getCurrentStatus());
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
        toolCard1.placeDice(chosenDice6Number.getId(), position );
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPickPositionException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test(expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard1.setCurrentToolStatus(3);
        toolCard1.placeDice(chosenDice6Number.getId(), null );
    }

    /**
     * @throws CannotPickDiceException because the id of the chhosen dice is different from the one of the modified dice
     * @throws CannotPickPositionException in no cases because this method wants to test the other exception
     * @throws CannotPerformThisMoveException in no cases because this method wants to test the other exception
     */
    @Test(expected = CannotPickDiceException.class)
    public void placeDiceIllegalDiceIdTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException, CannotPickNumberException {
        toolCard1.setCurrentToolStatus(1);
        toolCard1.pickDice(chosenDice1Number.getId());
        toolCard1.pickNumber(1);

        Position position = mock(Position.class);
        toolCard1.placeDice(chosenDice6Number.getId(), position );
    }

    /**
     * @throws CannotPickDiceException in no cases because this method wants to test the other exception
     * @throws CannotPickPositionException because the chosen position doens't respect the position rules
     * @throws CannotPerformThisMoveException in no cases because this method wants to test the other exception
     */
    @Test(expected = CannotPickPositionException.class)
    public void placeDiceIllegalDicePositionInWpcTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException, CannotPickNumberException {
        toolCard1.setCurrentToolStatus(1);
        toolCard1.pickDice(chosenDice1Number.getId());
        toolCard1.pickNumber(1);

        setSchema();
        Position position = mock(Position.class);
        when(position.getRow()).thenReturn(1);
        when(position.getColumn()).thenReturn(1);
        toolCard1.placeDice(chosenDice1Number.getId(), position );
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
        toolCard1.setCurrentToolStatus(1);
        toolCard1.pickDice(chosenDice1Number.getId());
        toolCard1.pickNumber(1);

        setSchema();
        Position position = mock(Position.class);
        when(position.getRow()).thenReturn(0);
        when(position.getColumn()).thenReturn(0);
        when(player.getWPC().addDiceWithAllRestrictions(chosenDice1Number, position)).thenReturn(true);
        MoveData moveData = toolCard1.placeDice(chosenDice1Number.getId(), position);
        assertTrue(moveData.moveFinished);
        assertEquals(2, game.getExtractedDices().size());

        /*at the end of placeDice() method the clean on is called, those assertions checks if it reset parameters in
          a correct way: Both dice and oldDice must be null and the arrayList size must be zero
        */
        assertNull(toolCard1.getToolDice());
        assertNull(toolCard1.getOldDice());
        assertEquals(0, toolCard1.getNumeberSize());

        moveData = toolCard1.getNextMove();
        assertNull(moveData);

    }

    /**
     * Tests for status equals to 0 and 1 if the nextMove() method works in a correct way. It doesn't test status 2 and 3
     * bacuse they have been already tested in the methods above
     */
    @Test
    public void nextMoveTest(){
        MoveData moveData = toolCard1.getNextMove();
        assertNull(moveData);

        toolCard1.setCurrentToolStatus(1);
        moveData = toolCard1.getNextMove();
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertNull(moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);
    }

    /**
     * @throws CannotInteruptToolCardException every time that is called
     */
    @Test(expected = CannotInteruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInteruptToolCardException {
        toolCard1.interruptToolCard(ToolCardInteruptValues.OK);
    }

//todo: cancel method
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
    }

}
