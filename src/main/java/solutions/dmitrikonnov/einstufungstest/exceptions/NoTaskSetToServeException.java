package solutions.dmitrikonnov.einstufungstest.exceptions;

public class NoTaskSetToServeException extends RuntimeException {
    public NoTaskSetToServeException(String message) {
        super(message);
    }
}
