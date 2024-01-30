package com.gamerytoffi.picpay.service;

import com.gamerytoffi.picpay.domain.User;
import com.gamerytoffi.picpay.domain.dto.NotificationDTO;
import com.gamerytoffi.picpay.infra.exception.NotificationSendingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate;
    private static final String url = "https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6";

    public void sendNotification(User user, String message) throws NotificationSendingException {
        NotificationDTO notificationRequest = new NotificationDTO(user.getEmail(), message);
        ResponseEntity<String> response = restTemplate.postForEntity(url, notificationRequest, String.class);
        if (!(response.getStatusCode() == HttpStatus.OK && response.getBody().contains("true")))
            throw new NotificationSendingException("Unable to send notification. Try later");
    }
}
