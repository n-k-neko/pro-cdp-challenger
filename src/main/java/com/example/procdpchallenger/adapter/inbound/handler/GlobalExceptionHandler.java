package com.example.procdpchallenger.adapter.inbound.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.procdpchallenger.adapter.inbound.dto.ErrorResponse;
import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.infrastructure.exception.InfrastructureException;
import com.example.procdpchallenger.infrastructure.exception.InvalidCredentialsException;
import com.example.procdpchallenger.shared.exception.BaseException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainRuleViolationException ex) {
        logging(ex);
        return createErrorResponse(ex);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(BusinessRuleViolationException ex) {
        logging(ex);
        return createErrorResponse(ex);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(InfrastructureException ex) {
        logging(ex);
        return createErrorResponse(ex);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) 
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "AUTHENTICATION_ERROR",
            "Invalid credentials provided"  // クライアントに返すメッセージ
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * カスタム例外のログ出力
     * @param ex 例外
     */
    public void logging(BaseException ex) {
        if(ex.getErrorCategory() == ErrorCategory.ERROR) {
            logger.error("Exception occurred: ErrorCategory={}, ErrorCode={}, Message={}", ex.getErrorCategory(), ex.getErrorCode(), ex.getMessage(), ex);
        } else if(ex.getErrorCategory() == ErrorCategory.WARN) {
            logger.warn("Exception occurred: ErrorCategory={}, ErrorCode={}, Message={}", ex.getErrorCategory(), ex.getErrorCode(), ex.getMessage(), ex);
        } else {
            // ログに記録しない
        }
    }

    // TODO：一般例外のログ出力

    /**
     * エラーレスポンスの生成
     * エラーカテゴリーが「エラー」は想定外のエラーのため、500エラーとしてエラーメッセージを一般化して返す
     * エラーカテゴリーが「エラー」以外は、400エラーとしてエラーメッセージを返す
     * @param ex 例外
     * @return エラーレスポンス
     */
    public ResponseEntity<ErrorResponse> createErrorResponse(BaseException ex) {
        if(ex.getErrorCategory() == ErrorCategory.ERROR) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(ex.getErrorCode(), "An unexpected error occurred."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
        }
    }
}
