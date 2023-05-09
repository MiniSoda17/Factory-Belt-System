package lms.io;

import lms.exceptions.FileFormatException;
import lms.grid.GameGrid;
import lms.grid.Coordinate;
import lms.grid.GridComponent;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;

public class GameLoader {
    
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
        int range = 0;
        int producerCount;
        int receiverCount;
        String producerName;
        String receiverName;
        String line;
        BufferedReader bufferedReader = (BufferedReader) reader;
        int sectionCount = 0;
        String section = "";
        while ((line = bufferedReader.readLine()) != null) {
            
            if (!(line.contains("_"))) {
                section += line;
            } else {
                if (sectionCount == 0) {
                    range = Integer.parseInt(section);
                } else if (sectionCount == 1) {
                    producerCount = Character.getNumericValue(section.charAt(0));
                    receiverCount = Character.getNumericValue(section.charAt(1));
                } else if (sectionCount == 3) {
                    
                }
                section = "";
                sectionCount ++;
            }
            
        }
        bufferedReader.close();
        gameGrid = new GameGrid(range);
        return gameGrid;
    }
}
