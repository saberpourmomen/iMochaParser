package com.imocha.parser.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ApiErrorResponse> handleParse(
            ParseException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.UNPROCESSABLE_ENTITY,
                "File parsing failed: " + ex.getMessage(), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxSize(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.PAYLOAD_TOO_LARGE,
                "Uploaded file is too large", request);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiErrorResponse> handleMultipart(
            MultipartException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.BAD_REQUEST,
                "Invalid multipart request", request);
    }

    @ExceptionHandler(JobExecutionException.class)
    public ResponseEntity<ApiErrorResponse> handleBatchJob(
            JobExecutionException ex,
            HttpServletRequest request) {

        log.error("Spring Batch job failed", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Batch job execution failed", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraint(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.CONFLICT,
                "Database constraint violation", request);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiErrorResponse> handleSql(
            SQLException ex,
            HttpServletRequest request) {

        log.error("SQL error", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Database error occurred", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error occurred", request);
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }
}

