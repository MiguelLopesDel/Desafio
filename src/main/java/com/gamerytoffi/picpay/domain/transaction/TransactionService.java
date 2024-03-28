package com.gamerytoffi.picpay.domain.transaction;

import com.gamerytoffi.picpay.domain.authorization.AuthorizationService;
import com.gamerytoffi.picpay.domain.notification.NotificationService;
import com.gamerytoffi.picpay.domain.user.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final AuthorizationService authorizationService;


    @Transactional
    public Transaction create(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.getUserById(transactionDTO.senderId());
        User receiver = this.userService.getUserById(transactionDTO.receiverId());
        BigDecimal amountTransaction = transactionDTO.amount();

        validateTransaction(sender, receiver, amountTransaction);
        authorizationService.authorizeTransaction();
        Transaction transaction = new Transaction(amountTransaction, sender, receiver);

        sender.debit(amountTransaction);
        receiver.credit(amountTransaction);
        transactionRepository.save(transaction);

        saveUsers(sender, receiver);
        notificationService.notify(transaction);
        return transaction;
    }

    private void validateTransaction(User sender, User receiver, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT)
            throw new MerchantTransactionException("MERCAHNT type user cannot carry out transactions");
        else if (sender.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("User does not have the balance to carry out this transaction");
        else if (Objects.equals(sender.getId(), receiver.getId()))
            throw new RuntimeException("Sender and Receiver cannot be the same");
    }

    private void saveUsers(User... users) {
        for (User user : users) this.userService.saveUser(user);
    }
}
