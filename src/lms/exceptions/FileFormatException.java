package lms.exceptions;

/** A class that represents a FileFormatException */
public class FileFormatException extends Exception{

    /** Constructs a FileFormatException when no argument is passed */
    public FileFormatException() {
        super();
    }

    /** Constructs a FileFormatException when a String is passed through */
    public FileFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a FileFormatException when a String and integer are passed 
     * 
     * @param message A string that will be displayed
     * @param lineNum An integer that will be stored in this Exception
     */
    public FileFormatException(String message, int lineNum) {
        super(String.format("%s (line: %s)", message, lineNum));
    }
    
    /**
     * Constructs a FileFormatException when a String, Integer and Throwable are passed
     * 
     * @param message A string that will be displayed
     * @param lineNum An integer that will be stored in this Exception
     * @param cause 
     */
    public FileFormatException(String message, int lineNum, Throwable cause) {
        super(String.format("%s (line: %s)", message, lineNum), cause);
    }

    /**
     * Constructs a FileFormatException when a String and Throwable are passed
     * @param message A string that will be displayed
     * @param cause 
     */
    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a FileFormatException when a Throwable is passed
     * 
     * @param cause
     */
    public FileFormatException(Throwable cause) {
        super(cause);
    }

    

}
