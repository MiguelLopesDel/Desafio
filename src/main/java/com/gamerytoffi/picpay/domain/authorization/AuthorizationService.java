package com.gamerytoffi.picpay.domain.authorization;

import com.gamerytoffi.picpay.domain.transaction.TransactionNotAuthorizeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AuthorizationService {

    private final RestClient restClient;
    private static final String URL = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";

    public AuthorizationService(RestClient.Builder restClient) {
        this.restClient = restClient.baseUrl(URL).build();
    }

    public void authorizeTransaction() {
        ResponseEntity<Map> entity;
        try {
            entity = restClient.get().retrieve().toEntity(Map.class);
        } catch (HttpServerErrorException e) {
            throw new TransactionNotAuthorizeException("Transaction not authorized");
        }
        if (!(entity.getStatusCode() == HttpStatus.OK && entity.getBody().get("message").equals("Autorizado")))
            throw new TransactionNotAuthorizeException("Transaction not authorized");
    }
}
