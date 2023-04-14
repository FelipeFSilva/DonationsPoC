package br.org.donations.authorizationapi.exception.handler;

import br.org.donations.authorizationapi.dto.exceptions.ProblemDTO;
import br.org.donations.authorizationapi.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
        ProblemDTO problem = getProblem("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", statusError.value());
        return ResponseEntity.status(statusError).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus statusError = HttpStatus.UNPROCESSABLE_ENTITY;
        ProblemDTO problem = getProblem("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", statusError.value());
        return ResponseEntity.status(statusError).body(problem);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, HttpServletRequest request){
        HttpStatus statusError = HttpStatus.UNAUTHORIZED;
        ProblemDTO problem = getProblem(exception.getMessage(), statusError.value());
        problem.setPath(request.getRequestURI());
        return ResponseEntity.status(statusError).body(problem);
    }

    private ProblemDTO getProblem(String title, int status){
        return ProblemDTO.builder()
                .title(title)
                .status(status)
                .dateAndTime(OffsetDateTime.now())
                .build();
    }
}
