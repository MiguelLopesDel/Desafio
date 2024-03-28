package com.gamerytoffi.picpay.service;

import com.gamerytoffi.picpay.domain.authorization.AuthorizationService;
import com.gamerytoffi.picpay.domain.notification.NotificationService;
import com.gamerytoffi.picpay.domain.transaction.*;
import com.gamerytoffi.picpay.domain.user.User;
import com.gamerytoffi.picpay.domain.user.UserService;
import com.gamerytoffi.picpay.domain.user.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private NotificationService notificationService;
    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void inicializate() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should create transaction when everthing is ok")
    void createTransactionCase1() throws Exception {
        User sender = new User(1L, "test", "Silva", "32434343", "Elenaa@example.com", "senha123", BigDecimal.TEN, UserType.COMMON);
        User receiver = new User(2L, "test", "Silva", "32434343", "Elenaa@example.com", "senha123", BigDecimal.TEN, UserType.MERCHANT);
        when(userService.getUserById(1L)).thenReturn(sender);
        when(userService.getUserById(2L)).thenReturn(receiver);
        doNothing().when(authorizationService).authorizeTransaction();
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.TEN, 1L, 2L);
        Transaction transaction = transactionService.create(transactionDTO);
        verify(transactionRepository, times(1)).save(any());
        sender.setBalance(BigDecimal.ZERO);
        verify(userService, times(1)).saveUser(sender);
        receiver.setBalance(BigDecimal.valueOf(20));
        verify(userService, times(1)).saveUser(receiver);
        verify(notificationService, times(1)).notify(transaction);
        verify(notificationService, times(1)).notify(transaction);
    }

    @Test
    @DisplayName("should throw TransactionNotAuthorizeException when transaction not authorized")
    void createTransactionCase2() throws Exception {
        User sender = new User(1L, "test", "Silva", "32434343", "Elenaa@example.com", "senha123", BigDecimal.TEN, UserType.COMMON);
        User receiver = new User(2L, "test", "Silva", "32434343", "Elenaa@example.com", "senha123", BigDecimal.TEN, UserType.MERCHANT);
        when(userService.getUserById(1L)).thenReturn(sender);
        when(userService.getUserById(2L)).thenReturn(receiver);
        doThrow(new TransactionNotAuthorizeException("Transaction not authorized")).when(authorizationService).authorizeTransaction();
        Exception thrown = Assertions.assertThrows(TransactionNotAuthorizeException.class, () -> {
            TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.TEN, 1L, 2L);
            transactionService.create(transactionDTO);
        });

        Assertions.assertEquals("Transaction not authorized", thrown.getMessage());
    }
}