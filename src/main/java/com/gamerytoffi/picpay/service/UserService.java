package com.gamerytoffi.picpay.service;

import com.gamerytoffi.picpay.domain.User;
import com.gamerytoffi.picpay.domain.UserType;
import com.gamerytoffi.picpay.domain.dto.UserDTO;
import com.gamerytoffi.picpay.infra.exception.InsufficientFundsException;
import com.gamerytoffi.picpay.infra.exception.MerchantTransactionException;
import com.gamerytoffi.picpay.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT)
            throw new MerchantTransactionException("MERCAHNT type user cannot carry out transactions");
        if (sender.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("User does not have the balance to carry out this transaction");
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public User createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        this.saveUser(user);
        return user;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
