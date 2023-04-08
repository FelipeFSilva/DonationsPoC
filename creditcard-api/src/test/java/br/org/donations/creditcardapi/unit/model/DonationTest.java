package br.org.donations.creditcardapi.unit.model;

import br.org.donations.creditcardapi.model.Donation;
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

import static br.org.donations.creditcardapi.utils.TestUtils.createValidDonationEntity;
import static br.org.donations.creditcardapi.utils.TestUtils.generateAnyStringWithLength;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class DonationTest {

    private static Validator validator;
    private Donation donation;

    @BeforeAll
    public static void setUpAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void setUp(){
        donation = createValidDonationEntity();
    }

    @Test
    @DisplayName("Deve criar uma entidade 'Donation' com todos campos válidos")
    public void donationValidFieldsTest(){

        Set<ConstraintViolation<Donation>> violations = validator.validate(donation);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com campos nulos")
    public void donationWithNullFieldsTest(){

        Set<ConstraintViolation<Donation>> violations = validator.validate(Donation.builder().build());
        assertThat(violations).hasSize(5);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com status vazio")
    public void donationWithEmptyStatusTest(){

        donation.setStatus("");
        Set<ConstraintViolation<Donation>> violations = validator.validate(donation);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com status maior que 8 caracteres")
    public void donationStatusInvalidLongLengthTest(){

        donation.setStatus(generateAnyStringWithLength(9));
        Set<ConstraintViolation<Donation>> violations = validator.validate(donation);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com valor negativo")
    public void donationWithNegativeValuePaidTest(){

        donation.setValuePaid(BigDecimal.valueOf(-100.00));
        Set<ConstraintViolation<Donation>> violations = validator.validate(donation);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com valor igual a zero")
    public void donationWithZeroValuePaidTest(){

        donation.setValuePaid(BigDecimal.valueOf(0));
        Set<ConstraintViolation<Donation>> violations = validator.validate(donation);
        assertThat(violations).hasSize(1);
    }

}
