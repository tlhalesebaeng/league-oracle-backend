package co.za.tlhalefosebaeng.leagueoracle.exceptions;

import co.za.tlhalefosebaeng.leagueoracle.response.MessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${api.environment}")
    private String apiEnvironment;

    // Handle custom runtime exceptions
    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<MessageResponse> handleCustomExceptions(ResourceNotFoundException e) {
        return ResponseEntity.status(e.getCode()).body(new MessageResponse(e.getMessage()));
    }

    // Handle app exceptions

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<MessageResponse> handleHttpMessageNotReadableException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Request body not readable"));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<MessageResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // Retrieve the first error message from the exception
        ObjectError error = e.getBindingResult().getAllErrors().get(0);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(error.getDefaultMessage()));
    }

    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
    public ResponseEntity<MessageResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        String errorMessage = "";
        if(e.getErrorCode() == 1062) {
            // Handle duplicate key constraint violation
            errorMessage = "Some field already exists! Please use a different one";
        } else if(e.getErrorCode() == 1048) {
            // Handle null key constraint violation
            errorMessage = "Invalid input data! Please check fields and try again";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorMessage));
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid field type provided! Please check your field(s) and try again"));
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<MessageResponse> handleUnknownExceptions(Exception e) {
        if(!apiEnvironment.equals("production")) System.out.println(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Something went really bad! Please try again later"));
    }
}
