package server.model.cards.concretetoolcards;

import org.junit.Before;
import org.junit.Test;
import server.model.cards.ToolCard;
import server.model.configLoader.ConfigLoader;
import server.model.dicebag.Color;
import server.model.dicebag.Dice;
import server.model.dicebag.DiceBag;
import server.model.game.Game;
import server.model.game.RoundTrack;
import server.model.users.MoveData;
import server.model.users.PlayerInGame;
import server.model.wpc.Cell;
import server.model.wpc.DiceAndPosition;
import server.model.wpc.Wpc;
import shared.clientInfo.*;
import shared.constants.ToolcardConstants;
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
    private Position position;
    private MoveData moveData;
    private Wpc wpc;

    @Before
    public void Before() throws CannotPickDiceException {

        ConfigLoader.loadConfig();
        toolCard11 = new ToolCard11();

        game = mock(Game.class);

        chosenDiceMiddleNumber = mock(Dice.class);
        ClientDice dice1 = mock(ClientDice.class);
        when(chosenDiceMiddleNumber.getId()).thenReturn(1);
        when(chosenDiceMiddleNumber.getDiceColor()).thenReturn(Color.VIOLET);
        when(chosenDiceMiddleNumber.getDiceNumber()).thenReturn(4);
        when(chosenDiceMiddleNumber.getClientDice()).thenReturn(dice1);

        chosenDice1Number = mock(Dice.class);
        ClientDice dice2 = mock(ClientDice.class);
        when(chosenDice1Number.getId()).thenReturn(2);
        when(chosenDice1Number.getDiceColor()).thenReturn(Color.RED);
        when(chosenDice1Number.getDiceNumber()).thenReturn(1);
        when(chosenDice1Number.getClientDice()).thenReturn(dice2);

        chosenDice6Number = mock(Dice.class);
        ClientDice dice3 = mock(ClientDice.class);
        when(chosenDice6Number.getId()).thenReturn(3);
        when(chosenDice6Number.getDiceColor()).thenReturn(Color.GREEN);
        when(chosenDice6Number.getDiceNumber()).thenReturn(6);
        when(chosenDice6Number.getClientDice()).thenReturn(dice3);

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

        ClientDice clientMiddleDice = mock(ClientDice.class);
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

        DiceBag diceBag = mock(DiceBag.class);
        when(game.getDiceBag()).thenReturn(diceBag);
        when(diceBag.pickDice()).thenReturn(chosenDice1Number);
    }


    /**
     * Checks if the constructor sets all parameters in a correct way
     */
    @Test
    public void toolCard1Test(){
        assertEquals("11", toolCard11.getID());
        assertEquals(ToolcardConstants.TOOL11_NAME, toolCard11.getName());
        assertEquals(ToolcardConstants.TOOL11_DESCRIPTION, toolCard11.getDescription());
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
        assertEquals(ClientDiceLocations.DICEBAG,moveData.wherePutNewDice);
    }


    /**
     * the current state is still equals to zero but the game isn't a single player game so the cannot perform this
     * action exception will be thrown.
     *
     * @throws CannotPickDiceException when it is not possible to pick the chosen dice
     * @throws CannotPerformThisMoveException when it is not possible to perform the chosen action
     */
    @Test(expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalStatusTest() throws CannotPickDiceException, CannotPerformThisMoveException, CannotPickPositionException {
        toolCard11.placeDice(chosenDiceMiddleNumber.getId(),position);
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
     * @throws CannotPickDiceException not in this test
     * @throws CannotPickPositionException not in this case
     * @throws CannotPerformThisMoveException because there's a position different from null while the state is 1
     */
    @Test(expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalDicePositionInWpcTest() throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard11.setCurrentToolStatus(1);
        toolCard11.placeDice(chosenDice1Number.getId(),position);
    }


    /**
     * Tests if the placeDice() method works in a correct way if the chosen dice is placeable in the chosen position
     *
     * @throws CannotPickDiceException should never be thrown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     * @throws CannotPickNumberException should never be thrown in this test
     * @throws CannotPickPositionException should never be thrown in this test
     */
    @Test
    public void placeDiceTest() throws CannotPickDiceException, CannotPerformThisMoveException,
            CannotPickNumberException, CannotPickPositionException, CannotInterruptToolCardException {
        setSchema();
        toolCard11.setCardExtractedDices(extractedDices);
        toolCard11.setCurrentToolStatus(1);
        moveData = toolCard11.placeDice(chosenDice1Number.getId(), null);

        assertEquals(20, toolCard11.getCurrentStatus());
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);

        moveData = toolCard11.getNextMove();
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertEquals("Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.", moveData.messageForStop);
        assertTrue(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);

        moveData = toolCard11.interruptToolCard(ToolCardInteruptValues.YES);

        when(wpc.isDicePlaceable(chosenDice1Number)).thenReturn(true);
        moveData = toolCard11.pickNumber(3);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(3, toolCard11.getCurrentStatus());

        when(toolCard11.getCardWpc().addDiceWithAllRestrictions(chosenDice1Number, position)).thenReturn(true);
        MoveData moveData = toolCard11.placeDice(chosenDice1Number.getId(), position);
        assertTrue(moveData.moveFinished);

        moveData = toolCard11.getNextMove();
        assertNull(moveData);

    }

    /**
     * Tests if the cancelAction() method works in a correct way the status is 3.
     *
     * @throws CannotPickDiceException should never be thrown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     * @throws CannotPickNumberException should never be thrown in this test
     * @throws CannotPickPositionException should never be thrown in this test
     */
    @Test
    public void cancelLastActionStatusThreeTest() throws CannotPickDiceException, CannotPerformThisMoveException,
            CannotPickNumberException, CannotPickPositionException, CannotInterruptToolCardException, CannotCancelActionException {
        setSchema();
        toolCard11.setCardExtractedDices(extractedDices);
        toolCard11.setCurrentToolStatus(1);
        moveData = toolCard11.placeDice(chosenDice1Number.getId(), null);

        assertEquals(20, toolCard11.getCurrentStatus());
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);

        moveData = toolCard11.getNextMove();
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertEquals("Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.", moveData.messageForStop);
        assertTrue(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);

        moveData = toolCard11.interruptToolCard(ToolCardInteruptValues.YES);

        when(wpc.isDicePlaceable(chosenDice1Number)).thenReturn(true);
        moveData = toolCard11.pickNumber(3);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(3, toolCard11.getCurrentStatus());

        moveData = toolCard11.cancelAction(false);
        assertEquals(NextAction.SELECT_NUMBER_TOOLCARD, moveData.nextAction);
    }

    /**
     * Tests if the placeDice() method works in a correct way if the chosen dice isn't placeable in the chosen position
     *
     * @throws CannotPickDiceException should never be thrown in this test
     * @throws CannotPerformThisMoveException should never be thrown in this test
     * @throws CannotPickNumberException should never be thrown in this test
     * @throws CannotPickPositionException should never be thrown in this test
     */
    @Test
    public void placeDiceIllegalPlacementTest() throws CannotPickDiceException, CannotPerformThisMoveException,
            CannotPickNumberException, CannotPickPositionException, CannotInterruptToolCardException {
        setSchema();
        toolCard11.setCardExtractedDices(extractedDices);
        toolCard11.setCurrentToolStatus(1);
        moveData = toolCard11.placeDice(chosenDice1Number.getId(), null);

        assertEquals(20, toolCard11.getCurrentStatus());
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);

        moveData = toolCard11.getNextMove();
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertEquals("Vuoi scegliere un valore e posizionare il dado estratto? Premi su Yes per posizionarlo, No per lasciarlo nei dadi estratti e terminare il tuo turno.", moveData.messageForStop);
        assertTrue(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);

        moveData = toolCard11.interruptToolCard(ToolCardInteruptValues.YES);

        when(wpc.isDicePlaceable(chosenDice1Number)).thenReturn(false);
        moveData = toolCard11.pickNumber(3);

        assertEquals("Il dado non può essere posizionato sulla Window Pattern Card. Continuando verrà riposizionato nei dadi estratti", moveData.messageForStop);
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertTrue(moveData.showBackButton);
        assertFalse(moveData.bothYesAndNo);
    }

    /**
     * Tests for status equals to 0 and 1 if the nextMove() method works in a correct way. It doesn't test status 2 and 3
     * because they have been already tested in the methods above
     */
    @Test
    public void nextMoveTest(){
        MoveData moveData = toolCard11.getNextMove();
        assertNull(moveData);

        toolCard11.setCurrentToolStatus(1);
        moveData = toolCard11.getNextMove();
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.DICEBAG, moveData.wherePutNewDice);
        assertNull(moveData.wpc);
        assertNotNull(moveData.extractedDices);
        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard11.setCurrentToolStatus(4);
        assertNull(toolCard11.getNextMove());
    }


    /**
     * @throws CannotInterruptToolCardException every time that is called
     */
    @Test(expected = CannotInterruptToolCardException.class)
    public void interruptToolCardTest() throws CannotInterruptToolCardException {
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
     * doesn't throw a the exception because it's a single player game
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStatusZeroSinglePlayerTest() throws CannotCancelActionException {
        toolCard11.setSinglePlayerGame(true);
        moveData = toolCard11.cancelAction(false);

        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);
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
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalOperationTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(5);
        toolCard11.cancelAction(true);
    }

    /**
     * checks if the cancel works in a crrect way when the status is 3 and the boolean all is true
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStatusThreeTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(3);
        moveData = toolCard11.cancelAction(true);

        assertTrue(moveData.moveFinished);
    }

    /**
     * checks if the cancel works in a correct way when the status is 3 and the boolean all is true
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStatusThirtyTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(30);
        moveData = toolCard11.cancelAction(true);

        assertTrue(moveData.moveFinished);
    }



    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard11.setCurrentToolStatus(6);
        toolCard11.cancelAction(false);
    }

    /**
     * checks if the cancelAction() method works in a correct way when the status is equal to 2.
     *
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     * @throws CannotPickPositionException not in this test
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelStatusTwoTest() throws CannotPickDiceException, CannotPerformThisMoveException,
             CannotPickPositionException, CannotCancelActionException {
        setSchema();
        toolCard11.setCardExtractedDices(extractedDices);
        toolCard11.setCurrentToolStatus(1);
        toolCard11.placeDice(chosenDice1Number.getId(), null);

        toolCard11.setCurrentToolStatus(2);
        assertEquals(2, toolCard11.getCurrentStatus());
        moveData = toolCard11.cancelAction(true);

    }

    /**
     * checks if the cancelAction() method works in a correct way when the status is equal to 20.
     *
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     * @throws CannotPickPositionException not in this test
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelStatusTwentyTest() throws CannotPickDiceException, CannotPerformThisMoveException,
            CannotPickPositionException, CannotCancelActionException {
        setSchema();
        toolCard11.setCardExtractedDices(extractedDices);
        toolCard11.setCurrentToolStatus(1);
        toolCard11.placeDice(chosenDice1Number.getId(), null);

        assertEquals(20, toolCard11.getCurrentStatus());
        moveData = toolCard11.cancelAction(true);

    }


    /**
     * @throws CannotInterruptToolCardException because the value should be ok when the status is 30
     */
    @Test (expected = CannotInterruptToolCardException.class)
    public void interruptToolCardValueNotOkTest() throws CannotInterruptToolCardException {
        toolCard11.setCurrentToolStatus(30);
        moveData = toolCard11.interruptToolCard(ToolCardInteruptValues.NO);
    }

    /**
     * @throws CannotInterruptToolCardException because the value should be no when the status is 20
     */
    @Test (expected = CannotInterruptToolCardException.class)
    public void interruptToolCardValueNotNoTest() throws CannotInterruptToolCardException {
        toolCard11.setCurrentToolStatus(20);
        moveData = toolCard11.interruptToolCard(ToolCardInteruptValues.OK);
    }

    @Test
    public void interruptToolCardStatusTwentyTest() throws CannotPickDiceException, CannotPerformThisMoveException,
            CannotPickPositionException, CannotInterruptToolCardException {
        setSchema();
        toolCard11.setCardExtractedDices(extractedDices);
        toolCard11.setCurrentToolStatus(1);
        moveData = toolCard11.placeDice(chosenDice1Number.getId(), null);

        moveData = toolCard11.interruptToolCard(ToolCardInteruptValues.NO);
        assertTrue(moveData.moveFinished);
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

