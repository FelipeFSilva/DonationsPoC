package br.org.donations.billetapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

//    @Value("${spring.mail.username}")
//    private String from;

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendEmailToDonor(String emailDonor) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ffernandes286@gmail.com");
        message.setTo(emailDonor);
        message.setSubject("Teste");
        message.setText("Teste");
        emailSender.send(message);
    }
}
