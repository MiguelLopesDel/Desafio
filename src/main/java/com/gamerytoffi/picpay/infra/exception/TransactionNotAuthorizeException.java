package com.gamerytoffi.picpay.infra.exception;

public class TransactionNotAuthorizeException extends Exception {
    public TransactionNotAuthorizeException(String transactionNotAuthorized) {
        super(transactionNotAuthorized);
    }
}
