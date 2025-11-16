package com.example.notification.kafka;

import com.example.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaListenerService {

    private final EmailService emailService;

    public KafkaListenerService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Kafka listener, который слушает топик "user-events" и обрабатывает события создания и удаления пользователей.
     *
     * <p>Метод принимает сообщение в формате JSON, содержащем поля:
     * <ul>
     *     <li>operation — операция, которая произошла с пользователем ("CREATE" или "DELETE")</li>
     *     <li>email — email пользователя, на который нужно отправить уведомление</li>
     * </ul>
     * В зависимости от значения поля operation формируется текст письма и вызывается EmailService для отправки уведомления.
     * Сообщения с неизвестной операцией приводят к отправке письма с текстом "Неизвестная операция".
     *
     * @param message JSON-строка с информацией о событии пользователя
     */
    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listen(String message) {
        System.out.println("Kafka message received: " + message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(message);
            String email = node.get("email").asText();
            String operation = node.get("operation").asText();

            String text;
            if ("CREATE".equalsIgnoreCase(operation)) {
                text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
            } else if ("DELETE".equalsIgnoreCase(operation)) {
                text = "Здравствуйте! Ваш аккаунт был удалён.";
            } else {
                text = "Неизвестная операция.";
            }

            emailService.sendEmail(email, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}