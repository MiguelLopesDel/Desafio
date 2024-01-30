package com.gamerytoffi.picpay.controller;

import com.gamerytoffi.picpay.domain.Transaction;
import com.gamerytoffi.picpay.domain.dto.TransactionDTO;
import com.gamerytoffi.picpay.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception {
        return new ResponseEntity(transactionService.createTransaction(transactionDTO) ,HttpStatus.CREATED);
    }
}
