package it.polimi.ingsw.model.dicebag;

import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DiceTest {
    private Dice dice;
    private Color color= Color.VIOLET;
    private int diceID = 1;
    @Before
    public void before() {
        dice = new Dice(color, diceID );
    }

    @Test
    public void checkDiceConstructor() {
        Assert.assertEquals(Color.VIOLET, dice.getDiceColor());
        Assert.assertEquals(1 , dice.getId());
        Assert.assertTrue(dice.getDiceNumber() >= 0 && dice.getDiceNumber()<= 6);
    }

    @Test
    public void checkRollDice(){
        Assert.assertTrue(dice.getDiceNumber() >= 1 && dice.getDiceNumber() <= 6);
    }

    @Test
    public void setLegalNumberTest() throws IncorrectNumberException{
        int n0 = 6;
        int n1 = 0;
        int n2 = 3;
        dice.setNumber(n0);
        Assert.assertEquals(n0, dice.getDiceNumber());
        dice.setNumber(n1);
        Assert.assertEquals(n1, dice.getDiceNumber());
        dice.setNumber(n2);
        Assert.assertEquals(n2, dice.getDiceNumber());
    }

    @Test(expected = IncorrectNumberException.class)
    public void illegalSetNumberTest() throws IncorrectNumberException{
        int n0 = 7;
        int n1 = -1;
        dice.setNumber(n0);
        dice.setNumber(n1);
    }
}