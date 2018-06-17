package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.PocDB;
import it.polimi.ingsw.model.cards.concretePublicObjectiveCards.PublicObjectiveCard3;
import it.polimi.ingsw.model.wpc.WpcDB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PocDBTest {
    static private PocDB allPoc;
    private PublicObjectiveCard3 card3;

    @Before
    public void Before(){
        allPoc = PocDB.getInstance();
        card3 = mock(PublicObjectiveCard3.class);
        when(card3.getID()).thenReturn("3");
    }

    @Test
    public void getCardsTest(){

        assertNotNull(PocDB.getInstance());
        assertEquals(10, allPoc.getCardsIDs().size());
        for(int i = 0; i < 10 ; i++) assertTrue(Integer.parseInt(allPoc.getCardsIDs().get(i)) <= 10 );

        assertEquals(card3.getID(), allPoc.getCardByID("3").getID());

    }



}
