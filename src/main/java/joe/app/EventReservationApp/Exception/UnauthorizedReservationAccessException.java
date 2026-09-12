package joe.app.EventReservationApp.Exception;

public class UnauthorizedReservationAccessException extends RuntimeException {
    public UnauthorizedReservationAccessException(String message) {
        super(message);
    }
}
