package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceBagTest {
    private DiceBag diceBag;
    private Dice violetDice;
    private Dice bluDice;
    private Dice redDice;
    private Dice yellowDice;
    private Dice greenDice;

    @Before
    public void before() {
        diceBag = new DiceBag();
        violetDice = mock(Dice.class);
        when(violetDice.getDiceColor()).thenReturn(Color.VIOLET);

        bluDice = mock(Dice.class);
        when(bluDice.getDiceColor()).thenReturn(Color.BLUE);

        redDice = mock(Dice.class);
        when(redDice.getDiceColor()).thenReturn(Color.RED);

        yellowDice = mock(Dice.class);
        when(yellowDice.getDiceColor()).thenReturn(Color.YELLOW);

        greenDice = mock(Dice.class);
        when(greenDice.getDiceColor()).thenReturn(Color.GREEN);
    }

    @Test
    public void checkDiceBagConstructor(){
        Assert.assertEquals(0, diceBag.getDiceIdGenerator());
        Assert.assertEquals(18, diceBag.getBlueDices());
        Assert.assertEquals(18, diceBag.getGreenDices());
        Assert.assertEquals(18, diceBag.getRedDices());
        Assert.assertEquals(18, diceBag.getVioletDices());
        Assert.assertEquals(18, diceBag.getYellowDices());
    }

    @Test
    public void checkDicesExtraction() {
        int soloplayer = 1;
        int twoPlayers = 2;
        int threePlayers = 3;
        int fourPlayers = 4;
        Assert.assertEquals(4, diceBag.extractDices(soloplayer).size());
        Assert.assertEquals(86, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));

        Assert.assertEquals(5, diceBag.extractDices(twoPlayers).size());
        Assert.assertEquals(81, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));

        Assert.assertEquals(7, diceBag.extractDices(threePlayers).size());
        Assert.assertEquals(74, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));

        Assert.assertEquals(9, diceBag.extractDices(fourPlayers).size());
        Assert.assertEquals(65, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));
    }

    /**
     * Checks if the number of dices is increased of one when a dice of the same color is reintroduces
     */
    @Test
    public void reInsertDiceTest() {
        int numPlayers = 4;
        diceBag.extractDices(numPlayers);
        diceBag.extractDices(numPlayers);

        int i = diceBag.getVioletDices();
        diceBag.reInsertDice(violetDice);
        if(i == 18) Assert.assertEquals(i, diceBag.getVioletDices());
        else Assert.assertEquals(i+1, diceBag.getVioletDices());

        i = diceBag.getBlueDices();
        diceBag.reInsertDice(bluDice);
        if(i == 18) Assert.assertEquals( i, diceBag.getBlueDices());
        else Assert.assertEquals( i+1, diceBag.getBlueDices());

        i = diceBag.getRedDices();
        diceBag.reInsertDice(redDice);
        if(i == 18) Assert.assertEquals( i, diceBag.getRedDices());
        else Assert.assertEquals( i+1, diceBag.getRedDices());

        i = diceBag.getGreenDices();
        diceBag.reInsertDice(greenDice);
        if(i == 18) Assert.assertEquals( i, diceBag.getGreenDices());
        else Assert.assertEquals( i+1, diceBag.getGreenDices());

        i = diceBag.getYellowDices();
        diceBag.reInsertDice(yellowDice);
        if(i == 18) Assert.assertEquals( i, diceBag.getYellowDices());
        else Assert.assertEquals( i+1, diceBag.getYellowDices());
    }

}

