package com.gamerytoffi.picpay.repository;

import com.gamerytoffi.picpay.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
