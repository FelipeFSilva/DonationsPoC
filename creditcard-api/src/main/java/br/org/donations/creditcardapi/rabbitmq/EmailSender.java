package br.org.donations.creditcardapi.rabbitmq;

import br.org.donations.creditcardapi.dto.EmailDTO;
import br.org.donations.creditcardapi.exception.RabbitMQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j(topic = "EmailSender")
@Service
public class EmailSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.credit-card}")
    private String creditCardTopicExchange;

    @Value("${app-config.rabbit.routingKey.credit-card-email}")
    private String emailRoutingKey;

    public void sendCreditCardEmailMessage(EmailDTO message){
        try {
            log.info("Enviando mensagem para fila de e-mail! Tópico: {}. Key: {}", creditCardTopicExchange, emailRoutingKey);
            rabbitTemplate.convertAndSend(creditCardTopicExchange, emailRoutingKey, message);
            log.info("Mensagem enviada com sucesso! Tópico: {}. Key: {}", creditCardTopicExchange, emailRoutingKey);
        }catch (AmqpConnectException ex){
            log.error("Erro ao tentar enviar mensagem para fila de e-mail: ", ex);
            throw new RabbitMQException("Falha de conexão ao enviar mensagem para fila: " + ex);
        }
    }
}
