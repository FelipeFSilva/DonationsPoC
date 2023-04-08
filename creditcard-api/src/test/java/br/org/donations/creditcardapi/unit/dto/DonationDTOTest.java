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

import java.math.BigDecimal;
import java.util.Set;

import static br.org.donations.creditcardapi.utils.TestUtils.createDonationInputDTO;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class DonationDTOTest {

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
    @DisplayName("Deve enviar uma doação com todos campos válidos")
    public void donationValidFieldsTest(){
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação sem informações de doador.")
    public void donationWithoutDonorTest(){
        donationDTO.setDonor(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação sem informações de cartão de crédito.")
    public void donationWithoutCreditCardTest(){
        donationDTO.setCreditCard(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com campo de valor nulo.")
    public void donationValueNullFieldTest(){
        donationDTO.setValue(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com valor igual a zero.")
    public void donationValueZeroTest(){
        donationDTO.setValue(BigDecimal.ZERO);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com valor negativo.")
    public void donationValueNegativeTest(){
        donationDTO.setValue(BigDecimal.valueOf(-123));
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }
}
