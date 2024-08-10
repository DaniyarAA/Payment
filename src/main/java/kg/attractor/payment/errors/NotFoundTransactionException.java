package kg.attractor.payment.errors;

public class NotFoundTransactionException extends RuntimeException{
    public NotFoundTransactionException(){
        super();
    }
    public NotFoundTransactionException(String message){
        super(message);
    }
}
