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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;

public class GameLoader {
    
    private static GameGrid gameGrid;

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
                    gridLayout = section.replace(" ", "");
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
        Coordinate origin = new Coordinate();
        GridComponent gridComponent = () -> "";
        Coordinate direction = origin;
        List<Integer> elementsPerRow = rowCalculator(range);
        boolean originFound = false;
        int idCounter = 0;
        int rowCounter = 0;
        for (int count = 0; count < gridLayout.length(); count++) {
            
            if (gridLayout.charAt(count) == 'w') {
                gridComponent = () -> "w";
            } else if (gridLayout.charAt(count) == 'o') {
                gridComponent = () -> "o";
            } else if (gridLayout.charAt(count) != ' '){
                idCounter ++;
                gridComponent = componentPosition.get(idCounter).getNode();
            }
            if (rowCounter % 2 == 0) {
                if (elementsPerRow.get(rowCounter) != 0) {
                    direction = direction.getRight();
                } else {
                    direction = direction.getBottomRight();
                    rowCounter ++;
                }
            } else {
                if (elementsPerRow.get(rowCounter) != 0) {
                    direction = direction.getLeft();
                } else {
                    direction = direction.getBottomLeft();
                    rowCounter ++;
                }
            } 
            if (originFound == false) {
                direction = origin;
                originFound = true;
            } 
            // System.out.println(direction);
            // System.out.println(gridComponent);
            gameGrid.setCoordinate(direction, gridComponent);
            elementsPerRow.set(rowCounter, elementsPerRow.get(rowCounter) - 1);
        }

        bufferedReader.close();
        // var entrySet = gameGrid.getGrid().entrySet();
        // for (var entry : entrySet) {
        //     System.out.println(entry.getKey());
        //     System.out.println(entry.getValue());
        // }
        System.out.println(gameGrid.getGrid());
        return gameGrid;
    }
    /**
     * 
     * @param range
     * @return
     */
    private static ArrayList<Integer> rowCalculator(int range) {
        ArrayList <Integer> elementsPerRow = new ArrayList<>();
        int gridLength = range * 2 + 1;
        int countPerRow = range;
        int middleRow = range + 1;
        for (int i = 0; i < gridLength; i++) {
            if (i + 1 > middleRow) {
                countPerRow--;
            } else {
                countPerRow++;
            }
            elementsPerRow.add(countPerRow);
        }
        return elementsPerRow;
    }
}
