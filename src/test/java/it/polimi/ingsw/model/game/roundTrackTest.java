package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.game.RoundTrack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.zip.DeflaterInputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

public class roundTrackTest {
    private RoundTrack roundTrack;
    private Dice dice0;
    private Dice dice1;
    private Dice dice2;
    private Dice[][] dicesNotUsed;


    @Before
    public void Before() {
        roundTrack = new RoundTrack();
        dice0 = mock(Dice.class);
        dice1 = mock(Dice.class);

    }

    @Test
    public void checkRaoundTrackConstructor(){
        Assert.assertEquals(0, roundTrack.getCurrentRound());
        Assert.assertEquals(0, roundTrack.getDicesNotUsed().size());
    }

    @Test
    public void nextRoundTest(){
        roundTrack.nextRound();
        Assert.assertEquals(1, roundTrack.getCurrentRound());
    }

    @Test
    public void addDiceTest(){
        roundTrack.nextRound();
        dice2 = mock(Dice.class);
        roundTrack.addDice(dice0);
        Assert.assertEquals(1, roundTrack.getDicesNotUsed().size());
    }

    @Test
    public void swapDiceTest(){
        roundTrack.nextRound();
        roundTrack.addDice(dice0);
        dice1 = mock(Dice.class);
        roundTrack.swapDice(dice1, dice0, 1);
        Assert.assertEquals(dice1, roundTrack.swapDice(dice1, dice0, 1));

    }
}
