package com.example.procdpchallenger.adapter.inbound.contoroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.procdpchallenger.adapter.inbound.dto.ErrorResponse;
import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.domain.exception.InfrastructureException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DomainRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainRuleViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(BusinessRuleViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(InfrastructureException ex) {
      // エンドユーザーには一般化されたエラーメッセージを表示
        log.error("Infrastructure error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(ex.getErrorCode(), "An unexpected error occurred."));
    }
}
