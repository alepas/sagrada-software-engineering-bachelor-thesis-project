package server.model.wpc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.model.dicebag.Dice;
import shared.clientInfo.Position;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceAndPositionTest {
    private Position position;
    private Dice dice;

    @Before
    public void before() {
        position = mock(Position.class);
        when(position.getRow()).thenReturn(0);
        when(position.getColumn()).thenReturn(1);

        dice = mock(Dice.class);

    }

    @Test
    public void checkPositionCOnstructor(){
        DiceAndPosition diceAndPosition = new DiceAndPosition(dice, position);

        Assert.assertEquals(0, diceAndPosition.getPosition().getRow() );
        Assert.assertEquals(1, diceAndPosition.getPosition().getColumn());
        Assert.assertEquals(dice, diceAndPosition.getDice());
    }
}
