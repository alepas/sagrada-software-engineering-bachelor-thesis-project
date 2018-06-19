package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.game.RoundTrack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.zip.DeflaterInputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class RoundTrackTest {
    private RoundTrack roundTrack;
    private Dice dice0;
    private Dice dice1;
    private Dice dice2;
    private Dice dice3;
    private Dice dice4;
    private Dice dice5;
    private Dice dice6;
    private Dice[][] dicesNotUsed;


    @Before
    public void Before() {
        roundTrack = new RoundTrack();
        dicesNotUsed = new Dice[4][4];
        dice0 = mock(Dice.class);
        dice1 = mock(Dice.class);
        dice2 = mock(Dice.class);
        when(dice2.getId()).thenReturn(1);
        dice3 = mock(Dice.class);
        dice4 = mock(Dice.class);
        dice5 = mock(Dice.class);
        dice6 = mock(Dice.class);

        dicesNotUsed[0][0] = dice0;
        dicesNotUsed[0][1] = dice1;
        dicesNotUsed[0][2] = dice2;
        dicesNotUsed[1][0] = dice3;
        dicesNotUsed[1][1] = dice4;
        dicesNotUsed[1][2] = dice5;
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

        Position position = mock(Position.class);
        when(position.getRow()).thenReturn(0);
        when(position.getColumn()).thenReturn(1);
        Dice dice = roundTrack.swapDice(dice1, position);
        for(int i = 0; i<2; i++)
            assertEquals(dice1, roundTrack.getDicesNotUsed().get(i));

        //assertEquals(dice2.getId(), dice.getId());

    }
}
