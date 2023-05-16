package lms.exceptions;

/**
 * A class that represents a BadStateException
 */
public class BadStateException extends RuntimeException {
    /** Constructs a BadStateException when no argument is given */
    public BadStateException() {
        super();
    }

    /** Constructs a BadStateException when a String message is passed 
     * 
     * @param message
    */
    public BadStateException(String message) {
        super(message);
    }

    /** Constructs a BadStateException when a String message and Throwable cause is passed 
     * 
     * @param message
     * @param cause
    */
    public BadStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a BadStateException when a Throwable is passed 
     * 
     * @param cause
    */
    public BadStateException(Throwable cause) {
        super(cause);

    }


    
}
