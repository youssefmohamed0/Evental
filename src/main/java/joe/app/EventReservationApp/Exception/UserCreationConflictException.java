package joe.app.EventReservationApp.Exception;

public class UserCreationConflictException extends RuntimeException {
    public UserCreationConflictException(String message) {
        super(message);
    }
}
