package com.ecommerce.infra.email;



import com.ecommerce.exceptions.EventInternalServerErrorException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailSenderDTO email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@e-commerce.com");
            message.setTo(email.to());
            message.setSubject(email.subject());
            message.setText(email.message());

            mailSender.send(message);

        } catch (Exception ex) {
            throw new EventInternalServerErrorException(ex.getMessage());

        }
    }
}