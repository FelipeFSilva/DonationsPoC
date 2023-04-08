package br.org.donations.creditcardapi.unit.rabbitmq;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.exception.RabbitMQException;
import br.org.donations.creditcardapi.rabbitmq.CreditCardDonationSender;
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

import static br.org.donations.creditcardapi.utils.TestUtils.createDonationInputDTO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CreditCardDonationSenderTest {

    @InjectMocks
    private CreditCardDonationSender creditCardDonationSender;

    @Mock
    private RabbitTemplate template;

    private DonationDTO donationDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        donationDTO = createDonationInputDTO();
        ReflectionTestUtils.setField(creditCardDonationSender, "creditCardTopicExchange", "");
        ReflectionTestUtils.setField(creditCardDonationSender, "creditCardKey", "");
    }

    @Test
    @DisplayName("Deve chamar 1 vez o método que envia doação para fila do RabbitMQ")
    public void sendCreditCardDonationTest() throws Exception {

        Mockito.doNothing().when(template).convertAndSend(Mockito.anyString(), Mockito.anyString(), Mockito.any(DonationDTO.class));
        creditCardDonationSender.sendCreditCardDonationMessage(donationDTO);
        Mockito.verify(template, times(1)).convertAndSend("","",donationDTO);

    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro de conexão com fila do RabbitMQ")
    public void sendCreditCardDonationExceptionTest() throws Exception {

        Mockito.doThrow(AmqpConnectException.class).when(template).convertAndSend(Mockito.anyString(), Mockito.anyString(), Mockito.any(DonationDTO.class));
        Throwable throwable = Assertions.catchThrowable(() -> creditCardDonationSender.sendCreditCardDonationMessage(donationDTO));
        assertThat(throwable).isInstanceOf(RabbitMQException.class);

    }
}
