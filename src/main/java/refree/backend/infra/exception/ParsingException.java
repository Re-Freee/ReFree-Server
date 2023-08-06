package refree.backend.infra.exception;

public class ParsingException extends IllegalArgumentException {

    public ParsingException(String message) {
        super(message);
    }
}
