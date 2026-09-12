package joe.app.EventReservationApp.Exception;

public class InvalidReservationArgumentsException extends RuntimeException {
    public InvalidReservationArgumentsException(String message) {
        super(message);
    }
}
