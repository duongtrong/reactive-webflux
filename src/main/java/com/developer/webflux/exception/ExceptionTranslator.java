package com.developer.webflux.exception;

import com.developer.webflux.constant.ResponseEntityStatusConstant;
import com.developer.webflux.dto.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;

@Slf4j
@ControllerAdvice
@SuppressWarnings({"rawtypes"})
public class ExceptionTranslator extends ResponseEntityExceptionHandler implements ResponseErrorHandler {

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ResponseEntityStatusConstant.BAD_REQUEST.getCode(),
                ex.getMessage());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ResponseEntityStatusConstant.BAD_REQUEST.getCode(),
                ResponseEntityStatusConstant.BAD_REQUEST.getMessage());
        errorDetails.addValidationError(ex.getBindingResult().getGlobalErrors());
        errorDetails.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(ClassCastException.class)
    public final ResponseEntity<Object> handleClassCastException(ClassCastException e) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code(ResponseEntityStatusConstant.BAD_REQUEST.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<Object> handleCustomerException(CustomException e) {
        ErrorDetails errorDetails = new ErrorDetails(ResponseEntityStatusConstant.BAD_REQUEST.getCode(),
                e.getMessage());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        HttpStatus status = clientHttpResponse.getStatusCode();
        return status.is4xxClientError() || status.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        String responseAsString = toString(clientHttpResponse.getBody());
        throw new CustomException(responseAsString);
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        String responseAsString = toString(response.getBody());
        throw new CustomException(responseAsString);
    }

    private String toString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
