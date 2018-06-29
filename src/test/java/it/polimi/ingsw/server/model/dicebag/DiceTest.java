package it.polimi.ingsw.server.model.dicebag;


import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.shared.clientInfo.ClientDice;
import it.polimi.ingsw.shared.exceptions.dicebagExceptions.IncorrectNumberException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(Color.VIOLET, dice1.getDiceColor());
        assertEquals(diceID , dice1.getId());
        Assert.assertTrue(dice1.getDiceNumber() >= 0 && dice1.getDiceNumber()<= 6);

        assertEquals(Color.VIOLET, dice2.getDiceColor());
        assertEquals(diceID, dice2.getId());
        assertEquals(number, dice2.getDiceNumber() );
    }

    /**
     * Tests if the client dice that has been created has the parameters value of the
     * copied dice.
     */
    @Test
    public void copyClientDiceTest(){
        assertEquals( clientDice.getDiceID(), dice2.getId());
        assertEquals( clientDice.getDiceNumber(), dice2.getDiceNumber());
        assertEquals( clientDice.getDiceColor(), dice2.getClientDice().getDiceColor());
    }

    /**
     * Checks if the new dice number is in the range.
     */
    @Test
    public void checkRollDice(){
        Assert.assertTrue(dice1.getDiceNumber() >= 1 && dice1.getDiceNumber() <= 6);
    }


    /**
     * tests if the insert number is in the correct range and sets it to the dice
     *
     * @throws IncorrectNumberException if the player has chosen an incorrect number this xception will be thrown
     */
    @Test
    public void setLegalNumberTest() throws IncorrectNumberException{
        int n0 = 6;
        int n1 = 0;
        int n2 = 3;
        dice1.setNumber(n0);
        assertEquals(n0, dice1.getDiceNumber());
        dice1.setNumber(n1);
        assertEquals(n1, dice1.getDiceNumber());
        dice1.setNumber(n2);
        assertEquals(n2, dice1.getDiceNumber());
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
        assertEquals( dice1.getId(), dice2.getId());
        assertEquals( dice1.getDiceNumber(), dice2.getDiceNumber());
        assertEquals( dice1.getDiceColor(), dice2.getDiceColor());

    }

    /**
     * Checks if the turnDiceOppositeSide method changes in a correct way the value
     */
    @Test
    public void turnDiceOppositeSideTest(){
        Dice dice = new Dice(Color.RED, 1, 1);
        dice.turnDiceOppositeSide();
        assertEquals(6, dice.getDiceNumber());
        dice.turnDiceOppositeSide();
        assertEquals(1, dice.getDiceNumber());

        Dice dice1 = new Dice(Color.BLUE, 2, 2);
        dice1.turnDiceOppositeSide();
        assertEquals(5,dice1.getDiceNumber());
        dice1.turnDiceOppositeSide();
        assertEquals(2,dice1.getDiceNumber());

        Dice dice2 = new Dice(Color.BLUE, 3, 3);
        dice2.turnDiceOppositeSide();
        assertEquals(4,dice2.getDiceNumber());
        dice2.turnDiceOppositeSide();
        assertEquals(3, dice2.getDiceNumber());
    }
}