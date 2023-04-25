package co.empathy.academy.search.controller;

import co.empathy.academy.search.exception.NoSearchResultException;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {FileNotFoundException.class})
    protected ResponseEntity<?> handleFileError(Exception ex, WebRequest request) {
        JsonObject body = Json.createObjectBuilder().add("message", "Error in file upload").build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {NoSearchResultException.class})
    protected ResponseEntity<?> handleNoSearchResult(Exception ex, WebRequest request) {
        JsonObject body = Json.createObjectBuilder().add("message", "No search result").build();
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
