package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DiceTest {
    private Dice dice;
    private Color color= Color.VIOLET;

    @Before
    public void before() {
        dice = new Dice(color);
    }

    @Test
    public void checkDiceConstructor() {
        Assert.assertEquals(Color.VIOLET, dice.getDiceColour());
    }

    @Test
    public  void checkRollDice(){
        Assert.assertTrue(dice.getDiceNumber() >= 1 && dice.getDiceNumber() <= 6);
    }
}