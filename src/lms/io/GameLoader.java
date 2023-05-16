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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.BufferedReader;

public class GameLoader {
    /** Used to represent the coordinates of each GridComponent and their Coordinate on a map */
    private static GameGrid gameGrid;

    /**
     * Parses through a text file and converts it to a GameGrid
     * 
     * @param reader The text file to be read through
     * @return A GameGrid
     * @throws IOException 
     * @throws FileFormatException when the file cannot be read
     */
    public static GameGrid load(Reader reader) throws IOException, FileFormatException {
        int range = 0;
        int producerCount;
        int receiverCount;
        int sectionCount = 1;
        String producerName = "something";
        String receiverName = "something";

        Item producerKey = new Item(producerName);
        Item receiverKey = new Item(receiverName);
        List<Item> producerKeys = new ArrayList<>();
        List<Item> receiverKeys = new ArrayList<>();

        HashMap <Integer, Path> componentPosition = new HashMap<>();
        HashMap<GridComponent, Coordinate> coordinateFind = new HashMap<>();
        BufferedReader bufferedReader = (BufferedReader) reader;

        String gridLayout = "";
        String section = "";
        String line;

        // Reads each line in the text file and stops if the line is empty
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
                    createKeys(section, producerKeys);
                } else if (sectionCount == 4) {
                    createKeys(section, receiverKeys);
                } else if (sectionCount == 5) {
                    gridLayout = section.replace(" ", "");
                    section = section.replace(" ", "").replace("w", "").replace("o", "");
                    createPaths(section, componentPosition, producerKey, receiverKey);
                    mapGameGrid(gridLayout, componentPosition, range, coordinateFind);
                } 
                section = "";
                sectionCount ++;
            }
        }
        createConnections(section, componentPosition, coordinateFind);
        bufferedReader.close();
        return gameGrid;
    }

    /**
     * Creates the item name for all the keys
     * 
     * @param section A string representing all the names of the keys
     * @param keys A list that will hold all the key names
     */
    private static void createKeys(String section, List<Item> keys) {
        String itemName;
        int nameLength = 2;
        if (section.contains("key")) {
            nameLength = 4;
        }
        for (int count = 1; count <= section.length(); count ++) {
            if (count % nameLength == 0) {
                itemName = section.substring(count - nameLength, count);
                keys.add(new Item(itemName));
            }
        }
    }

    /**
     * Creates the previous and next connections between the GridComponents in GameGrid
     * 
     * @param section A String which represents what connections need to be made
     * @param componentPosition A HashMap which links all the GridComponents with their id
     * @param coordinateFind
     */
    private static void createConnections(String section, HashMap<Integer, Path> componentPosition,
            HashMap<GridComponent, Coordinate> coordinateFind) {
        String connections = "";
        int startID = 0;
        int endID = 0;
        
        // Finding the GridComponents per line
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
                Path endPath = componentPosition.get(endID);
                Path middlePath;

                // Creating connections depending on the GridComponent type
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
                        addBackwardsConnection(coordinateFind, startPath, middlePath);
                    } if (connections.charAt(connections.length() - 1) != ',') {
                        addForwardConnection(coordinateFind, startPath, endPath);
                    }
                } else if (startPath.getNode() instanceof Receiver) {
                    addBackwardsConnection(coordinateFind, startPath, endPath);
                } else {
                    addForwardConnection(coordinateFind, startPath, endPath);
                }
                connections = "";
            }
        }
    }

    /**
     * Creates a backwards Connection between two Transport Nodes
     * 
     * @param startPath the Path that contains the Transport Node to be connected
     * @param endPath the second Path that contains the Transport Node to be connected
     * @param coordinateFind A hashmap containing the coordinate of each Transport Node
     */
    private static void addForwardConnection(HashMap<GridComponent, Coordinate> coordinateFind, 
            Path startPath, Path endPath) {
        Transport startTransport = (Transport) gameGrid.getGrid().get(coordinateFind.get(startPath.getNode()));
        Transport endTransport = (Transport) gameGrid.getGrid().get(coordinateFind.get(endPath.getNode()));
        startTransport.setOutput(endTransport.getPath());
        endTransport.setInput(startTransport.getPath());
    }

    /**
     * Creates a Forward Connection between two Transport Nodes
     * 
     * @param startPath the Path that contains the Transport Node to be connected
     * @param endPath the second Path that contains the Transport Node to be connected
     * @param coordinateFind A hashmap containing the coordinate of each Transport Node
     */
    private static void addBackwardsConnection(HashMap<GridComponent, Coordinate> coordinateFind, 
            Path startPath, Path endPath) {
        Transport startComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(startPath.getNode()));
        Transport endComponent = (Transport) gameGrid.getGrid().get(coordinateFind.get(endPath.getNode()));
        startComponent.setInput(endComponent.getPath());
        endComponent.setOutput(startComponent.getPath());
    }

    /**
     * Creates all the necessary paths with a positionalID
     * 
     * @param section A string which contains all the necessary paths to be made
     * @param componentPosition A hashmap which holds each GridComponent linked with their ID
     * @param producerKey An item for the Producer to be the key
     * @param receiverKey An item for the Receiver to be the key
     */
    private static void createPaths(String section, HashMap<Integer, Path> componentPosition, 
            Item producerKey, Item receiverKey) {
        for (int count = 0; count < section.length(); count++) {
            switch(section.charAt(count)) {
                case 'p':
                    componentPosition.put(count + 1, new Path(new Producer(count + 1, producerKey)));
                    break;
                case 'b':
                    componentPosition.put(count + 1, new Path(new Belt(count + 1)));
                    break;
                case 'r':
                    componentPosition.put(count + 1, new Path(new Receiver(count + 1, receiverKey)));
                    break;
            }
        }
    }
    
    /**
     * Sets each GridComponent to a coordinate in GameGrid
     * 
     * @param gridLayout A string that represents the position of all the GridComponent
     * @param componentPosition A hashmap containing the the positional id of each GridComponent
     * @param range An integer used to calculate the positional values of the grid
     * @param coordinateFind A HashMap which enables us to find the Coordinate of a GridComponent
     */
    private static void mapGameGrid(String gridLayout, HashMap<Integer, Path> componentPosition, 
            int range, HashMap<GridComponent, Coordinate> coordinateFind) {
        Coordinate origin = new Coordinate(0, -range);
        Coordinate direction = origin;
        GridComponent gridComponent = () -> "";
        List<Integer> elementsPerRow = rowCalculator(range);

        boolean originFound = false;
        int idCounter = 0;
        int rowCounter = 0;
        
        // Navigating through the Grid and assigning GridComponents with Coordinates
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

    /**
     * Calculates the amount of elements per row using the range
     * 
     * @param range an integer given from a text file
     * @return an ArrayList with the amount of elements per row
     */
    private static ArrayList<Integer> rowCalculator(int range) {
        ArrayList <Integer> elementsPerRow = new ArrayList<>();
        int gridLength = range * 2 + 1;
        int countPerRow = range;
        int middleRow = range + 1;

        // Counts the amount of elements per row
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
