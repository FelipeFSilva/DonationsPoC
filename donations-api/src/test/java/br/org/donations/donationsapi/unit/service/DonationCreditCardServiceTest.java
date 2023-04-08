package br.org.donations.donationsapi.unit.service;

import br.org.donations.donationsapi.dto.DonationCreditCardDTO;
import br.org.donations.donationsapi.dto.DonationResponse;
import br.org.donations.donationsapi.dto.PaymentCreditCardDTO;
import br.org.donations.donationsapi.enums.CreditCardStatusEnum;
import br.org.donations.donationsapi.enums.DonationStatusEnum;
import br.org.donations.donationsapi.enums.TypePersonEnum;
import br.org.donations.donationsapi.exception.ValidationException;
import br.org.donations.donationsapi.feignclients.PaymentGatewayFeignClient;
import br.org.donations.donationsapi.service.DonationCreditCardService;
import feign.RetryableException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.org.donations.donationsapi.utils.TestUtils.createValidDonationDTO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class DonationCreditCardServiceTest {

    @InjectMocks
    private DonationCreditCardService donationCreditCardService;
    @Mock
    private PaymentGatewayFeignClient paymentGatewayFeignClient;

    private DonationCreditCardDTO donationDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        donationDTO = createValidDonationDTO();
    }

    @Test
    @DisplayName("Deve retornar uma doação aprovada.")
    public void validateDonationApprovedTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.VALID.getStatus());

        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationDTO);

        assertThat(donationResponse).isNotNull();
        assertThat(donationResponse.getStatus()).isEqualTo(DonationStatusEnum.APPROVED.toString());
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão expirado.")
    public void validateDonationWithCardValidityExpiredTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.EXPIRED.getStatus());

        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationDTO);

        assertThat(donationResponse).isNotNull();
        assertThat(donationResponse.getStatus()).isEqualTo(DonationStatusEnum.REJECTED.toString());
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão com bandeira recusada.")
    public void validateDonationWithCardFlagDeclinedTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.FLAG_DECLINED.getStatus());

        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationDTO);

        assertThat(donationResponse).isNotNull();
        assertThat(donationResponse.getStatus()).isEqualTo(DonationStatusEnum.REJECTED.toString());
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão com número inválido.")
    public void validateDonationWithCardNumberInvalidTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.INVALID_NUMBER.getStatus());

        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationDTO);

        assertThat(donationResponse).isNotNull();
        assertThat(donationResponse.getStatus()).isEqualTo(DonationStatusEnum.REJECTED.toString());
    }

    @Test
    @DisplayName("Deve retornar uma doação rejeitada quando cartão sem limite.")
    public void validateCreditCardDonationWithCardNoLimitTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.NO_LIMIT.getStatus());

        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationDTO);

        assertThat(donationResponse).isNotNull();
        assertThat(donationResponse.getStatus()).isEqualTo(DonationStatusEnum.REJECTED.toString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não for possível estabelecer a conexão com o gateway de pagamentos.")
    public void validateDonationRetryableExceptionTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenThrow(RetryableException.class);

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(RetryableException.class);
    }

    @Test
    @DisplayName("Deve realizar as validações para doação de cartão de crédito por pessoa física.")
    public void validateDonationPFTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.VALID.getStatus());
        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationDTO);

        assertThat(donationResponse).isNotNull();
        assertThat(donationResponse.getDonor().getType()).isEqualTo(TypePersonEnum.PF.toString());
    }

    @Test
    @DisplayName("Deve realizar as validações para doação de cartão de crédito por pessoa jurídica.")
    public void validateDonationPJTest(){
        Mockito.when(paymentGatewayFeignClient.paymentCreditCard(Mockito.any(PaymentCreditCardDTO.class)))
                .thenReturn(CreditCardStatusEnum.VALID.getStatus());
        donationDTO.getDonor().setDocumentNumber("12345678910111");

        DonationResponse donationResponse = donationCreditCardService.validateDonation(donationDTO);

        assertThat(donationResponse).isNotNull();
        assertThat(donationResponse.getDonor().getType()).isEqualTo(TypePersonEnum.PJ.toString());
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 0.")
    public void validateDonationWithCPFOnlyZeroTest(){
        donationDTO.getDonor().setDocumentNumber("00000000000");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 1.")
    public void validateDonationWithCPFOnlyOneTest(){
        donationDTO.getDonor().setDocumentNumber("11111111111");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 2.")
    public void validateDonationWithCPFOnlyTwoTest(){
        donationDTO.getDonor().setDocumentNumber("22222222222");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 3.")
    public void validateDonationWithCPFOnlyThreeTest(){
        donationDTO.getDonor().setDocumentNumber("33333333333");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 4.")
    public void validateDonationWithCPFOnlyFourTest(){
        donationDTO.getDonor().setDocumentNumber("44444444444");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 5.")
    public void validateDonationWithCPFOnlyFiveTest(){
        donationDTO.getDonor().setDocumentNumber("55555555555");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 6.")
    public void validateDonationWithCPFOnlySixTest(){
        donationDTO.getDonor().setDocumentNumber("66666666666");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 7.")
    public void validateDonationWithCPFOnlySevenTest(){
        donationDTO.getDonor().setDocumentNumber("77777777777");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 8.")
    public void validateDonationWithCPFOnlyEightTest(){
        donationDTO.getDonor().setDocumentNumber("88888888888");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CPF somente 9.")
    public void validateDonationWithCPFOnlyNineTest(){
        donationDTO.getDonor().setDocumentNumber("99999999999");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CPF informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 0.")
    public void validateDonationWithCNPJOnlyZeroTest(){
        donationDTO.getDonor().setDocumentNumber("00000000000000");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 1.")
    public void validateDonationWithCNPJOnlyOneTest(){
        donationDTO.getDonor().setDocumentNumber("11111111111111");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 2.")
    public void validateDonationWithCNPJOnlyTwoTest(){
        donationDTO.getDonor().setDocumentNumber("22222222222222");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 3.")
    public void validateDonationWithCNPJOnlyThreeTest(){
        donationDTO.getDonor().setDocumentNumber("33333333333333");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 4.")
    public void validateDonationWithCNPJOnlyFourTest(){
        donationDTO.getDonor().setDocumentNumber("44444444444444");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 5.")
    public void validateDonationWithCNPJOnlyFiveTest(){
        donationDTO.getDonor().setDocumentNumber("55555555555555");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 6.")
    public void validateDonationWithCNPJOnlySixTest(){
        donationDTO.getDonor().setDocumentNumber("66666666666666");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 7.")
    public void validateDonationWithCNPJOnlySevenTest(){
        donationDTO.getDonor().setDocumentNumber("77777777777777");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 8.")
    public void validateDonationWithCNPJOnlyEightTest(){
        donationDTO.getDonor().setDocumentNumber("88888888888888");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com CNPJ somente 9.")
    public void validateDonationWithCNPJOnlyNineTest(){
        donationDTO.getDonor().setDocumentNumber("99999999999999");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O CNPJ informado é inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao realizar as validações para doação com número de documento com tamanho inválido.")
    public void validateDonationWithInvalidDocumentNumberLengthTest(){
        donationDTO.getDonor().setDocumentNumber("123456789112");

        Throwable throwable = Assertions.catchThrowable(() -> donationCreditCardService.validateDonation(donationDTO));
        assertThat(throwable).isInstanceOf(ValidationException.class);
        assertThat(throwable.getMessage()).isEqualTo("O documento de doador informado possui tamanho inválido.");
    }
}
