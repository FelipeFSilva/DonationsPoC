package br.org.donations.billetapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Slf4j(topic = "EmailSenderService")
@Service
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendEmailToDonor(String emailDonor) {
        log.info("Iniciando o envio de email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailDonor);
        message.setSubject("Obrigado pela sua Doação");
        message.setText("Obrigado pela sua doação");
        emailSender.send(message);
        log.info("Finalizando o envio de email");
    }
}
