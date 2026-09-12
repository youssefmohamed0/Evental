package joe.app.EventReservationApp.Exception;

public class InvalidPaymentArgumentException extends RuntimeException {
    public InvalidPaymentArgumentException(String message) {
        super(message);
    }
}
