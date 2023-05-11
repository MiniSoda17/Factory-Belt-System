package lms.io;

import lms.exceptions.FileFormatException;
import lms.grid.GameGrid;
import lms.grid.Coordinate;
import lms.grid.GridComponent;
import lms.logistics.Item;
import lms.logistics.Transport;
import lms.logistics.belts.Belt;
import lms.logistics.container.Producer;
import lms.logistics.container.Receiver;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
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
        int producerCount = 0;
        int receiverCount = 0;
        String producerName = "something";
        String receiverName = "something";
        Item producerKey = new Item(producerName);
        Item receiverKey = new Item(receiverName);
        String line;
        HashMap <Integer, Transport> componentPosition = new HashMap<>();
        BufferedReader bufferedReader = (BufferedReader) reader;
        int sectionCount = 1;
        String section = "";
        while ((line = bufferedReader.readLine()) != null) {
            
            if (!(line.contains("_"))) {
                if (sectionCount == 6) {
                    section += line + " ";
                } else {
                    section += line;
                }
            } else {
                if (sectionCount == 1) {
                    range = Integer.parseInt(section);
                    gameGrid = new GameGrid(range);
                } else if (sectionCount == 2) {
                    producerCount = Character.getNumericValue(section.charAt(0));
                    receiverCount = Character.getNumericValue(section.charAt(1));
                } else if (sectionCount == 3) {
                    String itemName;
                    for (int count = 1; count <= section.length(); count ++) {
                        if (count % 2 == 0) {
                            itemName = section.substring(count - 2, count);
                            producerKey = new Item(itemName);
                        }
                    }
                } else if (sectionCount == 4) {
                    String itemName;
                    for (int count = 1; count <= section.length(); count ++) {
                        if (count % 2 == 0) {
                            itemName = section.substring(count - 2, count);
                            receiverKey = new Item(itemName);
                        }
                    }
                } else if (sectionCount == 5) {
                    section = section.replace(" ", "").replace("w", "").replace("o", "");
                    for (int count = 0; count < section.length(); count++) {
                        if (section.charAt(count) == 'p') {
                            componentPosition.put(count + 1, new Producer(count + 1, producerKey));
                        } else if (section.charAt(count) == 'b') {
                            componentPosition.put(count + 1, new Belt(count + 1));
                        } else if (section.charAt(count) == 'r') {
                            componentPosition.put(count + 1, new Receiver(count + 1, receiverKey));
                        }
                    }
                } 
                section = "";
                sectionCount ++;
            }
        }
        
        Transport start;
        Transport end;
        String middle = "";
        Boolean startFound;
        for (char character : section.toCharArray()) {
            if (character != '-' && character != ' ' && character != ',') {
                int id = Character.getNumericValue(character);
                if (startFound = false) {
                    start = componentPosition.get(id);
                    startFound = true;
                } else {
                    end = componentPosition.get(id);
                }
            } else if (character == '-' || character == ',') {
                middle += character;
            } else {
                
            }
        }
        bufferedReader.close();
        return gameGrid;
    }
}
