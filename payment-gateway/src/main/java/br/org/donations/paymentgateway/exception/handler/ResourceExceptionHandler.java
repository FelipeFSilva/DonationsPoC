package br.org.donations.paymentgateway.exception.handler;


import br.org.donations.paymentgateway.exception.AuthenticationException;
import br.org.donations.paymentgateway.exception.AuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus statusError = HttpStatus.UNPROCESSABLE_ENTITY;
        Problem problem = getProblem("Um ou mais campos estão inválidos.", statusError.value());
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
