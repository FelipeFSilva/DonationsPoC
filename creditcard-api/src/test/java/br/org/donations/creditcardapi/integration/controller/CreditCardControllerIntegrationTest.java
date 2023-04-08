package br.org.donations.creditcardapi.integration.controller;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.dto.EmailDTO;
import br.org.donations.creditcardapi.dto.LoginRequest;
import br.org.donations.creditcardapi.exception.AuthorizationException;
import br.org.donations.creditcardapi.feignclients.DonationsApiFeignClient;
import br.org.donations.creditcardapi.integration.IntegrationTest;
import br.org.donations.creditcardapi.rabbitmq.CreditCardDonationListener;
import br.org.donations.creditcardapi.rabbitmq.CreditCardDonationSender;
import br.org.donations.creditcardapi.rabbitmq.EmailSender;
import br.org.donations.creditcardapi.repository.DonationRepository;
import br.org.donations.creditcardapi.service.CreditCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static br.org.donations.creditcardapi.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class CreditCardControllerIntegrationTest {

    private static final String CREDIT_CARD_DONATION_QUEUE = "credit-card-donation.queue";
    private static final String CREDIT_CARD_DONATION_RETRY_QUEUE = "credit-card-donation-retry.queue";
    private static final String CREDIT_CARD_DONATION_DLQ_QUEUE = "credit-card-donation-dlq.queue";
    private static final String CREDIT_CARD_EMAIL_QUEUE = "credit-card-email.queue";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private DonationsApiFeignClient donationsApiFeignClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CreditCardDonationSender creditCardDonationSender;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private CreditCardDonationListener creditCardDonationListener;

    private String token;


    @BeforeEach
    public void setUp() throws Exception {
        if (rabbitAdmin != null) {
            rabbitAdmin.purgeQueue(CREDIT_CARD_DONATION_QUEUE, true);
            rabbitAdmin.purgeQueue(CREDIT_CARD_DONATION_RETRY_QUEUE, true);
            rabbitAdmin.purgeQueue(CREDIT_CARD_DONATION_DLQ_QUEUE, true);
            rabbitAdmin.purgeQueue(CREDIT_CARD_EMAIL_QUEUE, true);
        }
        token = generateToken();
    }

    private String generateToken() throws Exception {
        LoginRequest validLoginRequest = createValidLoginRequest();
        String json = getTokenRequestJson(validLoginRequest);
        return mvc.perform(getTokenRequestPost(json))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @AfterEach
    public void tearDown() {
        if (rabbitAdmin != null) {
            rabbitAdmin.purgeQueue(CREDIT_CARD_DONATION_QUEUE, true);
            rabbitAdmin.purgeQueue(CREDIT_CARD_DONATION_RETRY_QUEUE, true);
            rabbitAdmin.purgeQueue(CREDIT_CARD_DONATION_DLQ_QUEUE, true);
            rabbitAdmin.purgeQueue(CREDIT_CARD_EMAIL_QUEUE, true);
        }
        if(donationRepository != null)
            donationRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve enviar uma doação por cartão de crédito com sucesso e verificar se encaminhou mensagem para fila de e-mail.")
    public void sendCreditCardDonationTest() throws Exception {
        DonationDTO donationDTO = createDonationInputDTO();
        String json = objectMapper.writeValueAsString(donationDTO);

        Mockito.when(donationsApiFeignClient.validateCreditCardDonation(Mockito.any(DonationDTO.class)))
                .thenReturn(createValidDonationToSaveDTO());

        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Doação enviada com sucesso!"));

        Thread.sleep(2000);
        assertThat(donationRepository.findById(1L)).isPresent();
        Assertions.assertFalse(donationRepository.findById(2L).isPresent());
        EmailDTO emailMessage = (EmailDTO) template.receiveAndConvert(CREDIT_CARD_EMAIL_QUEUE);
        assertThat(emailMessage).isNotNull();
        assertThat(emailMessage.getStatusDonation()).isEqualTo(APPROVED);
    }

    @Test
    @DisplayName("Deve tentar reenviar a mensagem com doação por cartão de crédito 3 vezes e encaminhar para fila DLQ ao final.")
    public void sendCreditCardDonationRetryTest() throws Exception {
        DonationDTO donationDTO = createDonationInputDTO();
        String json = objectMapper.writeValueAsString(donationDTO);

        Mockito.when(donationsApiFeignClient.validateCreditCardDonation(donationDTO))
                .thenThrow(FeignException.class);

        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, token);

        mvc.perform(request);
        Thread.sleep(5000);

        Assertions.assertFalse(donationRepository.findById(1L).isPresent());
        Mockito.verify(donationsApiFeignClient, Mockito.times(4))
                .validateCreditCardDonation(Mockito.any(DonationDTO.class));

        DonationDTO messageDonation = (DonationDTO) template.receiveAndConvert(CREDIT_CARD_DONATION_DLQ_QUEUE);
        assertThat(messageDonation).isNotNull();
        assertThat(messageDonation.getDonor().getName()).isEqualTo(donationDTO.getDonor().getName());
    }

    @Test
    @DisplayName("Deve enviar uma doação com token inválido")
    public void sendCreditCardDonationInvalidTokenTest() throws Exception {
        DonationDTO donationDTO = createDonationInputDTO();
        String json = objectMapper.writeValueAsString(donationDTO);

        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, token + "a");
        Throwable throwable = org.assertj.core.api.Assertions.catchThrowable(() -> mvc.perform(request));
        AssertionsForClassTypes.assertThat(throwable).isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("Deve enviar uma doação com token expirado")
    public void sendCreditCardDonationExpiredTokenTest() throws Exception {

        DonationDTO donationDTO = createDonationInputDTO();
        String json = objectMapper.writeValueAsString(donationDTO);

        MockHttpServletRequestBuilder request = getSendDonationPostRequestWithToken(json, EXPIRED_TOKEN);
        mvc.perform(request)
                .andExpect(status().isForbidden());

    }
}
