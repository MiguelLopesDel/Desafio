package com.gamerytoffi.picpay.service;

import com.gamerytoffi.picpay.domain.Transaction;
import com.gamerytoffi.picpay.domain.User;
import com.gamerytoffi.picpay.domain.dto.TransactionDTO;
import com.gamerytoffi.picpay.infra.exception.TransactionNotAuthorizeException;
import com.gamerytoffi.picpay.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionService {

    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        BigDecimal amountTransaction = transactionDTO.amount();
        userService.validateTransaction(sender, amountTransaction);
        boolean isAuthorized = authorizationService.authorizeTransaction();
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
}
