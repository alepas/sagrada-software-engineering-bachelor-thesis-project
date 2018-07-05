package server.model.cards.concretetoolcards;

import org.junit.Assert;
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
import shared.constants.ToolcardConstants;
import shared.exceptions.usersAndDatabaseExceptions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCard8Test {
    private ToolCard8 toolCard8;
    private Game game;
    private Dice chosenDice;
    private Dice dice;
    private PlayerInGame player;
    private Position position;
    private Wpc wpc;
    private MoveData moveData;
    private ArrayList<Dice> extractedDices = new ArrayList<>();

    @Before
    public void Before() throws CannotPickDiceException {

        ConfigLoader.loadConfig();
        toolCard8 = new ToolCard8();
        player = mock(PlayerInGame.class);
        game = mock(Game.class);

        when(player.getGame()).thenReturn(game);

        chosenDice = mock(Dice.class);
        when(chosenDice.getId()).thenReturn(1);
        when(chosenDice.getDiceColor()).thenReturn(Color.RED);
        when(chosenDice.getDiceNumber()).thenReturn(4);
        extractedDices.add(chosenDice);

        dice = mock(Dice.class);
        extractedDices.add(dice);

        //mock the DiceAndPosition to do not have integration problems with the playerInGame class
        DiceAndPosition diceAndPosition1 = mock(DiceAndPosition.class);
        when(diceAndPosition1.getDice()). thenReturn(chosenDice);
        when(diceAndPosition1.getPosition()).thenReturn(null);

        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.WPC)).thenReturn(diceAndPosition1);
        when(player.dicePresentInLocation(chosenDice.getId(), ClientDiceLocations.EXTRACTED)).thenReturn(diceAndPosition1);
        when(player.getTurnForRound()).thenReturn(1);

        toolCard8.setCurrentToolGame(game);
        toolCard8.setCurrentToolPlayer(player);
        when(player.getUser()).thenReturn("Username");
        toolCard8.setToolUser(player.getUser());

        position = mock(Position.class);
        when(position.getColumn()).thenReturn(1);
        when(position.getRow()).thenReturn(1);

        ClientDice clientDice = mock(ClientDice.class);
        when(chosenDice.getClientDice()).thenReturn(clientDice);
        when(diceAndPosition1.getDice().getClientDice()).thenReturn(clientDice);

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
        assertEquals("8", toolCard8.getID());
        assertEquals(ToolcardConstants.TOOL8_NAME, toolCard8.getName());
        assertEquals(ToolcardConstants.TOOL8_DESCRIPTION, toolCard8.getDescription());
        assertEquals(0, toolCard8.getCurrentStatus());
    }

    /**
     * Checks if a copy of the object is well created.
     */
    @Test
    public void copyToolCard1Test(){
        ToolCard copy = toolCard8.getToolCardCopy();
        assertEquals(toolCard8.getID(), copy.getID());
        assertEquals(toolCard8.getName(), copy.getName());
        assertEquals(toolCard8.getDescription(), copy.getDescription());
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

        toolCard8.setCurrentToolPlayer(null);
        moveData = toolCard8.setCard(player);
        assertEquals(NextAction.SELECT_DICE_TO_ACTIVATE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
    }

    /**
     * Tests if the tool is initialized in a correct way if the game is a multi player game and the player
     * has already placed a dice.
     *
     * @throws CannotUseToolCardException not in this test
     */
    @Test
    public void setCardIllegalMultiPlayerGameTest() throws CannotUseToolCardException {
        setSchema();
        when(game.isSinglePlayerGame()).thenReturn(false);
        when(player.isPlacedDiceInTurn()).thenReturn(true);
        toolCard8.setCurrentToolPlayer(null);
        moveData = toolCard8.setCard(player);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertEquals(10, toolCard8.getCurrentStatus());
    }

    /**
     * Tests if the tool is initialized in a correct way if the game is a multi player game and the player
     * hasn't already placed a dice.
     *
     * @throws CannotUseToolCardException not in this test
     */
    @Test
    public void setCardMultiPlayerGameTest() throws CannotUseToolCardException {
        setSchema();
        when(game.isSinglePlayerGame()).thenReturn(false);
        when(player.isPlacedDiceInTurn()).thenReturn(false);
        toolCard8.setCurrentToolPlayer(null);
        moveData = toolCard8.setCard(player);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertEquals(1, toolCard8.getCurrentStatus());
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the state is different from 1
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalStatusTest () throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException {
        toolCard8.setCurrentToolStatus(0);
        toolCard8.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionStatusOneTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard8.setCurrentToolStatus(1);
        toolCard8.placeDice(chosenDice.getId(), null);
    }

    /**
     * @throws CannotPickDiceException not in this test
     * @throws CannotPickPositionException because it is not possible to set the dice in the chosen position
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test (expected = CannotPickPositionException.class)
    public void placeDiceIllegalWpcPositionStatusOneTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        setSchema();
        toolCard8.setCurrentToolStatus(1);
        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(false);
        toolCard8.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException not in this test
     * @throws CannotPickPositionException because it is not possible to set the dice in the chosen position
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void placeDiceFromStatusOneToThirtyTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException, CannotInteruptToolCardException {
        setSchema();
        toolCard8.setCurrentToolStatus(1);

        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(true);
        moveData = toolCard8.placeDice(chosenDice.getId(), position);

        assertEquals(30, toolCard8.getCurrentStatus());
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);


        assertNotNull(moveData.messageForStop);
        assertNotNull(moveData.extractedDices);
        assertNotNull(moveData.wpc);

        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard8.setCurrentToolStatus(30);

        MoveData moveData = toolCard8.interruptToolCard(ToolCardInteruptValues.OK);
        assertTrue(moveData.canceledToolCard);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);
        assertNotNull(moveData.wpc);
        assertNull(moveData.roundTrack);
    }

    @Test
    public void placeDiceFromStatusOneToStatusTwoTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException, CannotCancelActionException {
        setSchema();
        toolCard8.setCardExtractedDices(extractedDices);

        when(wpc.isDicePlaceable(dice)).thenReturn(true);
        toolCard8.setCurrentToolStatus(1);
        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(true);
        moveData = toolCard8.placeDice(chosenDice.getId(), position);

        assertEquals(2, toolCard8.getCurrentStatus());

        toolCard8.cancelAction(true);
        assertFalse(moveData.canceledToolCard);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);
        assertNotNull(moveData.wpc);
        assertNull(moveData.roundTrack);
    }


    /**
     * @throws CannotPickDiceException not in this test
     * @throws CannotPickPositionException because it is not possible to set the dice in the chosen position
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test (expected = CannotPickPositionException.class)
    public void placeDiceIllegalWpcPositionStatusTwoTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        setSchema();
        toolCard8.setCurrentToolStatus(2);
        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(false);
        toolCard8.placeDice(chosenDice.getId(), position);
    }

    /**
     * @throws CannotPickDiceException in any case because this method wants to test the other exception
     * @throws CannotPickPositionException in any case because this method wants to test the other exception
     * @throws CannotPerformThisMoveException because the position is null
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void placeDiceIllegalPositionStatusTwoTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        toolCard8.setCurrentToolStatus(2);
        toolCard8.placeDice(chosenDice.getId(), null);
    }


    /**
     * @throws CannotPickDiceException not in this test
     * @throws CannotPickPositionException because it is not possible to set the dice in the chosen position
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void placeDiceStatusTwoTest () throws CannotPerformThisMoveException,
            CannotPickPositionException, CannotPickDiceException {
        setSchema();
        toolCard8.setCurrentToolStatus(2);
        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(true);
        moveData = toolCard8.placeDice(chosenDice.getId(), position);

        assertTrue(moveData.moveFinished);

        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);
    }

    /**
     * @throws CannotPickDiceException because the dice color is different from the request
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test (expected = CannotPickDiceException.class)
    public void pickDiceWrongColorTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard8.setSinglePlayerGame(true);
        when(chosenDice.getDiceColor()).thenReturn(Color.YELLOW);
        toolCard8.pickDice(chosenDice.getId());
    }

    /**
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException because the pickdice() method can't be called during a multi
     * player game
     */
    @Test (expected = CannotPerformThisMoveException.class)
    public void pickDiceMultiPlayerTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard8.setSinglePlayerGame(false);
        toolCard8.pickDice(chosenDice.getId());
    }

    /**
     * Checks if the pickDice() method works in a correct way.
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void pickDiceNotPlacedYetTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard8.setSinglePlayerGame(true);
        MoveData moveData = toolCard8.pickDice(chosenDice.getId());
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
    }

    /**
     * Checks if the pickDice() method works in a correct way.
     * @throws CannotPickDiceException not in this test
     * @throws CannotPerformThisMoveException not in this test
     */
    @Test
    public void pickDicePlacedTest() throws CannotPickDiceException, CannotPerformThisMoveException {
        toolCard8.setSinglePlayerGame(true);
        when(player.isPlacedDiceInTurn()).thenReturn(true);

        MoveData moveData = toolCard8.pickDice(chosenDice.getId());
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
    }

    /**
     * @throws CannotPerformThisMoveException every time that this method is called
     */
    @Test ( expected = CannotPerformThisMoveException.class)
    public void pickNumberTest() throws CannotPerformThisMoveException {
        toolCard8.pickNumber(5);
    }

    /**
     * @throws CannotCancelActionException because the boolean is false and the status isn't a valid one
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusTest() throws CannotCancelActionException {
        toolCard8.setCurrentToolStatus(3);
        toolCard8.cancelAction(false);
    }

    /**
     * @throws CannotCancelActionException because the game is a multi player game
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationIllegalStatusZeroTest() throws CannotCancelActionException {
        toolCard8.setCurrentToolStatus(0);
        toolCard8.cancelAction(false);
    }


    /**
     * doesn't throw a the exception because it's a single player game
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStatusZeroSinglePlayerTest() throws CannotCancelActionException {
        toolCard8.setSinglePlayerGame(true);
        moveData = toolCard8.cancelAction(false);

        Assert.assertTrue(moveData.moveFinished);
        Assert.assertTrue(moveData.canceledToolCard);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusZeroTest() throws CannotCancelActionException {
        toolCard8.setCurrentToolStatus(0);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        RoundTrack roundTrack = mock(RoundTrack.class);
        ClientRoundTrack clientRoundTrack = mock(ClientRoundTrack.class);
        when(roundTrack.getClientRoundTrack()).thenReturn(clientRoundTrack);
        when(game.getRoundTrack()).thenReturn(roundTrack);

        MoveData moveData = toolCard8.cancelAction(true);
        Assert.assertTrue(moveData.moveFinished);
        Assert.assertTrue(moveData.canceledToolCard);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException because the status doesn't exist
     */
    @Test (expected = CannotCancelActionException.class)
    public void cancelLastOperationStatusTwoTest() throws CannotCancelActionException {
        toolCard8.setCurrentToolStatus(45);
        toolCard8.cancelAction(true);
    }

    /**
     * Tests if it is possible to cancel in a correct way the first action at status 0
     *
     * @throws CannotCancelActionException in any case in this test
     */
    @Test
    public void cancelLastOperationStatusOneTest() throws CannotCancelActionException {
        toolCard8.setCurrentToolStatus(1);
        setSchema();
        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

        toolCard8.cancelAction(false);
        assertEquals(0, toolCard8.getCurrentStatus());
    }

    /**
     * checks if the cancel method works in a correct way when the status is 30
     * @throws CannotPerformThisMoveException not in this test
     * @throws CannotPickPositionException not in this test
     * @throws CannotPickDiceException not in this test
     * @throws CannotCancelActionException not in this test
     */
    @Test
    public void cancelLastOperationStatusThirtyTest() throws CannotPerformThisMoveException, CannotPickPositionException, CannotPickDiceException, CannotCancelActionException {
        setSchema();
        toolCard8.setCurrentToolStatus(1);
        when(wpc.addDiceWithAllRestrictions(chosenDice, position)).thenReturn(true);
        moveData = toolCard8.placeDice(chosenDice.getId(), position);

        assertEquals(30, toolCard8.getCurrentStatus());
        assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);


        assertNotNull(moveData.messageForStop);
        assertNotNull(moveData.extractedDices);
        assertNotNull(moveData.wpc);

        assertNull(moveData.roundTrack);
        assertNull(moveData.diceChosen);
        assertNull(moveData.diceChosenLocation);

        toolCard8.cancelAction(true);
        assertFalse(moveData.canceledToolCard);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);
        assertNotNull(moveData.wpc);
        assertNull(moveData.roundTrack);
    }


    /**
     * @throws CannotInteruptToolCardException because the value isn't equivalent to OK
     */
    @Test (expected = CannotInteruptToolCardException.class)
    public void interruptToolCardValueNoTest() throws CannotInteruptToolCardException {
        setSchema();
        toolCard8.setCurrentToolStatus(30);
        MoveData moveData = toolCard8.interruptToolCard(ToolCardInteruptValues.NO);

        Assert.assertTrue( moveData.moveFinished);

        assertNull(moveData.roundTrack);
        assertNull(moveData.wpc);
        assertNull(moveData.extractedDices);
    }

    /**
     * @throws CannotInteruptToolCardException because the status is different from 30.
     */
    @Test (expected = CannotInteruptToolCardException.class)
    public void interruptToolCardIllegalStatusTest() throws CannotInteruptToolCardException {
        setSchema();
        toolCard8.setCurrentToolStatus(10);
        MoveData moveData = toolCard8.interruptToolCard(ToolCardInteruptValues.OK);

        Assert.assertTrue( moveData.moveFinished);

        assertNull(moveData.roundTrack);
        assertNull(moveData.wpc);
        assertNull(moveData.extractedDices);
    }




     /**
      * Tests if the nextMove() method works in a correct way with all possible status
      */
     @Test
     public void nextMoveTest(){
         MoveData moveData = toolCard8.getNextMove();
         assertNull(moveData);

         toolCard8.setCurrentToolStatus(1);
         moveData = toolCard8.getNextMove();
         assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
         assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
         assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
         assertNull(moveData.wpc);
         assertNotNull(moveData.extractedDices);
         assertNull(moveData.roundTrack);
         assertNull(moveData.diceChosen);
         assertNull(moveData.diceChosenLocation);

         toolCard8.setCurrentToolStatus(2);
         moveData = toolCard8.getNextMove();
         assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
         assertEquals(ClientDiceLocations.EXTRACTED, moveData.wherePickNewDice);
         assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
         assertNull(moveData.wpc);
         assertNotNull(moveData.extractedDices);
         assertNull(moveData.roundTrack);
         assertNull(moveData.diceChosen);
         assertNull(moveData.diceChosenLocation);

         toolCard8.setCurrentToolStatus(30);
         moveData = toolCard8.getNextMove();
         assertEquals(NextAction.INTERRUPT_TOOLCARD, moveData.nextAction);
         assertEquals("Nessun dado tra quelli estratti può essere posizionato sulla Window Pattern Card.\n" +
                 "Il primo piazzamento è considerato valido. L'utilizzo della toolCard è stato annullato.", moveData.messageForStop);

         assertFalse(moveData.bothYesAndNo);
         assertFalse(moveData.canceledToolCard);

         assertNull(moveData.wpc);
         assertNotNull(moveData.extractedDices);
         assertNull(moveData.roundTrack);
         assertNull(moveData.diceChosen);
         assertNull(moveData.diceChosenLocation);

         toolCard8.setCurrentToolStatus(4);
         assertNull(toolCard8.getNextMove());
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

        toolCard8.setCardWpc(wpc);

        ClientWpc clientWpc = mock(ClientWpc.class);
        when(wpc.getClientWpc()).thenReturn(clientWpc);

    }
}
