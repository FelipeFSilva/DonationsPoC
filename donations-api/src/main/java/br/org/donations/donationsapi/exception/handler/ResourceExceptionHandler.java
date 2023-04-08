package br.org.donations.donationsapi.exception.handler;


import br.org.donations.donationsapi.exception.AuthenticationException;
import br.org.donations.donationsapi.exception.AuthorizationException;
import br.org.donations.donationsapi.exception.ValidationException;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidation(ValidationException exception, HttpServletRequest request){

        HttpStatus statusError = HttpStatus.UNPROCESSABLE_ENTITY;
        Problem problem = getProblem(exception.getMessage(), statusError.value());
        problem.setPath(request.getRequestURI());

        return ResponseEntity.status(statusError).body(problem);
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<Object> handleServiceUnavailable(RetryableException exception, HttpServletRequest request){

        HttpStatus statusError = HttpStatus.SERVICE_UNAVAILABLE;
        Problem problem = getProblem(exception.getMessage(), statusError.value());
        problem.setPath(request.getRequestURI());

        return ResponseEntity.status(statusError).body(problem);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, HttpServletRequest request){

        HttpStatus statusError = HttpStatus.UNAUTHORIZED;
        Problem problem = getProblem(exception.getMessage(), statusError.value());

        return ResponseEntity.status(statusError).body(problem);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> handleAuthorizationException(AuthorizationException exception, HttpServletRequest request){

        HttpStatus statusError = HttpStatus.FORBIDDEN;
        Problem problem = getProblem(exception.getMessage(), statusError.value());

        return ResponseEntity.status(statusError).body(problem);
    }

    private Problem getProblem(String title, int status){
        return Problem.builder()
                .title(title)
                .status(status)
                .dateAndTime(OffsetDateTime.now())
                .build();
    }

}
