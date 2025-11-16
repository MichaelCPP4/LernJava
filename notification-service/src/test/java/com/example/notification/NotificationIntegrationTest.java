package com.example.notification;

import com.example.notification.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "user-events" }, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class NotificationIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private EmailService emailService;

    @Test
    public void testCreateUserSendsEmail() throws InterruptedException {
        // Шлём событие CREATE
        String createMessage = "{ \"operation\": \"CREATE\", \"email\": \"ivan123@gmail.com\" }";
        kafkaTemplate.send("user-events", createMessage);

        Thread.sleep(2000);

        verify(emailService, times(1)).sendEmail("ivan123@gmail.com",
                "Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
    }

    @Test
    public void testDeleteUserSendsEmail() throws InterruptedException {
        // Шлём событие DELETE
        String deleteMessage = "{ \"operation\": \"DELETE\", \"email\": \"ivan123@gmail.com\" }";
        kafkaTemplate.send("user-events", deleteMessage);

        Thread.sleep(2000);

        verify(emailService, times(1)).sendEmail("ivan123@gmail.com",
                "Здравствуйте! Ваш аккаунт был удалён.");
    }
}
