package it.polimi.ingsw.model.dicebag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceBagTest {
    private DiceBag diceBag;
    private Dice dice;

    @Before
    public void before() {
        diceBag = new DiceBag();
        dice = mock(Dice.class);
        when(dice.getDiceColor()).thenReturn(Color.VIOLET);
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
    public void checkDicesExtraction(){
        int numplayer = 1;
        int numplayers = 3;
        diceBag.extractDices(numplayer);
        Assert.assertEquals(4, diceBag.extractDices(numplayer).size());
        //Assert.assertEquals(86, (diceBag.getBlueDices() + diceBag.getGreenDices()+ diceBag.getRedDices() + diceBag.getVioletDices() + diceBag.getYellowDices()));
        diceBag.extractDices(numplayers);
        Assert.assertEquals(7, diceBag.extractDices(numplayers).size());
        int i = diceBag.getVioletDices();
        diceBag.reInsertDice(dice);
        Assert.assertEquals(i+1, diceBag.getVioletDices());
    }
}

