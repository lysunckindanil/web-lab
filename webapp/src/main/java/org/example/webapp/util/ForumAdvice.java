package org.example.webapp.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
public class ForumAdvice {
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, BadRequestException.class.getName());
        problemDetail.setProperty("error", e.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler({HttpMessageConversionException.class})
    public ResponseEntity<?> handleHttpMessageConversionException() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, HttpMessageConversionException.class.getName());
        problemDetail.setProperty("error", "JSON parsing error");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler({ClosedChannelException.class})
    public void handleClosedChannelException(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }
}
