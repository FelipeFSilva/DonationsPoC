package br.org.donations.creditcardapi.unit.rabbitmq;

import br.org.donations.creditcardapi.dto.EmailDTO;
import br.org.donations.creditcardapi.exception.RabbitMQException;
import br.org.donations.creditcardapi.rabbitmq.EmailSender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static br.org.donations.creditcardapi.utils.TestUtils.createValidEmailDTO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EmailSenderTest {

    @InjectMocks
    private EmailSender emailSender;

    @Mock
    private RabbitTemplate template;

    private EmailDTO emailDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailDTO = createValidEmailDTO();
        ReflectionTestUtils.setField(emailSender, "creditCardTopicExchange", "");
        ReflectionTestUtils.setField(emailSender, "emailRoutingKey", "");
    }

    @Test
    @DisplayName("Deve chamar 1 vez o método que envia mensagem para fila de emails do RabbitMQ")
    public void sendMessageToEmailQueueTest() throws Exception {

        Mockito.doNothing().when(template).convertAndSend(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmailDTO.class));
        emailSender.sendCreditCardEmailMessage(emailDTO);
        Mockito.verify(template, times(1)).convertAndSend("","", emailDTO);

    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro de conexão com fila de envio de e-mails do RabbitMQ")
    public void sendMessageToEmailQueueExceptionTest() throws Exception {

        Mockito.doThrow(AmqpConnectException.class).when(template).convertAndSend(Mockito.anyString(), Mockito.anyString(), Mockito.any(EmailDTO.class));
        Throwable throwable = Assertions.catchThrowable(() -> emailSender.sendCreditCardEmailMessage(emailDTO));
        assertThat(throwable).isInstanceOf(RabbitMQException.class);

    }
}
