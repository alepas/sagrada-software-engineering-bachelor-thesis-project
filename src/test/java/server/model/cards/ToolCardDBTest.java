package server.model.cards;

import org.junit.Before;
import org.junit.Test;
import server.model.cards.concretetoolcards.ToolCard3;
import server.model.configLoader.ConfigLoader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolCardDBTest {
    static private ToolCardDB allToolCard;
    private ToolCard3 toolCard3;

    /**
     * Creates a mock POC card and get an instance of the class.
     */
    @Before
    public void Before(){

        ConfigLoader.loadConfig();
        allToolCard = ToolCardDB.getInstance();
        toolCard3 = mock(ToolCard3.class);
        when(toolCard3.getID()).thenReturn("3");
        when(toolCard3.getName()).thenReturn("Alesatore per lamina di rame");
        when(toolCard3.getDescription()).thenReturn("Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore\n" +
                "Devi rispettare tutte le altre restrizioni di piazzamento");
    }

    /**
     * Checks if the instance is not null and if the HashMap is build in a correct way
     */
    @Test
    public void getCardsTest(){

        assertNotNull(ToolCardDB.getInstance());
        assertEquals(12, allToolCard.getCardsIDs().size());
        for(int i = 0; i < 12 ; i++) assertTrue(Integer.parseInt(allToolCard.getCardsIDs().get(i)) <= 12 );

        assertEquals(toolCard3.getID(), allToolCard.getCardByID("3").getID());
        assertEquals(toolCard3.getDescription(), allToolCard.getCardByID("3").getDescription());
        assertEquals(toolCard3.getName(), allToolCard.getCardByID("3").getName());
        assertNull(allToolCard.getCardByID("14"));
    }

}
