package server.model.dicebag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.clientInfo.ClientColor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static server.model.dicebag.Color.getClientColor;
import static server.model.dicebag.Color.getColorFromClientColor;

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

        assertEquals(Color.VIOLET, Color.parseColor(violet));
        assertEquals(Color.BLUE, Color.parseColor(blue));
        assertEquals(Color.GREEN, Color.parseColor(green));
        assertEquals(Color.YELLOW, Color.parseColor(yellow));
        assertEquals(Color.RED, Color.parseColor(red));
        assertNull(Color.parseColor(noColor));
    }


    /**
     * tests if the clientColor() method works in a correct way
     */
    @Test
    public void getClientColorTest(){
        Color violet = Color.VIOLET;
        Color blue = Color.BLUE;
        Color green = Color.GREEN;
        Color red = Color.RED;
        Color yellow = Color.YELLOW;

        ClientColor clientColor;

        clientColor = getClientColor(violet);
        assertEquals(violet.toString(), clientColor.toString() );

        clientColor = getClientColor(blue);
        assertEquals(blue.toString(), clientColor.toString());

        clientColor = getClientColor(green);
        assertEquals(green.toString(), clientColor.toString());

        clientColor = getClientColor(red);
        assertEquals(red.toString(), clientColor.toString());

        clientColor = getClientColor(yellow);
        assertEquals(yellow.toString(), clientColor.toString());

        assertNull(getClientColor(null));
    }

    /**
     * tests if the clientColor() method works in a correct way
     */
    @Test
    public void getColorFromClientColorTest(){
        ClientColor violet = ClientColor.VIOLET;
        ClientColor blue = ClientColor.BLUE;
        ClientColor green = ClientColor.GREEN;
        ClientColor red = ClientColor.RED;
        ClientColor yellow = ClientColor.YELLOW;

        Color color;

        color = getColorFromClientColor(violet);
        assertEquals(violet.toString(), color.toString() );

        color = getColorFromClientColor(blue);
        assertEquals(blue.toString(), color.toString());

        color = getColorFromClientColor(green);
        assertEquals(green.toString(), color.toString());

        color = getColorFromClientColor(red);
        assertEquals(red.toString(), color.toString());

        color = getColorFromClientColor(yellow);
        assertEquals(yellow.toString(), color.toString());

        assertNull(getColorFromClientColor(null));
    }
}
