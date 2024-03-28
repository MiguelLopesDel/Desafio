package com.gamerytoffi.picpay.domain.transaction;

import com.gamerytoffi.picpay.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transactions")
@Table(name = "transactions")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @CreatedDate
    private LocalDateTime timeStamp;

    public Transaction(BigDecimal amount, User sender, User receiver) {
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
    }
}
