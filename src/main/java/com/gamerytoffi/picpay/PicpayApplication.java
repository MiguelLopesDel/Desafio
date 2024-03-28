package com.gamerytoffi.picpay;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableJpaAuditing
public class PicpayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PicpayApplication.class, args);
    }

    @Bean
    NewTopic notificationTopic() {
        return TopicBuilder.name("transaction_notification").build();
    }
}
