package joe.app.EventReservationApp.Controller;

import jakarta.servlet.http.HttpServletRequest;
import joe.app.EventReservationApp.DTO.ErrorResponseDTO;
import joe.app.EventReservationApp.Exception.TokenRefreshException;
import joe.app.EventReservationApp.Exception.UserCreationConflictException;
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
    public ResponseEntity<ErrorResponseDTO> UserCreationConflict(UserCreationConflictException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(), httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("User exception: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> userInvalid(UsernameNotFoundException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("User exception: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenRefreshException(TokenRefreshException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.FORBIDDEN.value(), httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Token refresh exception: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(joe.app.EventReservationApp.Exception.EventNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventNotFoundException(joe.app.EventReservationApp.Exception.EventNotFoundException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(), httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Event not found: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(joe.app.EventReservationApp.Exception.VenueNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleVenueNotFoundException(joe.app.EventReservationApp.Exception.VenueNotFoundException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(), httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Venue not found: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(joe.app.EventReservationApp.Exception.UnauthorizedEventAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedEventAccessException(joe.app.EventReservationApp.Exception.UnauthorizedEventAccessException ex, HttpServletRequest httpServletRequest) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getMessage(), HttpStatus.FORBIDDEN.value(), httpServletRequest.getRequestURI(), LocalDateTime.now());
        log.warn("Unauthorized event access: {} path: {}", ex.getMessage(), httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
