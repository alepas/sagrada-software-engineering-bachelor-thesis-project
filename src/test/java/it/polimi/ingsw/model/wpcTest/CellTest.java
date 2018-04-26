package it.polimi.ingsw.model.wpcTest;
import it.polimi.ingsw.model.wpc.Cell;
import it.polimi.ingsw.model.dicebag.Dice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CellTest {
    private Cell cell1;
    private Cell cell2;

    @Before
    public void Before(){
        Dice dice;
        dice = mock(Dice.class);
        cell1 = mock(Cell.class);
        cell2 = mock(Cell.class);
        when(cell1.getCellDice()).thenReturn(dice);
    }

    @Test
    public void checkRemoveDice(){
        Assert.assertNull(cell1.getCellDice());
    }

    @Test
    public void checkSetDice(){
        Assert.assertNotNull(cell2.getCellDice());
    }
}
