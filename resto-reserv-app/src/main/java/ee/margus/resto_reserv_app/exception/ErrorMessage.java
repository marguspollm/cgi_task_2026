package ee.margus.resto_reserv_app.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Data
public class ErrorMessage {
    private String message;
    private HttpStatus status;
    private Date date;
    private Map<String, String> errors;
}
