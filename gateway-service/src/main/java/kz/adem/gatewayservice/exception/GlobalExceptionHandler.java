package kz.adem.gatewayservice.exception;

import kz.adem.gatewayservice.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//Class to handle all exceptions to this service
@ControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleMethodArgumentNotValid(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return Mono.just(new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorDetails>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public Mono<ResponseEntity<ErrorDetails>> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .details("Unauthorized")
                .build();
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorDetails>> handleGlobalException(Exception ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
