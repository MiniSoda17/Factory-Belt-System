package lms.exceptions;

/**
 * A class that represents the UnsupportedActionException class
 */
public class UnsupportedActionException extends RuntimeException {
    /** Constructs an UnsupportedActionException when no arguments are passed */
    public UnsupportedActionException() {
        super();
    }

    /** Constructs an UnsupportedActionException when a String is passed through */
    public UnsupportedActionException(String message) {
        super(message);
    }

    /** Constructs an UnsupportedException when a String and a Throwable is passed through */
    public UnsupportedActionException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs an UnsupportedActionException when a Throwable is passed */
    public UnsupportedActionException(Throwable cause) {
        super(cause);
    }
    
}
