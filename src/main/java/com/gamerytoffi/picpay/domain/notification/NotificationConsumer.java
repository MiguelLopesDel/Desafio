package com.gamerytoffi.picpay.domain.notification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
class NotificationConsumer {
    private final RestClient restClient;

    private static final String url = "https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6";

    public NotificationConsumer(RestClient.Builder restClient) {
        this.restClient = restClient.baseUrl(url).build();
    }

    @KafkaListener(topics = "transaction_notification", groupId = "picpay")
    public void receiveNotification() {
        ResponseEntity<Notification> entity = restClient.get().retrieve().toEntity(Notification.class);
        if (!(entity.getStatusCode() == HttpStatus.OK || entity.getBody().message()))
            throw new NotificationSendingException("Unable to send notification. Try later");
    }
}
