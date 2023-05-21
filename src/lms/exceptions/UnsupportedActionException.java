package lms.exceptions;

/**
 * A class that represents the UnsupportedActionException class
 */
public class UnsupportedActionException extends RuntimeException {
    
    /** 
     * Constructs an UnsupportedActionException when no arguments are passed 
    */
    public UnsupportedActionException() {
        super();
    }

    /** Constructs an UnsupportedActionException when a String is passed through 
     * 
     * @param message A string that will show when this Exception is thrown
    */
    public UnsupportedActionException(String message) {
        super(message);
    }

    /** Constructs an UnsupportedException when a String and a Throwable is passed through 
     * 
     * @param message A string that will be shown when this Exception is thrown
     * @param cause A Throwable that occurred when an operation was perfomed
    */
    public UnsupportedActionException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs an UnsupportedActionException when a Throwable is passed 
     * 
     * @param cause A Throwable that occured when an operation was performed
    */
    public UnsupportedActionException(Throwable cause) {
        super(cause);
    }
    
}
