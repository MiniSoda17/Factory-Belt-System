package lms.io;

import lms.exceptions.FileFormatException;
import lms.grid.GameGrid;
import lms.grid.Coordinate;
import lms.grid.GridComponent;
import lms.logistics.Item;
import lms.logistics.Path;
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
        HashMap <Integer, Path> componentPosition = new HashMap<>();
        HashMap <Path, Coordinate> componentCoordinate = new HashMap<>();
        String gridLayout = "";
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
                    gridLayout = section;
                    section = section.replace(" ", "").replace("w", "").replace("o", "");
                    for (int count = 0; count < section.length(); count++) {
                        if (section.charAt(count) == 'p') {
                            componentPosition.put(count + 1, new Path(new Producer(count + 1, producerKey)));
                        } else if (section.charAt(count) == 'b') {
                            componentPosition.put(count + 1, new Path(new Belt(count + 1)));
                        } else if (section.charAt(count) == 'r') {
                            componentPosition.put(count + 1, new Path(new Receiver(count + 1, receiverKey)));
                        }
                    }
                } 
                section = "";
                sectionCount ++;
            }
        }
        String connections = "";
        HashMap <String, Path> paths = new HashMap<>();
        for (char character : section.toCharArray()) {
            if (character != ' ') {
                connections += character;
            } else {
                int startID = Character.getNumericValue(connections.charAt(0));
                Path startPath = componentPosition.get(startID);
                int endID = Character.getNumericValue(connections.charAt(connections.length() - 1));
                Path endPath = componentPosition.get(endID);
                Path middlePath;
                if (connections.contains(",")) {
                    if (connections.charAt(connections.indexOf("-") + 1) != ',') {
                        middlePath = componentPosition.get(connections.indexOf("-") + 1);
                        startPath.previous(middlePath);
                        startPath.getNode().setInput(middlePath);
                        middlePath.next(startPath);
                        middlePath.getNode().setOutput(middlePath);
                    } if (connections.charAt(connections.length() - 1) != ',') {
                        endPath = componentPosition.get(Character.getNumericValue(connections.charAt(connections.length() - 1)));
                        startPath.next(endPath);
                        startPath.getNode().setOutput(endPath);
                        endPath.previous(startPath);
                        endPath.getNode().setInput(endPath);
                    }
                } else if (startPath.getNode() instanceof Receiver) {
                    startPath.previous(endPath);
                    startPath.getNode().setInput(endPath);
                    endPath.next(startPath);
                    endPath.getNode().setOutput(endPath);
                } else {
                    startPath.next(endPath);
                    startPath.getNode().setOutput(endPath);
                    endPath.previous(startPath);
                    endPath.getNode().setInput(startPath);
                }
                connections = "";
            }
        }
        Coordinate coordinate = new Coordinate(0, 0, 0);
        var entrySet = componentPosition.entrySet();
        for (int count = 0; count < section.length(); count++) {
            if (gridLayout.charAt(count) == 'w') {
                GridComponent gridComponent = () -> "w";
                System.out.println(gridComponent.getEncoding());
            }
        }
        for (var entry : entrySet) {
            gameGrid.setCoorindate(coordinate, entry.getValue().getNode());
        }
        // System.out.println(componentPosition.get(3).getNode().getPath());
        bufferedReader.close();
        return gameGrid;
    }

    private HashMap <Integer, Integer> rowCalculator(int range) {
        HashMap <Integer, Integer> row = new HashMap<>();
        return row;
    }
}
