package com.gamerytoffi.picpay.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthorizationService {

    private final RestTemplate restTemplate;
    private final static String url = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";


    public boolean authorizeTransaction() {
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getStatusCode() == HttpStatus.OK && response.getBody().get("message").equals("Autorizado");
    }
}
