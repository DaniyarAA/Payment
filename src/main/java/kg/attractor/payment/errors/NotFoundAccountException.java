package kg.attractor.payment.errors;

import java.util.NoSuchElementException;

public class NotFoundAccountException extends NoSuchElementException {
    public NotFoundAccountException() {
        super();
    }
    public NotFoundAccountException(String message) {
        super(message);
    }
}
