package br.org.donations.creditcardapi.rabbitmq;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.exception.RabbitMQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CreditCardDonationSender")
@Service
public class CreditCardDonationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.credit-card}")
    private String creditCardTopicExchange;

    @Value("${app-config.rabbit.routingKey.credit-card}")
    private String creditCardKey;

    public void sendCreditCardDonationMessage(DonationDTO message){
        try {
            log.info("Enviando mensagem! Tópico: {}. Key: {}", creditCardTopicExchange, creditCardKey);
            rabbitTemplate.convertAndSend(creditCardTopicExchange, creditCardKey, message);
            log.info("Mensagem enviada com sucesso! Tópico: {}. Key: {}", creditCardTopicExchange, creditCardKey);
        }catch (AmqpConnectException ex){
            log.error("Erro ao tentar enviar mensagem de doação por cartão de crédito: ", ex);
            throw new RabbitMQException("Falha de conexão ao enviar mensagem para fila: " + ex);
        }
    }
}
