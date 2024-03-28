package com.gamerytoffi.picpay.domain.transaction;

public class TransactionNotAuthorizeException extends RuntimeException {
    public TransactionNotAuthorizeException(String transactionNotAuthorized) {
        super(transactionNotAuthorized);
    }
}
