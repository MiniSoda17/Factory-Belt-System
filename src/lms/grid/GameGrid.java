package lms.grid;

import java.util.HashMap;
import java.util.Map;

import lms.logistics.Item;
import lms.logistics.Path;
import lms.logistics.belts.Belt;
import lms.logistics.container.Producer;

/**
 * The GameGrid is responsible for managing the state and initialisation of the game's grid.
 * It provides the Map structure to hold the coordinates of each node in the grid. It also
 * maintains the size of the grid using a range variable. The range value donates how many
 * nodes each hexagonal grid node extends to.
 *
 * @ass2
 * @version 1.0
 * <p>
 * Summary: Initializes a grid of the game.
 *
 */
public class GameGrid {
    /** An integer used to calculate the size of the grid */
    private int range;
    /** A HashMap which will keep track of each coordinate and the gridcomponent with it */
    private Map<Coordinate, GridComponent> grid;;

    /**
     * Constructs a new GameGrid
     * @param range
     */
    public GameGrid(int range) {
        this.range = range;
        this.grid = this.generate(this.range);
    }

    /**
     * Gets the a copy of the grid HashMap
     * @return a copy of the grid
     */
    public Map<Coordinate, GridComponent> getGrid() {
        Map<Coordinate, GridComponent> gridCopy = new HashMap<>(this.grid);
        return gridCopy;
        
    }

    /**
     * Gets the range of this GameGrid
     * @return an integer which represents the range of this GameGrid
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Maps a coordinate and a GridComponent to this grid's HashMap
     * 
     * @param coordinate The coordinated to be added to the grip HashMap as the key
     * @param componenet The component to be added to the grid Hashmap as the value
     */
    public void setCoordinate(Coordinate coordinate, GridComponent componenet) {
        grid.put(coordinate, componenet);
    }

    /**
     * Helper method:
     * Generates a grid with the given range, starting from the origin (the centre) and maintaining a
     * balanced shape for the entire mapping structure.
     * This has been provided to support you with the hexagonal coordinate logic.
     * @param range The range of the map.
     * @provided
     */
    private Map<Coordinate, GridComponent> generate(int range) {
        Map<Coordinate, GridComponent> tempGrid = new HashMap<>();
        for (int q = -range; q <= range; q++) { // From negative to positive (inclusive)
            for (int r = -range; r <= range; r++) { // From negative to positive (inclusive)
                for (int s = -range; s <= range; s++) { // From negative to positive (inclusive)
                    if (q + r + s == 0) {
                        // Useful to default to error
                        tempGrid.put(new Coordinate(q, r, s), () -> "ERROR");
                        // tempGrid.put(new Coordinate(q, r, s), new Producer(5, new Item("hi")));
                    }
                }
            }
        }
        return tempGrid;
    }
}
