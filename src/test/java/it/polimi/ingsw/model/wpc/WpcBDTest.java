package it.polimi.ingsw.model.wpc;


import it.polimi.ingsw.model.clientModel.ClientWpc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class WpcBDTest {
    static private WpcDB allWpc0, allWpc1;

    @Before
    public void Before(){
        allWpc0 = WpcDB.getInstance();
        allWpc1 = WpcDB.getInstance("src/main/resources/wpc/wpc_schema");
    }

    /**
     * Tests if it read it create a correct instance and if it reads and stores in a correct way the wpcs
     */
    @Test
    public void getInstanceTest() {
        assertNotNull(allWpc0);
        assertNotNull(allWpc1);
        assertEquals(allWpc0, allWpc1);

        assertEquals(24, allWpc1.getWpcIDs().size());
        assertEquals(5, allWpc1.getWpcByID("1").getFavours());

        ClientWpc clientWpc = allWpc1.getClientWpcByID("14");
        Wpc wpc = allWpc1.getWpcByID("14");

        assertEquals(wpc.getId(), clientWpc.getWpcID());
        assertEquals(wpc.getFavours(), clientWpc.getFavours());

        for(int i = 0; i< 20; i ++){
            assertEquals(wpc.getClientWpc().getSchema().get(i).getCellColor(), clientWpc.getSchema().get(i).getCellColor());
            assertEquals(wpc.getClientWpc().getSchema().get(i).getCellNumber(), clientWpc.getSchema().get(i).getCellNumber());
        }

        for(int j = 0; j < 24; j++){
            assertTrue( Integer.parseInt(allWpc1.getWpcIDs().get(j)) <= 24 );
            assertTrue(Integer.parseInt(allWpc1.getWpcIDs().get(j)) > 0);
        }

    }


}
