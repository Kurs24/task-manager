package com.refresh.javatask.handler;

import com.refresh.javatask.exception.BusinessRuleException;
import com.refresh.javatask.exception.ResourceNotFoundException;
import com.refresh.javatask.responsedto.APIError;
import com.refresh.javatask.responsedto.FieldViolation;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                  HttpServletRequest request) {
        log.warn("Resource Not Found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(BusinessRuleException ex, HttpServletRequest request) {
        log.warn("Business rule violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        log.warn("Method argument not valid: {}", ex.getMessage());
        return buildResponse(status, "Validation Error!", ((ServletWebRequest) request).getRequest().getRequestURI(),
                ex.getBindingResult().getFieldErrors().stream()
                        .map(errs -> new FieldViolation(errs.getField(), errs.getDefaultMessage())).toList());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        log.warn("Http message not readable: {}", ex.getMessage());
        return buildResponse(status, "JSON or Message not readable!",
                ((ServletWebRequest) request).getRequest().getRequestURI(), null);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, @Nullable HttpHeaders headers,
                                                        @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        log.warn("Type mismatch: {}", ex.getMessage());
        return buildResponse(status, "Type mismatch for Parameter " + ex.getPropertyName() + "!",
                ((ServletWebRequest) request).getRequest().getRequestURI(), null);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode,
                                                             @NonNull WebRequest request) {
        log.warn("Error: {}", ex.getMessage());
        String errDetail = (ex instanceof org.springframework.web.ErrorResponse er) ? er.getBody().getDetail() : null;
        return ResponseEntity.status(statusCode).headers(headers).body(
                new APIError(Instant.now(), statusCode.value(),
                        HttpStatus.valueOf(statusCode.value()).getReasonPhrase(),
                        errDetail != null ? errDetail : "Request Error", getUrlPath(request), null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOther(Exception ex, WebRequest request) {
        log.error("Internal Server Error: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                ((ServletWebRequest) request).getRequest().getRequestURI(), null);
    }

    private ResponseEntity<Object> buildResponse(HttpStatusCode code, String detail, String location,
                                                 List<FieldViolation> errors) {
        APIError err =
                new APIError(Instant.now(), code.value(), HttpStatus.valueOf(code.value()).getReasonPhrase(), detail,
                        location, errors);
        return ResponseEntity.status(code).body(err);
    }

    private String getUrlPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}
