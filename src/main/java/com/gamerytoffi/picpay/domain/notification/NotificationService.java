package com.gamerytoffi.picpay.domain.notification;

import com.gamerytoffi.picpay.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void notify(Transaction transaction) throws NotificationSendingException {
        notificationProducer.sendNotification(transaction);
    }
}
