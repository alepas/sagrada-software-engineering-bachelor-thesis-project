package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.dicebag.Dice;
import it.polimi.ingsw.model.game.RoundTrack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class roundTrackTest {
    private RoundTrack roundTrack;
    private Dice dice;
    private ArrayList[] dicesNotUsed;


    @Before
    public void Before() {
        roundTrack = new RoundTrack();
        dice = mock(Dice.class);
    }

    @Test
    public void checkRaoundTrackConstructor(){
        Assert.assertEquals(0, roundTrack.getCurrentRound());
        Assert.assertNull(roundTrack.getDicesNotUsed());
    }

}
