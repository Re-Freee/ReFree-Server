package refree.backend.infra.exception;

public class NotFoundException extends IllegalArgumentException { // 400에러

    public NotFoundException(String message) {
        super(message);
    }
}
