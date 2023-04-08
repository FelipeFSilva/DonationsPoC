package br.org.donations.creditcardapi.unit.service;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.dto.DonationToSaveDTO;
import br.org.donations.creditcardapi.dto.EmailDTO;
import br.org.donations.creditcardapi.feignclients.DonationsApiFeignClient;
import br.org.donations.creditcardapi.model.Donation;
import br.org.donations.creditcardapi.rabbitmq.CreditCardDonationSender;
import br.org.donations.creditcardapi.rabbitmq.EmailSender;
import br.org.donations.creditcardapi.repository.DonationRepository;
import br.org.donations.creditcardapi.service.CreditCardService;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.org.donations.creditcardapi.utils.TestUtils.createValidDonationEntity;
import static br.org.donations.creditcardapi.utils.TestUtils.createValidDonationToSaveDTO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CreditCardServiceTest {

    @InjectMocks
    private CreditCardService service;

    @Mock
    private CreditCardDonationSender creditCardDonationSender;
    @Mock
    private EmailSender emailSender;
    @Mock
    private DonationRepository donationRepository;
    @Mock
    private DonationsApiFeignClient donationsApiFeignClient;
    @MockBean
    private ModelMapper mapper;

    private Donation donation;
    private DonationToSaveDTO donationToSaveDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        donation = createValidDonationEntity();
        donationToSaveDTO = createValidDonationToSaveDTO();
    }

    @Test
    @DisplayName("Deve realizar chamada ao Sender do RabbitMQ para envio de doação")
    public void sendDonationTest() {
        DonationDTO donationDTO = DonationDTO.builder().build();
        Mockito.doNothing().when(creditCardDonationSender).sendCreditCardDonationMessage(donationDTO);

        service.sendDonation(donationDTO);
        Mockito.verify(creditCardDonationSender, times(1))
                .sendCreditCardDonationMessage(donationDTO);
    }

    @Test
    @DisplayName("Deve salvar uma doação com sucesso.")
    public void saveDonationTest() {
        DonationDTO donationDTO = DonationDTO.builder().build();
        Donation savedDonation = donation;
        savedDonation.setId(1L);

        Mockito.when(donationsApiFeignClient.validateCreditCardDonation(donationDTO))
                .thenReturn(donationToSaveDTO);
        Mockito.when(mapper.map(Mockito.any(), Mockito.any()))
                .thenReturn(donation);
        Mockito.when(donationRepository.save(donation))
                .thenReturn(savedDonation);
        Mockito.doNothing().when(emailSender).sendCreditCardEmailMessage(Mockito.any(EmailDTO.class));

        service.saveDonation(donationDTO);
        Mockito.verify(donationsApiFeignClient, times(1)).validateCreditCardDonation(Mockito.any(DonationDTO.class));
        Mockito.verify(donationRepository, times(1)).save(Mockito.any(Donation.class));
        Mockito.verify(emailSender, times(1)).sendCreditCardEmailMessage(Mockito.any(EmailDTO.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar uma doação.")
    public void saveDonationExceptionTest() {
        DonationDTO donationDTO = DonationDTO.builder().build();
        Mockito.when(donationsApiFeignClient.validateCreditCardDonation(donationDTO))
                .thenThrow(FeignException.class);

        Throwable throwable = Assertions.catchThrowable(() -> service.saveDonation(donationDTO));
        assertThat(throwable).isInstanceOf(FeignException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao chamar serviço de validação de doações.")
    public void saveDonationConstraintViolationExceptionTest() {
        DonationDTO donationDTO = DonationDTO.builder().build();
        Mockito.when(donationsApiFeignClient.validateCreditCardDonation(donationDTO))
                .thenThrow(ConstraintViolationException.class);

        Throwable throwable = Assertions.catchThrowable(() -> service.saveDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ConstraintViolationException.class);
    }

}
