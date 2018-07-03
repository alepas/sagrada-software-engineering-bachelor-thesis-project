package server.model.game;

import org.junit.Before;
import org.junit.Test;
import server.model.configLoader.ConfigLoader;
import server.model.dicebag.Dice;
import shared.clientInfo.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoundTrackTest {
    private RoundTrack roundTrack;
    private Dice dice1;
    private Dice dice2;


    @Before
    public void Before() {
        ConfigLoader.loadConfig();
        roundTrack = new RoundTrack();
        dice1 = mock(Dice.class);
        dice2 = mock(Dice.class);
        when(dice2.getId()).thenReturn(1);
    }

    @Test
    public void roundTrackTest(){
        assertEquals(0, roundTrack.getCurrentRound());
        assertEquals(0, roundTrack.getDicesNotUsed().size());
    }

    @Test
    public void nextRoundTest(){
        roundTrack.nextRound();
        assertEquals(1, roundTrack.getCurrentRound());
    }


    /**
     * Tests if dices are correctly add to the roundTrack and the swapp works in a correct way
     */
    @Test
    public void addAndSwapDicesTest(){
        roundTrack.nextRound();
        roundTrack.addDice(dice1);
        roundTrack.addDice(dice2);
        assertEquals(2, roundTrack.getDicesNotUsed().size());
        assertEquals(2, roundTrack.getNumberOfDices());

        Position position = mock(Position.class);
        when(position.getRow()).thenReturn(0);
        when(position.getColumn()).thenReturn(1);

        assertEquals(1, roundTrack.getDiceAndPosition(dice2.getId()).getDice().getId());
        assertNull(roundTrack.getDiceAndPosition(9));


        roundTrack.swapDice(dice1, position);
        for(int i = 0; i<2; i++)
            assertEquals(dice1, roundTrack.getDicesNotUsed().get(i));

        assertEquals(1, roundTrack.getClientRoundTrack().getCurrentRound());

    }
}
