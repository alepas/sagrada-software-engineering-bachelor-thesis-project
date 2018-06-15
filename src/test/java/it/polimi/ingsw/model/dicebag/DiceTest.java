package it.polimi.ingsw.model.dicebag;


import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.exceptions.dicebagExceptions.IncorrectNumberException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DiceTest {
    private Dice dice1;
    private Dice dice2;
    private ClientDice clientDice;

    private Color color= Color.VIOLET;

    private int diceID = 1;
    private int number = 3;

    @Before
    public void before() {
        dice2 = new Dice(color, number, diceID);
        dice1 = new Dice(color, diceID );
        clientDice = dice2.getClientDice();

    }

    /**
     * Checks if the constructors work in a correct way.
     */
    @Test
    public void constructorsTest() {
        Assert.assertEquals(Color.VIOLET, dice1.getDiceColor());
        Assert.assertEquals(diceID , dice1.getId());
        Assert.assertTrue(dice1.getDiceNumber() >= 0 && dice1.getDiceNumber()<= 6);

        Assert.assertEquals(Color.VIOLET, dice2.getDiceColor());
        Assert.assertEquals(diceID, dice2.getId());
        Assert.assertEquals(number, dice2.getDiceNumber() );
    }

    /**
     * Tests if the client dice that has been created has the parameters value of the
     * copied dice.
     */
    @Test
    public void copyClientDiceTest(){
        Assert.assertEquals( clientDice.getDiceID(), dice2.getId());
        Assert.assertEquals( clientDice.getDiceNumber(), dice2.getDiceNumber());
        Assert.assertEquals( clientDice.getDiceColor(), dice2.getClientDice().getDiceColor());
    }

    /**
     * Checks if the new dice number is in the range.
     */
    @Test
    public void checkRollDice(){
        Assert.assertTrue(dice1.getDiceNumber() >= 1 && dice1.getDiceNumber() <= 6);
    }


    @Test
    public void setLegalNumberTest() throws IncorrectNumberException{
        int n0 = 6;
        int n1 = 0;
        int n2 = 3;
        dice1.setNumber(n0);
        Assert.assertEquals(n0, dice1.getDiceNumber());
        dice1.setNumber(n1);
        Assert.assertEquals(n1, dice1.getDiceNumber());
        dice1.setNumber(n2);
        Assert.assertEquals(n2, dice1.getDiceNumber());
    }

    @Test(expected = IncorrectNumberException.class)
    public void illegalSetNumberTest() throws IncorrectNumberException{
        int n0 = 7;
        int n1 = -1;
        dice1.setNumber(n0);
        dice1.setNumber(n1);
    }

    /**
     *checks in dice1 has the same values of dice2
     */
    @Test
    public void copyDiceTest(){
        dice1 = dice2.copyDice();
        Assert.assertEquals( dice1.getId(), dice2.getId());
        Assert.assertEquals( dice1.getDiceNumber(), dice2.getDiceNumber());
        Assert.assertEquals( dice1.getDiceColor(), dice2.getDiceColor());

    }
}