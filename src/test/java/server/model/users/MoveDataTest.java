package server.model.users;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.clientInfo.*;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MoveDataTest {
    private MoveData moveData;
    private ClientWpc wpc;
    private ClientRoundTrack roundTrack;
    private ClientDice dice;
    private ArrayList<ClientDice> extractedDices = new ArrayList<>();
    private ArrayList<Integer> number = new ArrayList<>();

    @Before
    public void Before() {
        wpc = mock(ClientWpc.class);
        roundTrack = mock(ClientRoundTrack.class);
        dice = mock(ClientDice.class);
    }

    @Test
    public void moveDataOneTest() {
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC,
                wpc, extractedDices, roundTrack, dice, ClientDiceLocations.EXTRACTED, number, false, false, null);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
        assertFalse(moveData.moveFinished);
        assertFalse(moveData.canceledToolCard);
        assertNull(moveData.exception);

    }

    @Test
    public void moveDataTwoTest() {
        Exception e = mock(Exception.class);
        moveData = new MoveData(e);
        assertEquals(e, moveData.exception);
    }

    @Test
    public void moveDataThreeTest() {
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC,
                wpc, extractedDices, roundTrack, dice, ClientDiceLocations.EXTRACTED);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
    }


    @Test
    public void moveDataFourTest() {
        ArrayList<Integer> number = new ArrayList<>();
        moveData = new MoveData(false, ClientDiceLocations.WPC, ClientDiceLocations.WPC,
                wpc, extractedDices, roundTrack, dice, ClientDiceLocations.WPC, number);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.WPC, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
        assertFalse(moveData.moveFinished);
    }

    @Test
    public void moveDataFiveTest() {
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.WPC,
                wpc, extractedDices, roundTrack, dice, ClientDiceLocations.EXTRACTED, number, false, false, null);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePutNewDice);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
        assertFalse(moveData.moveFinished);
        assertFalse(moveData.canceledToolCard);
        assertNull(moveData.exception);

    }

    @Test
    public void moveDataSixTest() {
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertNull(moveData.wpc);
        assertNull(moveData.roundTrack);

        moveData.setNextAction(NextAction.SELECT_DICE_TOOLCARD);
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
    }

    @Test
    public void moveDataSevenTest() {
        moveData = new MoveData(true);
        assertTrue(moveData.moveFinished);
        assertNull(moveData.nextAction);
        assertNull(moveData.extractedDices);
    }

    @Test
    public void moveDataEightTest() {
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, false, wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.EXTRACTED);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertFalse(moveData.moveFinished);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
    }

    @Test
    public void moveDataNineTest(){
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, false, wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.EXTRACTED, number);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
    }


    @Test
    public void moveDataTenTest(){
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, "test", false, false);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals("test",moveData.messageForStop);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);
    }

    @Test
    public void moveDataElevenTest(){
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.EXTRACTED,false);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertFalse(moveData.canceledToolCard);
    }

    @Test
    public void moveDataTwelveTest(){
        moveData = new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.WPC);
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
    }

    @Test
    public void moveDataThirteenTest(){
        moveData = new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.WPC, extractedDices);
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(extractedDices, moveData.extractedDices);
    }

    @Test
    public void moveDataFourteenTest(){
        moveData = new MoveData(true, wpc, extractedDices, roundTrack, number);
        assertTrue(moveData.moveFinished);

        assertEquals(wpc, moveData.wpc);
        assertEquals(extractedDices, moveData.extractedDices);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(number, moveData.numbersToChoose);
    }

    @Test
    public void moveDataFifteenTest(){
        moveData = new MoveData(true, wpc, extractedDices, roundTrack);
        assertTrue(moveData.moveFinished);
        assertEquals(wpc, moveData.wpc);
        assertEquals(extractedDices, moveData.extractedDices);
        assertEquals(roundTrack, moveData.roundTrack);
    }

    @Test
    public void moveDataSixteenTest(){
        moveData = new MoveData(true, true);
        assertTrue(moveData.moveFinished);
        assertTrue(moveData.canceledToolCard);
    }

    @Test
    public void moveDataSeventeenTest(){
        moveData = new MoveData(NextAction.SELECT_DICE_TOOLCARD, ClientDiceLocations.WPC, ClientDiceLocations.DICEBAG);
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.DICEBAG, moveData.wherePutNewDice);
    }

    @Test
    public void moveDataEightTeenTest(){
        moveData = new MoveData(NextAction.PLACE_DICE_TOOLCARD, wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.EXTRACTED, number, false);
        assertEquals(NextAction.PLACE_DICE_TOOLCARD, moveData.nextAction);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
        assertFalse(moveData.canceledToolCard);
    }

    @Test
    public void moveDataNineTeenTest(){
        moveData = new MoveData(true,  wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.EXTRACTED, number);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
        assertTrue(moveData.moveFinished);
    }

    @Test
    public void moveDataTwentyTest(){
        moveData = new MoveData(true,  wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.EXTRACTED);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertTrue(moveData.moveFinished);
    }

    @Test
    public void moveDataTwentyOneTest(){
        moveData = new MoveData(true,true, wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.EXTRACTED, number);
        assertEquals(wpc, moveData.wpc);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.EXTRACTED, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
        assertTrue(moveData.moveFinished);
    }

    @Test
    public void moveDataTwentyTwoTest(){
        moveData = new MoveData(true, ClientDiceLocations.WPC, ClientDiceLocations.DICEBAG,
                wpc,extractedDices, roundTrack, number);
        assertEquals(ClientDiceLocations.WPC, moveData.wherePickNewDice);
        assertEquals(ClientDiceLocations.DICEBAG, moveData.wherePutNewDice);
        assertEquals(wpc, moveData.wpc);
        assertEquals(extractedDices, moveData.extractedDices);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(number, moveData.numbersToChoose);
        assertTrue(moveData.moveFinished);
    }

    @Test
    public void moveDataTwentyThreeTest(){
        moveData = new MoveData(NextAction.SELECT_DICE_TOOLCARD, "test", false,false,  wpc, extractedDices,
                roundTrack, dice, ClientDiceLocations.WPC, number,false);
        assertEquals(NextAction.SELECT_DICE_TOOLCARD, moveData.nextAction);
        assertEquals("test", moveData.messageForStop);
        assertFalse(moveData.bothYesAndNo);
        assertFalse(moveData.showBackButton);
        assertEquals(wpc, moveData.wpc);
        assertEquals(extractedDices, moveData.extractedDices);
        assertEquals(roundTrack, moveData.roundTrack);
        assertEquals(dice, moveData.diceChosen);
        assertEquals(ClientDiceLocations.WPC, moveData.diceChosenLocation);
        assertEquals(number, moveData.numbersToChoose);
        assertFalse(moveData.canceledToolCard);
    }
}
