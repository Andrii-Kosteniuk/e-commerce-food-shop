package com.ecommerce.notification.mail;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService  {

    @Value("${notification.from-email}")
    private String fromEmail;

    private final MailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email was sent to user with email: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to: {}, error: {}", toEmail, e.getMessage());
        }

    }


}
