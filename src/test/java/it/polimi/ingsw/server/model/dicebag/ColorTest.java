package it.polimi.ingsw.server.model.dicebag;

import it.polimi.ingsw.server.model.dicebag.Color;
import it.polimi.ingsw.shared.clientInfo.ClientColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.server.model.dicebag.Color.getClientColor;

public class ColorTest {
    private Color color;

    @Before
    public void Before() {
        color = Color.randomColor();
    }

    @Test
    public void checkRandomColor(){
        Assert.assertTrue(color == Color.VIOLET || color == Color.YELLOW || color == Color.RED || color == Color.BLUE || color == Color.GREEN);
    }

    /**
     * given a color string it should return the same color in the enum.
     */
    @Test
    public void checkParseColor(){

        String violet = "violet";
        String blue = "blue";
        String green = "green";
        String yellow = "yellow";
        String red = "red";
        String noColor = "null";

        Assert.assertEquals(Color.VIOLET, Color.parseColor(violet));
        Assert.assertEquals(Color.BLUE, Color.parseColor(blue));
        Assert.assertEquals(Color.GREEN, Color.parseColor(green));
        Assert.assertEquals(Color.YELLOW, Color.parseColor(yellow));
        Assert.assertEquals(Color.RED, Color.parseColor(red));
        Assert.assertNull(Color.parseColor(noColor));
    }



    public void getClientColorTest(){
        ClientColor clientColor = getClientColor(color);
    }


}
