package com.gamerytoffi.picpay.exception;

import com.gamerytoffi.picpay.domain.notification.NotificationSendingException;
import com.gamerytoffi.picpay.domain.transaction.TransactionNotAuthorizeException;
import com.gamerytoffi.picpay.domain.user.InsufficientFundsException;
import com.gamerytoffi.picpay.domain.user.MerchantTransactionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity duplicateEntry(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(new ExceptionDTO("user is already registered", "400"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFound(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity InsufficientMoney(InsufficientFundsException e) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(e.getMessage(), "500"));
    }

    @ExceptionHandler(MerchantTransactionException.class)
    public ResponseEntity merchantAttemptedTransaction(MerchantTransactionException e) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(e.getMessage(), "500"));
    }

    @ExceptionHandler(NotificationSendingException.class)
    public ResponseEntity failSendingNotification(NotificationSendingException e) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(e.getMessage(), "500"));
    }

    @ExceptionHandler(TransactionNotAuthorizeException.class)
    public ResponseEntity failTransaction(TransactionNotAuthorizeException e) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(e.getMessage(), "500"));
    }
}
