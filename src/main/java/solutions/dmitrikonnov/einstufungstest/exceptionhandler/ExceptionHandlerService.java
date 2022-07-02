package solutions.dmitrikonnov.einstufungstest.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import solutions.dmitrikonnov.einstufungstest.exceptions.ThresholdNotFoundException;

@RestController
@ControllerAdvice
public class ExceptionHandlerService {
    public static Logger log = LoggerFactory.getLogger(ExceptionHandlerService.class);

    @ExceptionHandler(ThresholdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> ThresholdNotFoundException (ThresholdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
