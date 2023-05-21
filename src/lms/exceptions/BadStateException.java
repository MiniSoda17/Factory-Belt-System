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
     * @param message A string that will show when this Exception occurs
    */
    public BadStateException(String message) {
        super(message);
    }

    /** Constructs a BadStateException when a String message and Throwable cause is passed 
     * 
     * @param message A string that will show when this Exception is thrown
     * @param cause The exception that occured 
    */
    public BadStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a BadStateException when a Throwable is passed 
     * 
     * @param cause The exception that occured
    */
    public BadStateException(Throwable cause) {
        super(cause);

    }


    
}
