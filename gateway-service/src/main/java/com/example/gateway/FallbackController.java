package com.example.gateway;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    /**
     * Фолбэк для User-service.
     * Этот эндпоинт вызывается, когда основной User-service недоступен.
     * Возвращаем пользователю информативное сообщение о временной недоступности сервиса.
     */
    @GetMapping("/userServiceFallback")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.ok("User-service временно недоступен.");
    }

    /**
     * Фолбэк для Notification-service.
     * Используется в случае недоступности Notification-service.
     * Сообщение информирует пользователя о временных неполадках и необходимости повторного запроса позже.
     */
    @GetMapping("/notificationFallback")
    public ResponseEntity<String> notificationFallback() {
        return ResponseEntity.ok("Notification-service лег. Мы работаем над этим.");
    }

    /**
     * Универсальный фолбэк для получения списка пользователей.
     * Этот метод вызывается при недоступности user-service.
     * Пользователю возвращается сообщение с рекомендацией повторить попытку позднее.
     */
    @GetMapping("/fallback/users")
    public ResponseEntity<String> fallbackUsers() {
        return ResponseEntity.ok("user-service не работает, попробуйте позже.");
    }
}