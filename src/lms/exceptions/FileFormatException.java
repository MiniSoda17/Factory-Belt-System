package lms.exceptions;

public class FileFormatException extends Exception{
    public int lineNum;

    public FileFormatException() {
        super();
    }

    public FileFormatException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param lineNum
     */
    public FileFormatException(String message, int lineNum) {
        super(message);
        this.lineNum = lineNum;
    }

    public FileFormatException(String message, int lineNum, Throwable cause) {
        super(message, cause);
        this.lineNum = lineNum;
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileFormatException(Throwable cause) {
        super(cause);
    }

    

}
