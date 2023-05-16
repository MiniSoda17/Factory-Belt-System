package test;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import lms.grid.Coordinate;
import lms.grid.GameGrid;
import lms.grid.GridComponent;
import lms.logistics.Transport;
import lms.logistics.belts.Belt;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

public class GameGridTest {
    private GameGrid gameGrid1;
    private GameGrid gameGrid2;
    private GameGrid gameGrid3;

    @Before
    public void setUp() {
        gameGrid1 = new GameGrid(1);
        gameGrid2 = new GameGrid(2);
        gameGrid3 = new GameGrid(3);
    }

    @Test
    public void getRangeTest1() {
        assertEquals(1, gameGrid1.getRange());
    }

    @Test
    public void getRangeTest2() {
        assertEquals(2, gameGrid2.getRange());
    }

    @Test
    public void getRangeTest3() {
        assertEquals(3, gameGrid3.getRange());
    }

    @Test
    public void setCoordinateTest1() {
        Coordinate coordinate = new Coordinate();
        GridComponent gridComponent = new Belt(1);
        gameGrid1.setCoordinate(coordinate, gridComponent);
        assertEquals(gridComponent, gameGrid1.getGrid().get(coordinate));
    }

    @Test
    public void setCoordinateTest2() {
        Coordinate coordinate = new Coordinate(1, 0);
        GridComponent gridComponent = new Belt(2);
        gameGrid2.setCoordinate(coordinate, gridComponent);
        assertEquals(gridComponent, gameGrid2.getGrid().get(coordinate));
    }

    @Test
    public void setCoordinateTest3() {
        Coordinate coordinate = new Coordinate(0, -1);
        GridComponent gridComponent = new Belt(3);
        gameGrid3.setCoordinate(coordinate, gridComponent);
        assertEquals(gridComponent, gameGrid3.getGrid().get(coordinate));
    }

    @Test
    public void getGridTest1() {
        Map<Coordinate, GridComponent> gridCopy = new HashMap<>(gameGrid1.getGrid());

        assertEquals(gridCopy, new HashMap<>(gameGrid1.getGrid()));
    }

    @Test
    public void getGridTest1() {

    }

    @Test
    public void getGridTest1() {

    }
}
