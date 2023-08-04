package refree.backend.infra.exception;

public class PaymentRequiredException extends IllegalArgumentException { // 402에러

    public PaymentRequiredException(String message) {
        super(message);
    }
}

