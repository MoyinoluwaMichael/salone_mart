package africa.techimmortal.martbackend.infrastructure.exception.handler;

import africa.techimmortal.martbackend.infrastructure.exception.MartException;
import africa.techimmortal.martbackend.infrastructure.exception.MediaUploadFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final ExceptionResponse exceptionResponse = new ExceptionResponse();

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> martException(MartException ex){
        log.error("MartException {}", ex.getLocalizedMessage());
        exceptionResponse.setMessage(ex.getLocalizedMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setErrorCode(BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exception(Exception ex){
        log.error("Exception::>> {}", ex.toString());
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setErrorCode(BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException::>> {}", ex.toString());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        exceptionResponse.setMessage(errors.toString());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setErrorCode(BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MediaUploadFailedException errors) {
        log.error("Exception::>> {}", errors.toString());
        exceptionResponse.setMessage(errors.toString());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setErrorCode(BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, BAD_REQUEST);
    }
}
