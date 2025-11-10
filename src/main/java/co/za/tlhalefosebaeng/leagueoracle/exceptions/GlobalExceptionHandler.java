package co.za.tlhalefosebaeng.leagueoracle.exceptions;

import co.za.tlhalefosebaeng.leagueoracle.response.MessageResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${api.environment}")
    private String apiEnvironment;

    @ExceptionHandler({ AppException.class })
    public ResponseEntity<MessageResponse> handleAppException(AppException e) {
        return ResponseEntity.status(e.getStatus()).body(new MessageResponse(e.getMessage()));
    }

    // A JWT exception thrown when the provided token has expired
    @ExceptionHandler({ ExpiredJwtException.class })
    public ResponseEntity<MessageResponse> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Your token has expired! Please login to continue"));
    }

    // Handle all the other exceptions that extends the JwtException class
    @ExceptionHandler({ JwtException.class })
    public ResponseEntity<MessageResponse> handleJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid token! Please check your token and try again"));
    }

    // A spring authentication thrown when an authentication request is rejected
    @ExceptionHandler({ InsufficientAuthenticationException.class })
    public ResponseEntity<MessageResponse> handleAuthenticationException(InsufficientAuthenticationException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("You are not logged in! Please login to continue"));
    }

    // A spring exception raises when a resource was not found (e.g. a provided URL has no mapper)
    @ExceptionHandler({ NoResourceFoundException.class })
    public ResponseEntity<MessageResponse> handleNoResourceFoundException(NoResourceFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("/" + e.getResourcePath() + " not found"));
    }

    // A spring exception thrown when the http message cannot be read for conversion (e.g. the required body of a post request was not provided)
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<MessageResponse> handleHttpMessageNotReadableException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Request body not readable"));
    }

    // A spring exception thrown when a resource exists but the http method handler is not found under the mappers of that resource
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<MessageResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request){
        // Derive a descriptive message to send back to the user
        String message = "HTTP method " + e.getMethod() + " not supported for resource " + request.getRequestURI();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    // A spring exception thrown when validation on object fields of an argument annotated with @Valid fails
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<MessageResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // Retrieve the first error message from the exception
        ObjectError error = e.getBindingResult().getAllErrors().get(0);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(error.getDefaultMessage()));
    }

    // A spring exception thrown when a required parameter is missing in the request
    @ExceptionHandler({ MissingServletRequestParameterException.class })
    public ResponseEntity<MessageResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(e.getParameterName() + " parameter required! Please provide a valid " + e.getParameterName()));
    }

    // A spring exception that reports the result of constraint violations
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<MessageResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<ConstraintViolation<?>> violations = new ArrayList<>(e.getConstraintViolations());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(violations.get(0).getMessage()));
    }

    // A spring exception thrown when the integrity of an entity has been violated
    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<MessageResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable cause = e.getRootCause();

        if(cause instanceof SQLException sqlException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(this.deriveSQLExceptionMessage(sqlException)));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Something went really bad! Please try again later"));
    }

    // A java exception thrown when an entity constraint has been violated
    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
    public ResponseEntity<MessageResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(this.deriveSQLExceptionMessage(e)));
    }

    // A spring type mismatch exception raised while resolving a controller method argument
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid field type provided! Please check your field(s) and try again"));
    }

    // A general exception - used to handle uncaught exceptions
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<MessageResponse> handleUnknownExceptions(Exception e) {
        if(!apiEnvironment.equals("production")) System.out.println(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Something went really bad! Please try again later"));
    }

    // Helper method to derive an error message from the sql exception
    private String deriveSQLExceptionMessage(SQLException e) {
        String errorMessage = "";
        if(e.getErrorCode() == 1062) {
            // Handle duplicate key constraint violation
            errorMessage = "Some field already exists! Please use a different one";
        } else if(e.getErrorCode() == 1048) {
            // Handle null key constraint violation
            errorMessage = "Invalid input data! Please check fields and try again";
        }

        return errorMessage;
    }
}
