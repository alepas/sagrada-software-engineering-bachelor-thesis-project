package it.polimi.ingsw.model.wpc;


import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static it.polimi.ingsw.model.wpc.WpcDB.getInstance;

public class WpcBDTest {
    static private WpcDB allWpc0, allWpc1;
    private WPC wpc;

    @Before
    public void Before(){
        allWpc0 = WpcDB.getInstance();
        allWpc1 = WpcDB.getInstance("src/main/resources/wpc/wpc_schema");
    }

    @Test
    public void getInstanceTest() {
        Assert.assertNotNull(allWpc0);
        Assert.assertNotNull(allWpc1);
        Assert.assertEquals(allWpc0, allWpc1);
    }

}
