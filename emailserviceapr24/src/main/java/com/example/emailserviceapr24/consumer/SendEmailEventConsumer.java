package com.example.emailserviceapr24.consumer;

import com.example.emailserviceapr24.dtos.SendEmailEventDto;
import com.example.emailserviceapr24.utils.EmailUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailEventConsumer {
    private ObjectMapper objectMapper;

    public SendEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void listenSendEmailEvent(String message) {
        try {
            System.out.println("Received message: " + message);
            SendEmailEventDto sendEmailEvent = objectMapper.readValue(message, SendEmailEventDto.class);

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("testkapil29@gmail.com", "kapilgarg@29");
                }
            };

            Session session = Session.getInstance(properties, auth);

            EmailUtil.sendEmail(session, sendEmailEvent.getTo(), sendEmailEvent.getSubject(), sendEmailEvent.getBody());

            // Process the SendEmailEvent and send the email
            // ...
        } catch (Exception e) {
            // Handle any exceptions that occur during the deserialization or processing
            // ...
            System.out.println(e.getMessage());
        }
    }
}
