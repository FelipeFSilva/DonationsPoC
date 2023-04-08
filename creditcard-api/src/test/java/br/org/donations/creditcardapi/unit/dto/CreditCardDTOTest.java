package br.org.donations.creditcardapi.unit.dto;

import br.org.donations.creditcardapi.dto.DonationDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static br.org.donations.creditcardapi.utils.TestUtils.createDonationInputDTO;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CreditCardDTOTest {

    private static Validator validator;

    private DonationDTO donationDTO;

    @BeforeAll
    public static void setUpAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void setUp(){
        donationDTO = createDonationInputDTO();
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito nome do titular nulo.")
    public void creditCardHolderNullFieldTest(){
        donationDTO.getCreditCard().setHolder(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com nome do titular vazio.")
    public void creditCartHolderEmptyFieldTest(){
        donationDTO.getCreditCard().setHolder("");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com nome do titular maior que 40 dígitos.")
    public void creditCardHolderInvalidLongLengthFieldTest(){
        donationDTO.getCreditCard().setHolder("NomeNomeNomeNomeNomeNomeNomeNomeNomeNomeN");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com número nulo.")
    public void creditCardNumberNullFieldTest(){
        donationDTO.getCreditCard().setNumber(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com número vazio.")
    public void creditCardNumberEmptyFieldTest(){
        donationDTO.getCreditCard().setNumber("");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(2);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com número menor que 16 dígitos.")
    public void creditCardNumberInvalidShortLengthFieldTest(){
        donationDTO.getCreditCard().setNumber("555789219504666");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com número maior que 16 dígitos.")
    public void creditCardNumberInvalidLongLengthFieldTest(){
        donationDTO.getCreditCard().setNumber("55578921950466655");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito contendo letras no campo de número do cartão.")
    public void creditCardNumberInvalidFieldTest(){
        donationDTO.getCreditCard().setNumber("A557892195046665");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito validade nula.")
    public void creditCardValidityNullFieldTest(){
        donationDTO.getCreditCard().setValidity(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com código de segurança nulo.")
    public void creditCardSecurityCodeNullFieldTest(){
        donationDTO.getCreditCard().setSecurityCode(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com código de segurança vazio.")
    public void creditCardSecurityCodeEmptyFieldTest(){
        donationDTO.getCreditCard().setSecurityCode("");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(2);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com código de segurança menor que 3 dígitos.")
    public void creditCardSecurityCodeInvalidShortLengthFieldTest(){
        donationDTO.getCreditCard().setSecurityCode("12");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com código de segurança maior que 3 dígitos.")
    public void creditCardSecurityCodeInvalidLongLengthFieldTest(){
        donationDTO.getCreditCard().setSecurityCode("1234");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar cartão de crédito com código de segurança com letras.")
    public void creditCardSecurityCodeInvalidFieldTest(){
        donationDTO.getCreditCard().setSecurityCode("12a");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }
}
