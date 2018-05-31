package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.clientModel.Position;
import org.junit.Assert;
import org.junit.Test;

public class PositionTest {
    private Position position;
    private int row = 0;
    private int column = 2;


    @Test
    public void checkPositionCOnstructor(){
        position = new Position(row, column);
        Assert.assertEquals(0, position.getRow() );
        Assert.assertEquals(2, position.getColumn());
    }
}
