package com.gamerytoffi.picpay.domain.user;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String string) {
        super(string);
    }
}
