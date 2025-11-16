package com.example.notification.controller;

import com.example.notification.service.EmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public String sendMail(@RequestParam String email, @RequestParam String message) {
        emailService.sendEmail(email, message);
        return "Email отправлен";
    }
}
