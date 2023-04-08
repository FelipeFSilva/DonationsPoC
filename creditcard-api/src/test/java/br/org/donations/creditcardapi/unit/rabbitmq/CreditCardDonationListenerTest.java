package br.org.donations.creditcardapi.unit.rabbitmq;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.rabbitmq.CreditCardDonationListener;
import br.org.donations.creditcardapi.service.CreditCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.org.donations.creditcardapi.utils.TestUtils.createDonationInputDTO;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CreditCardDonationListenerTest {

    @Mock
    private CreditCardService creditCardService;
    @Mock
    private RetryTemplate retryTemplate;
    @InjectMocks
    private CreditCardDonationListener creditCardDonationListener;

    private DonationDTO donationDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        donationDTO = createDonationInputDTO();
    }

    @Test
    @DisplayName("Deve chamar o método de salvar doação ao realizar consumo de mensagem")
    public void receiveCreditCardDonationMessageTest(){
        Mockito.when(creditCardService.saveDonation(donationDTO)).thenReturn(1L);
        creditCardDonationListener.receiveCreditCardDonationMessage(donationDTO);
        verify(creditCardService, times(1)).saveDonation(donationDTO);
    }

    @Test
    @DisplayName("Deve chamar o método de salvar doação ao realizar retentativa de consumo de mensagem")
    public void retryReceiveCreditCardDonationMessageTest(){
        Mockito.when(creditCardService.saveDonation(donationDTO))
                .thenReturn(1L);
        Mockito.when(retryTemplate.execute(Mockito.any(RetryCallback.class), isNull(), isNull()))
                .thenAnswer(invocationOnMock -> creditCardService.saveDonation(donationDTO));

        creditCardDonationListener.retryReceiveCreditCardDonationMessage(donationDTO);
        verify(creditCardService, times(1)).saveDonation(donationDTO);
        verify(retryTemplate, times(1)).execute(Mockito.any(RetryCallback.class),isNull(), isNull());
    }

}
