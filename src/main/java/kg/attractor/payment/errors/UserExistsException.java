package kg.attractor.payment.errors;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super();
    }
    public UserExistsException(String message) {
        super(message);
    }
}
