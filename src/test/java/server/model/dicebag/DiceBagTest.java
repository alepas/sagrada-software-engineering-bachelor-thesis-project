package server.model.dicebag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        assertEquals(0, diceBag.getDiceIdGenerator());
        assertEquals(18, diceBag.getBlueDices());
        assertEquals(18, diceBag.getGreenDices());
        assertEquals(18, diceBag.getRedDices());
        assertEquals(18, diceBag.getVioletDices());
        assertEquals(18, diceBag.getYellowDices());
    }

    @Test
    public void checkDicesExtraction() {
        int soloplayer = 1;
        int twoPlayers = 2;
        int threePlayers = 3;
        int fourPlayers = 4;
        assertEquals(4, diceBag.extractDices(soloplayer).size());
        assertEquals(86, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));

        assertEquals(5, diceBag.extractDices(twoPlayers).size());
        assertEquals(81, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));

        assertEquals(7, diceBag.extractDices(threePlayers).size());
        assertEquals(74, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));

        assertEquals(9, diceBag.extractDices(fourPlayers).size());
        assertEquals(65, (diceBag.getBlueDices() + diceBag.getGreenDices() + diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));
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
        if(i == 18) assertEquals(i, diceBag.getVioletDices());
        else assertEquals(i+1, diceBag.getVioletDices());

        i = diceBag.getBlueDices();
        diceBag.reInsertDice(bluDice);
        if(i == 18) assertEquals( i, diceBag.getBlueDices());
        else assertEquals( i+1, diceBag.getBlueDices());

        i = diceBag.getRedDices();
        diceBag.reInsertDice(redDice);
        if(i == 18) assertEquals( i, diceBag.getRedDices());
        else assertEquals( i+1, diceBag.getRedDices());

        i = diceBag.getGreenDices();
        diceBag.reInsertDice(greenDice);
        if(i == 18) assertEquals( i, diceBag.getGreenDices());
        else assertEquals( i+1, diceBag.getGreenDices());

        i = diceBag.getYellowDices();
        diceBag.reInsertDice(yellowDice);
        if(i == 18) assertEquals( i, diceBag.getYellowDices());
        else assertEquals( i+1, diceBag.getYellowDices());
    }

    /**
     * it is not possible to add a dice id the parameter is equal to 18
     */
    @Test
    public void reInsertDiceIllegalBound(){
        diceBag.reInsertDice(violetDice);
        assertEquals(18, diceBag.getVioletDices());

        diceBag.reInsertDice(bluDice);
        assertEquals(18, diceBag.getBlueDices());

        diceBag.reInsertDice(yellowDice);
        assertEquals(18, diceBag.getYellowDices());

        diceBag.reInsertDice(redDice);
        assertEquals(18, diceBag.getRedDices());

        diceBag.reInsertDice(greenDice);
        assertEquals(18, diceBag.getGreenDices());
    }

}

