package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CellTest {
    private Cell cell;
    private Cell cell1;
    private Dice dice;
    private Position position;
    private int cellNumber;
    private Color cellColor;

    @Before
    public void Before(){

        dice = mock(Dice.class);

        position = mock(Position.class);
        cellNumber = 1;
        cellColor = Color.VIOLET;
        cell = new Cell(position, cellColor, cellNumber);
        cell1 = new Cell(cell);
    }

    @Test
    public void checkCellConstructor(){
        Assert.assertEquals(cellNumber, cell.getCellNumber());
        Assert.assertEquals(cellColor, cell.getCellColor());
        Assert.assertEquals(position, cell.getCellPosition());
        Assert.assertNull(cell.getCellDice());
    }

    @Test
    public void check2ndCellConstructor(){
        Assert.assertEquals(cell.getCellNumber(), cell1.getCellNumber());
        Assert.assertEquals(cell.getCellColor(), cell1.getCellColor());
        Assert.assertEquals(cell.getCellPosition(), cell1.getCellPosition());
        Assert.assertNull(cell1.getCellDice());
    }

    @Test
    public void checkSetAndRemoveDice(){
        cell.setDice(dice);
        Assert.assertEquals( dice, cell.getCellDice());
        cell.removeDice();
        Assert.assertEquals(null, cell.getCellDice());
    }
}
