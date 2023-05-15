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
import java.util.Iterator;

import javax.swing.text.AbstractDocument.ElementEdit;
import javax.xml.stream.events.StartDocument;

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
        List<Item> producerKeys = new ArrayList<>();
        List<Item> receiverKeys = new ArrayList<>();
        Iterator<Item> producerKeyIterator = producerKeys.iterator();
        Iterator<Item> receiverKeyIterator = receiverKeys.iterator();
        String line;
        HashMap <Integer, Path> componentPosition = new HashMap<>();
        String gridLayout = "";
        BufferedReader bufferedReader = (BufferedReader) reader;
        int sectionCount = 1;
        String section = "";
        HashMap<GridComponent, Coordinate> coordinateFind = new HashMap<>();
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
                    int nameLength = 2;
                    if (section.contains("key")) {
                        nameLength = 4;
                    }
                    for (int count = 1; count <= section.length(); count ++) {
                        if (count % nameLength == 0) {
                            itemName = section.substring(count - nameLength, count);
                            producerKeys.add(new Item(itemName));
                        }
                    }
                } else if (sectionCount == 4) {
                    String itemName;
                    int nameLength = 2;
                    if (section.contains("key")) {
                        nameLength = 4;
                    }
                    for (int count = 1; count <= section.length(); count ++) {
                        if (count % nameLength == 0) {
                            itemName = section.substring(count - nameLength, count);
                            receiverKeys.add(new Item(itemName));
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
                    Coordinate origin = new Coordinate(0, -range);
                    Coordinate direction = origin;
                    GridComponent gridComponent = () -> "";
                    List<Integer> elementsPerRow = rowCalculator(range);
                    boolean originFound = false;
                    int idCounter = 0;
                    int rowCounter = 0;
                    for (int count = 0; count < gridLayout.length(); count++) {
                        if (gridLayout.charAt(count) == 'w') {
                        gridComponent = () -> "w";
                        } else if (gridLayout.charAt(count) == 'o') {
                            gridComponent = () -> "o";
                        } else if (gridLayout.charAt(count) != ' ') {
                            idCounter ++;
                            gridComponent = componentPosition.get(idCounter).getNode();
                        }
                        if (originFound == false) {
                            direction = origin;
                            originFound = true;
                        } else {
                            if (elementsPerRow.get(rowCounter) != 0) {
                                direction = direction.getRight();
                            } else {
                                if (rowCounter >= range) {
                                    origin = origin.getBottomRight();
                                } else {
                                    origin = origin.getBottomLeft();
                                }
                                direction = origin;
                                rowCounter ++;
                            }
                        }
                        coordinateFind.put(gridComponent, direction);
                        gameGrid.setCoordinate(direction, gridComponent);
                        elementsPerRow.set(rowCounter, elementsPerRow.get(rowCounter) - 1);
                    }
                } 
                section = "";
                sectionCount ++;
            }
        }
        String connections = "";
        int startID = 0;
        int endID = 0;
        for (char character : section.toCharArray()) {
            if (character != ' ') {
                connections += character;
            } else {
                int numberEnder = connections.indexOf("-");
                if (numberEnder == 2) {
                    startID = Integer.parseInt(connections.substring(0, numberEnder));
                } else {
                    startID = Character.getNumericValue(connections.charAt(0));
                }
                Path startPath = componentPosition.get(startID);
                int numberStarter = 0;
                if (connections.contains(",")) {
                    numberStarter = connections.indexOf(",") + 1;
                } else {
                    numberStarter = connections.indexOf("-") + 1;
                }
                if (connections.length() - numberStarter == 2) {
                    endID = Integer.parseInt(connections.substring(numberStarter, connections.length()));
                } else {
                    endID = Character.getNumericValue(connections.charAt(connections.length() - 1));
                }
                System.out.println(endID);
                Path endPath = componentPosition.get(endID);
                Path middlePath;
                if (connections.contains(",")) {
                    if (connections.charAt(connections.indexOf("-") + 1) != ',') {
                        int middleID = 0;
                        int start = connections.indexOf("-") + 1;
                        int end = connections.indexOf(",");
                        if (end - start == 2) {
                            middleID = Integer.parseInt(connections.substring(start, end));
                        } else {
                            middleID = Character.getNumericValue(connections.charAt(connections.indexOf("-") + 1));
                        }
                        middlePath = componentPosition.get(middleID);
                        Transport startComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(startPath.getNode()));
                        Transport middleComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(middlePath.getNode()));
                        startComponent.setInput(middleComponent.getPath());
                        middleComponent.setOutput(startComponent.getPath());
                    } if (connections.charAt(connections.length() - 1) != ',') {
                        Transport startComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(startPath.getNode()));
                        Transport endComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(endPath.getNode()));
                        startComponent.setOutput(endComponent.getPath());
                        endComponent.setInput(startComponent.getPath());
                    }
                } else if (startPath.getNode() instanceof Receiver) {
                    Transport startComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(startPath.getNode()));
                    Transport endComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(endPath.getNode()));
                    startComponent.setInput(endComponent.getPath());
                    endComponent.setOutput(startComponent.getPath());
                } else {
                    Transport startComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(startPath.getNode()));
                    Transport endComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(endPath.getNode()));
                    startComponent.setOutput(endComponent.getPath());
                    endComponent.setInput(startComponent.getPath());
                }
                connections = "";
            }
        }
        bufferedReader.close();
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
