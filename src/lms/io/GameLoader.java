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

/** A class that represents a GameLoader */
public class GameLoader {
    /** Used to represent the coordinates of each GridComponent and their Coordinate on a map */
    private static GameGrid gameGrid;

    /**
     * Parses through a text file and converts it to a GameGrid
     * 
     * @param reader The text file to be read through
     * @return A GameGrid with fully implemented GridComponents
     * @throws IOException Thrown when there is trouble reading the file
     * @throws FileFormatException when the file cannot be read
     */
    public static GameGrid load(Reader reader) throws IOException, FileFormatException {
        if (reader == null) {
            throw new NullPointerException();
        }
        
        int range = 0;
        int producerCount = 0;
        int receiverCount = 0;
        int sectionCount = 1;

        String producerName = "name";
        String receiverName = "name";

        Item producerKey = new Item(producerName);
        Item receiverKey = new Item(receiverName);
        List<Item> producerKeys = new ArrayList<>();
        List<Item> receiverKeys = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(reader);
        HashMap<Integer, Path> componentPosition = new HashMap<>();
        HashMap<GridComponent, Coordinate> coordinateFind = new HashMap<>();

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
                satisfactoryLineCount(line);
                switch (sectionCount) {
                    case 1:
                        range = Integer.parseInt(section);
                        gameGrid = new GameGrid(range);
                        break;
                    case 2:
                        producerCount = Character.getNumericValue(section.charAt(0));
                        receiverCount = Character.getNumericValue(section.charAt(1));
                        break;
                    case 3:
                        createKeys(section, producerKeys);
                        break;
                    case 4:
                        createKeys(section, receiverKeys);
                        break;
                    case 5: 
                        createPaths(section, componentPosition, producerKey, receiverKey);
                        mapGameGrid(section, componentPosition, range, coordinateFind);
                        break;
                }
                section = "";
                sectionCount++;
            }
        }
        producerAndReceiverMatch(componentPosition, producerCount, receiverCount);
        createConnections(section, componentPosition, coordinateFind);
        bufferedReader.close();
        return gameGrid;
    }

    /**
     * Checks to see that the number of producers and receivers is the same as the given number from
     * the text file
     * 
     * @param componentPosition A HashMap containing all the positions of the GridComponents
     * @param producerCount The number of Producers listed in the text file
     * @param receiverCount The number of Receivers listed in the text file
     * @throws FileFormatException throws if the number of actual producers/receivers is different
     * the number given from the text file
     */
    private static void producerAndReceiverMatch(HashMap<Integer, Path> componentPosition, 
            int producerCount, int receiverCount) throws FileFormatException {
        var entrySet = componentPosition.entrySet();
        int actualProducerCount = 0;
        int actualReceiverCount = 0;
        for (var entry : entrySet) {
            if (entry.getValue().getNode() instanceof Producer) {
                actualProducerCount++;
            }
            if (entry.getValue().getNode() instanceof Receiver) {
                actualReceiverCount++;
            }
        }
        if (actualProducerCount != producerCount || actualReceiverCount != receiverCount) {
            throw new FileFormatException();
        }
    }

    /**
     * Checks to see if there is a satisfactory amount of underscores for the section separator
     * 
     * @param line A String that contains the section separators
     * @throws FileFormatException throws when there is less than 5 underscores in a separator
     */
    private static void satisfactoryLineCount(String line) throws FileFormatException {
        int requiredUnderscores = 5;
        int underscoreCount = 0;
        for (char character : line.toCharArray()) {
            if (character == '_') {
                underscoreCount++;
            }
        }
        if (underscoreCount < requiredUnderscores) {
            throw new FileFormatException();
        }
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

        // Adds the new items to a list
        for (int count = 1; count <= section.length(); count++) {
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
     * @param coordinateFind Helps to find Coordinate of specific GridComponents
     * @throws FileFormatException Throws when a producer and receiver are connecting together
     */
    private static void createConnections(String section, HashMap<Integer, Path> componentPosition,
            HashMap<GridComponent, Coordinate> coordinateFind) throws FileFormatException {
        String connections = "";

        // Finding the GridComponents per line
        for (char character : section.toCharArray()) {
            if (character != ' ') {
                connections += character;
            } else {
                int startId = findStartId(connections);
                Path startPath = componentPosition.get(startId);

                int endId = findEndId(connections);
                Path endPath = componentPosition.get(endId);
                Path middlePath;

                // Creating connections depending on the GridComponent type
                if (connections.contains(",")) {
                    if (connections.charAt(connections.indexOf("-") + 1) != ',') {
                        int middleId = 0;
                        int start = connections.indexOf("-") + 1;
                        int end = connections.indexOf(",");
                        if (end - start == 2) {
                            middleId = Integer.parseInt(connections.substring(start, end));
                        } else {
                            middleId = Character.getNumericValue(connections.charAt(connections
                                    .indexOf("-") + 1));
                        }
                        producerReceiverCheck(startPath);
                        middlePath = componentPosition.get(middleId);
                        addBackwardsConnection(coordinateFind, startPath, middlePath);
                    } 
                    if (connections.charAt(connections.length() - 1) != ',') {
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
     * Checks to see if that path instance passsed through is either a Producer or a Receiver
     * @param path A path to be tested for its instance
     * @throws FileFormatException throws if that Path given is a Producer or Receiver
     */
    private static void producerReceiverCheck(Path path) throws FileFormatException {
        if (path.getNode() instanceof Producer || path.getNode() instanceof Receiver) {
            throw new FileFormatException();
        }
    }

    /**
     * Finds the starting ID for each line
     * 
     * @param connections A string which shows the ID's the GridComponents have
     * @return an integer representing the id of the first GridComponent
     */
    private static int findStartId(String connections) {
        int startId;
        int numberEnder = connections.indexOf("-");
        if (numberEnder == 2) {
            startId = Integer.parseInt(connections.substring(0, numberEnder));
        } else {
            startId = Character.getNumericValue(connections.charAt(0));
        }
        return startId;
    }

    /**
     * Finds the last ID of a line
     * 
     * @param connections A string which shows what ID's the GridComponents have
     * @return An integer representing the id of the last GridComponent
     */
    private static int findEndId(String connections) {
        int numberStarter = 0;
        int endId;
        if (connections.contains(",")) {
            numberStarter = connections.indexOf(",") + 1;
        } else {
            numberStarter = connections.indexOf("-") + 1;
        }
        
        // Creates connection depending if the id is double digits or not
        if (connections.length() - numberStarter == 2) {
            endId = Integer.parseInt(connections.substring(numberStarter, connections.length()));
        } else {
            endId = Character.getNumericValue(connections.charAt(connections.length() - 1));
        }
        return endId;
    }

    /**
     * Creates a backwards Connection between two Transport Nodes
     * 
     * @param startPath the Path that contains the Transport Node to be connected
     * @param endPath the second Path that contains the Transport Node to be connected
     * @param coordinateFind A hashmap containing the coordinate of each Transport Node
     * @throws FileFormatException throws when a Producer is connecting to a Receiver
     */
    private static void addForwardConnection(HashMap<GridComponent, Coordinate> coordinateFind, 
            Path startPath, Path endPath) throws FileFormatException {
        Transport startTransport = (Transport) gameGrid.getGrid().get(coordinateFind
                .get(startPath.getNode()));
        Transport endTransport = (Transport) gameGrid.getGrid().get(coordinateFind
                .get(endPath.getNode()));

        if (startTransport instanceof Producer && endTransport instanceof Receiver) {
            throw new FileFormatException();
        }
        startTransport.setOutput(endTransport.getPath());
        endTransport.setInput(startTransport.getPath());
    }

    /**
     * Creates a Forward Connection between two Transport Nodes
     * 
     * @param startPath the Path that contains the Transport Node to be connected
     * @param endPath the second Path that contains the Transport Node to be connected
     * @param coordinateFind A hashmap containing the coordinate of each Transport Node
     * @throws FileFormatException throws when a Receiver is connecting to a Producer
     */
    private static void addBackwardsConnection(HashMap<GridComponent, Coordinate> coordinateFind, 
            Path startPath, Path endPath) throws FileFormatException {
        Transport startTransport = (Transport) gameGrid.getGrid().get(coordinateFind.get(startPath
                .getNode()));
        Transport endTransport = (Transport) gameGrid.getGrid().get(coordinateFind.get(endPath
                .getNode()));

        if (startTransport instanceof Receiver && endTransport instanceof Producer) {
            throw new FileFormatException();
        }
        startTransport.setInput(endTransport.getPath());
        endTransport.setOutput(startTransport.getPath());
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
            Item producerKey, Item receiverKey) throws FileFormatException {
        // Removing unnecessary whitespace and parts that are not Transport Nodes
        section = section.replace(" ", "").replace("w", "")
                .replace("o", "");
        
        // Creates different components depending on what is in the section
        for (int count = 0; count < section.length(); count++) {
            switch (section.charAt(count)) {
                case 'p':
                    componentPosition.put(count + 1, new Path(new Producer(count + 1, 
                            producerKey)));
                    break;
                case 'r':
                    componentPosition.put(count + 1, new Path(new Receiver(count + 1, 
                            receiverKey)));
                    break;
                case 'b':
                    componentPosition.put(count + 1, new Path(new Belt(count + 1)));
                    break;
                default:
                    throw new FileFormatException();
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
     * @throws FileFormatException throws when there are two many elements than intended
     */
    private static void mapGameGrid(String section, HashMap<Integer, Path> componentPosition, 
            int range, HashMap<GridComponent, Coordinate> coordinateFind) 
            throws FileFormatException {
        String gridLayout = section.replace(" ", "");
        
        int originNumber = 0;
        Coordinate origin = new Coordinate(originNumber, -range);
        List<Integer> elementsPerRow = rowCalculator(range);
        GridComponent gridComponent = () -> "";
        Coordinate direction = origin;

        boolean originFound = false;
        int idCounter = 0;
        int rowCounter = 0;

        for (int count = 0; count < gridLayout.length(); count++) {

            // Assigning the correct GridComponent to be mapped
            if (gridLayout.charAt(count) == 'w') {
                gridComponent = () -> "w";
            } else if (gridLayout.charAt(count) == 'o') {
                gridComponent = () -> "o";
            } else if (gridLayout.charAt(count) != ' ') {
                idCounter++;
                gridComponent = componentPosition.get(idCounter).getNode();
            }

            // Finding the Coordinate for the GridComponent
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
                    rowCounter++;
                }
            }
            // Makes sure there is the correct number of elements per row
            if (rowCounter == (range * 2) + 1) {
                throw new FileFormatException();
            }
            coordinateFind.put(gridComponent, direction);
            gameGrid.setCoordinate(direction, gridComponent);
            elementsPerRow.set(rowCounter, elementsPerRow.get(rowCounter) - 1);
        }
    }

    /**
     * Calculates the amount of elements per row using the range
     * 
     * @param range an integer representing the range of the Grid
     * @return an ArrayList with the amount of elements per row
     */
    private static ArrayList<Integer> rowCalculator(int range) {
        ArrayList<Integer> elementsPerRow = new ArrayList<>();

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
