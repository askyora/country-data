package com.rc.country.config;

import com.rc.country.dto.ApiError;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ControllerAdvice
public class ControllerAdvisorConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            RuntimeException ex, WebRequest request) {
        return defaultError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getLocalizedMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            HttpClientErrorException ex, WebRequest request) {
        return defaultError(ex.getStatusCode(), ex.getStatusText(), ex.getLocalizedMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(String.format("Class : [%s] , Path : [%s] , Message : [%s]", violation.getRootBeanClass().getName(),
                    violation.getPropertyPath(), violation.getMessage()));
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return defaultError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getParameterName() +
                " parameter is missing");
    }

    private ResponseEntity<Object> defaultError(HttpStatus badRequest, String reasonPhrase, String localizedMessage) {
        ApiError apiError =
                new ApiError(badRequest, reasonPhrase, List.of(localizedMessage));
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}