package joe.app.EventReservationApp.Controller;

import jakarta.servlet.http.HttpServletRequest;
import joe.app.EventReservationApp.DTO.ErrorResponseDTO;
import joe.app.EventReservationApp.Exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(UserCreationConflictException.class)
    public ResponseEntity<ErrorResponseDTO> UserCreationConflict(UserCreationConflictException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("User exception: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> userInvalid(UsernameNotFoundException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("User exception: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenRefreshException(TokenRefreshException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.FORBIDDEN.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Token refresh exception: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventNotFoundException(EventNotFoundException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Event not found: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidEventArgumentsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidEventArgumentsException(InvalidEventArgumentsException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Invalid event arguments: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(VenueNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleVenueNotFoundException(VenueNotFoundException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Venue not found: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedEventAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedEventAccessException(UnauthorizedEventAccessException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.FORBIDDEN.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Unauthorized event access: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(InvalidPaymentArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPaymentArgumentException(InvalidPaymentArgumentException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Invalid payment argument: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidPaymentStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPaymentStateException(InvalidPaymentStateException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Invalid payment state: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidReservationArgumentsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidReservationArgumentsException(
            InvalidReservationArgumentsException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Invalid reservation arguments: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleReservationConflictException(ReservationConflictException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.CONFLICT.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Reservation conflict: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleReservationNotFoundException(ReservationNotFoundException ex,
            HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Reservation not found: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedReservationAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedReservationAccessException(
            UnauthorizedReservationAccessException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.FORBIDDEN.value(),
                httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Unauthorized reservation access: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

}
