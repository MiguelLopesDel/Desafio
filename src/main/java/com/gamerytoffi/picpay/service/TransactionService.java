package com.gamerytoffi.picpay.service;

import com.gamerytoffi.picpay.domain.Transaction;
import com.gamerytoffi.picpay.domain.User;
import com.gamerytoffi.picpay.domain.dto.TransactionDTO;
import com.gamerytoffi.picpay.infra.exception.NotificationSendingException;
import com.gamerytoffi.picpay.infra.exception.TransactionNotAuthorizeException;
import com.gamerytoffi.picpay.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionService {

    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;
    private final NotificationService notificationService;
    private final static String url = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        BigDecimal amountTransaction = transactionDTO.amount();
        userService.validateTransaction(sender, amountTransaction);
        boolean isAuthorized = authorizeTransaction(sender, amountTransaction);
        if (!isAuthorized)
            throw new TransactionNotAuthorizeException("Transaction not authorized");

        Transaction transaction = new Transaction();
        transaction.setAmount(amountTransaction);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimeStamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(amountTransaction));
        receiver.setBalance(receiver.getBalance().add(amountTransaction));

        this.transactionRepository.save(transaction);
        saveUsers(sender, receiver);
        notificationService.sendNotification(sender, "successul transaction");
        notificationService.sendNotification(receiver, "received transaction");
        return transaction;
    }

    private void saveUsers(User... users) {
        for (User user : users)
            this.userService.saveUser(user);
    }

    public boolean authorizeTransaction(User sender, BigDecimal amount) {
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getStatusCode() == HttpStatus.OK && response.getBody().get("message").equals("Autorizado") ? true : false;
    }
}
