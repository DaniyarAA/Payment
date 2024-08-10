package kg.attractor.payment.errors;

import java.util.NoSuchElementException;

public class NotFoundUserException extends NoSuchElementException {

    public NotFoundUserException() {
        super();
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
