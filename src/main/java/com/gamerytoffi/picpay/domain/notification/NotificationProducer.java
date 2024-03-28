package com.gamerytoffi.picpay.domain.notification;

import com.gamerytoffi.picpay.domain.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class NotificationProducer {
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public void sendNotification(Transaction transaction) {
        kafkaTemplate.send("transaction_notification", transaction);
    }
}
