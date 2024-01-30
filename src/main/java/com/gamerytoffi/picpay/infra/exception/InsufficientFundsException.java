package com.gamerytoffi.picpay.infra.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String string) {
        super(string);
    }
}
