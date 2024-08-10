package kg.attractor.payment.errors;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(){
        super();
    }
    public InsufficientFundsException(String message){
        super(message);
    }
}
