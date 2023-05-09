package lms.io;

import lms.exceptions.FileFormatException;
import lms.grid.GameGrid;
import lms.grid.Coordinate;
import lms.grid.GridComponent;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class GameLoader {
    private static int range = 5;
    private static GameGrid gameGrid;
    

    public GameLoader() {

    }

    /**
     * @param reader
     * @return
     * @throws IOException
     * @throws FileFormatException
     */
    public static GameGrid load(Reader reader) throws IOException, FileFormatException {

        System.out.println(reader);
        gameGrid = new GameGrid(range);
        return gameGrid;
    }
}
