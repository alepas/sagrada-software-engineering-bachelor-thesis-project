package it.polimi.ingsw.model.dicebagtest;

import it.polimi.ingsw.model.dicebag.DiceBag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DiceBagTest {
    private DiceBag diceBag;

    @Before
    public void before() {
        diceBag = new DiceBag();
    }

    @Test
    public void checkDiceBagConstructor(){
        Assert.assertEquals(18, diceBag.getBlueDices());
        Assert.assertEquals(18, diceBag.getGreenDices());
        Assert.assertEquals(18, diceBag.getRedDices());
        Assert.assertEquals(18, diceBag.getVioletDices());
        Assert.assertEquals(18, diceBag.getYellowDices());
    }


}
