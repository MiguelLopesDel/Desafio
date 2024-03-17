package com.gamerytoffi.picpay.service;

import com.gamerytoffi.picpay.domain.User;
import com.gamerytoffi.picpay.domain.UserType;
import com.gamerytoffi.picpay.domain.dto.TransactionDTO;
import com.gamerytoffi.picpay.infra.exception.TransactionNotAuthorizeException;
import com.gamerytoffi.picpay.repository.TransactionRepository;
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
        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction()).thenReturn(true);
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.TEN, 1L, 2L);
        transactionService.createTransaction(transactionDTO);
        verify(transactionRepository, times(1)).save(any());
        sender.setBalance(BigDecimal.ZERO);
        verify(userService, times(1)).saveUser(sender);
        receiver.setBalance(BigDecimal.valueOf(20));
        verify(userService, times(1)).saveUser(receiver);
        verify(notificationService, times(1)).sendNotification(sender, "successul transaction");
        verify(notificationService, times(1)).sendNotification(receiver, "received transaction");
    }

    @Test
    @DisplayName("should throw TransactionNotAuthorizeException when transaction not authorized")
    void createTransactionCase2() throws Exception {
        User sender = new User(1L, "test", "Silva", "32434343", "Elenaa@example.com", "senha123", BigDecimal.TEN, UserType.COMMON);
        User receiver = new User(2L, "test", "Silva", "32434343", "Elenaa@example.com", "senha123", BigDecimal.TEN, UserType.MERCHANT);
        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(authorizationService.authorizeTransaction()).thenReturn(false);

        Exception thrown = Assertions.assertThrows(TransactionNotAuthorizeException.class, () -> {
            TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.TEN, 1L, 2L);
            transactionService.createTransaction(transactionDTO);
        });

        Assertions.assertEquals("Transaction not authorized", thrown.getMessage());
    }
}