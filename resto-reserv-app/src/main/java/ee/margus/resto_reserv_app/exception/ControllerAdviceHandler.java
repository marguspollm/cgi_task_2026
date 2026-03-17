package ee.margus.resto_reserv_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.BAD_REQUEST);
        errorMessage.setMessage("Validation failed");
        errorMessage.setErrors(errors);
        errorMessage.setDate(new Date());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setDate(new Date());

        return ResponseEntity.internalServerError().body(errorMessage);
    }

}