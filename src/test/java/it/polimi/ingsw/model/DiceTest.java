package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceTest {
    private Dice dice;
    private Colour color;

    @Before
    public void before() {
        color = mock();

        dice = new Dice(color);
    }
}