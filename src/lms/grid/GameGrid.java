package lms.grid;

import java.util.HashMap;
import java.util.Map;

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

    private int range;

    private Map<Coordinate, GridComponent> grid = new HashMap<>();
    /**
     * 
     * @param range
     */
    public GameGrid(int range) {
        this.range = range;
    }

    public Map<Coordinate, GridComponent> getGrid() {
        this.generate(this.range);

        return this.grid;
    }

    public int getRange() {
        return this.range;
    }

    public void setCoorindate(Coordinate coordinate, GridComponent componenet) {
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
    private void generate(int range) {
        for (int q = -range; q <= range; q++) { // From negative to positive (inclusive)
            for (int r = -range; r <= range; r++) { // From negative to positive (inclusive)
                for (int s = -range; s <= range; s++) { // From negative to positive (inclusive)
                    if (q + r + s == 0) {
                        // Useful to default to error
                        grid.put(new Coordinate(q, r, s), () -> "ERROR");
                    }
                }
            }
        }
    }
}
