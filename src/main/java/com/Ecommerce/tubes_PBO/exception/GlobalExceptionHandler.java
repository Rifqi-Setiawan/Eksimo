package com.Ecommerce.tubes_PBO.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice // Menjadikan kelas ini sebagai global exception handler
public class GlobalExceptionHandler {

    // Handler untuk ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(), // Pesan dari exception
                request.getDescription(false), // Deskripsi URI request
                HttpStatus.NOT_FOUND.value() // Kode status
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Handler untuk BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handler untuk validasi DTO (@Valid) yang gagal
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailsWithFields> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value"
                ));

        ErrorDetailsWithFields errorDetails = new ErrorDetailsWithFields(
                new Date(),
                "Validation Failed",
                request.getDescription(false),
                HttpStatus.BAD_REQUEST.value(),
                fieldErrors
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    // Handler untuk AuthException (jika Anda ingin format respons yang sama)
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorDetails> handleAuthException(AuthException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value() // Atau HttpStatus yang sesuai dari AuthException Anda
        );
        // Sesuaikan HttpStatus berdasarkan detail AuthException jika perlu
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // Handler untuk AuthenticationException dari Spring Security (misalnya, bad credentials)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Authentication Failed: " + ex.getMessage(), // Pesan dari Spring Security
                request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // Handler untuk AccessDeniedException dari Spring Security (misalnya, tidak punya role yang tepat)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Access Denied: " + ex.getMessage(), // Pesan dari Spring Security
                request.getDescription(false),
                HttpStatus.FORBIDDEN.value()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }


    // Handler umum untuk semua exception lain yang tidak tertangani
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "An unexpected error occurred: " + ex.getMessage(), // Hindari expose detail internal di produksi
                request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        // TODO: Log error ex secara detail di sisi server
        // logger.error("Unhandled exception:", ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Kelas internal untuk struktur respons error standar
    // Anda bisa memindahkannya ke file terpisah jika diinginkan (misal, di package dto.response atau dto.error)
    public static class ErrorDetails {
        private Date timestamp;
        private String message;
        private String details; // Biasanya URI
        private int statusCode;

        public ErrorDetails(Date timestamp, String message, String details, int statusCode) {
            super();
            this.timestamp = timestamp;
            this.message = message;
            this.details = details;
            this.statusCode = statusCode;
        }

        // Getter
        public Date getTimestamp() { return timestamp; }
        public String getMessage() { return message; }
        public String getDetails() { return details; }
        public int getStatusCode() { return statusCode; }
    }
    
    // Kelas internal untuk struktur respons error dengan field errors (untuk validasi)
    public static class ErrorDetailsWithFields extends ErrorDetails {
        private Map<String, String> fieldErrors;

        public ErrorDetailsWithFields(Date timestamp, String message, String details, int statusCode, Map<String, String> fieldErrors) {
            super(timestamp, message, details, statusCode);
            this.fieldErrors = fieldErrors;
        }

        // Getter
        public Map<String, String> getFieldErrors() { return fieldErrors; }
    }
}