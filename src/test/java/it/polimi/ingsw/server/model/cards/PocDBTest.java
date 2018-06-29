package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.cards.PocDB;
import it.polimi.ingsw.server.model.cards.concretePublicObjectiveCards.PublicObjectiveCard3;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PocDBTest {
    static private PocDB allPoc;
    private PublicObjectiveCard3 card3;

    /**
     * Creates a mock POC card and get an instance of the class.
     */
    @Before
    public void Before(){
        allPoc = PocDB.getInstance();
        card3 = mock(PublicObjectiveCard3.class);
        when(card3.getID()).thenReturn("3");
        when(card3.getName()).thenReturn("Sfumature diverse - Riga");
        when(card3.getDescription()).thenReturn("Ottieni 5 punti per ogni riga senza sfumature ripetute");
    }

    /**
     * Checks if the instance is not null and if the HashMap is build in a correct way
     */
    @Test
    public void getCardsTest(){

        assertNotNull(PocDB.getInstance());
        assertEquals(10, allPoc.getCardsIDs().size());
        for(int i = 0; i < 10 ; i++) assertTrue(Integer.parseInt(allPoc.getCardsIDs().get(i)) <= 10 );

        assertEquals(card3.getID(), allPoc.getCardByID("3").getID());
        assertEquals(card3.getDescription(), allPoc.getCardByID("3").getDescription());
        assertEquals(card3.getName(), allPoc.getCardByID("3").getName());
        assertNull(allPoc.getCardByID("14"));
    }

}
