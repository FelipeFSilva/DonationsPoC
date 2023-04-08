package br.org.donations.creditcardapi.unit.model;

import br.org.donations.creditcardapi.model.Donor;
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

import static br.org.donations.creditcardapi.utils.TestUtils.createValidDonorEntity;
import static br.org.donations.creditcardapi.utils.TestUtils.generateAnyStringWithLength;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class DonorTest {

    private static Validator validator;
    private Donor donor;

    @BeforeAll
    public static void setUpAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void setUp(){
        donor = createValidDonorEntity();
    }

    @Test
    @DisplayName("Deve criar uma entidade 'Donor' com todos campos válidos")
    public void donorValidFieldsTest(){
        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doador com campos nulos")
    public void donorNullFieldsTest(){

        Set<ConstraintViolation<Donor>> violations = validator.validate(Donor.builder().build());
        assertThat(violations).hasSize(4);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doador com campos vazios, 'type' menor que 2 caracteres e 'documentNumber' menor que 11 caracteres")
    public void donorEmptyFieldsTest(){

        donor.setName("");
        donor.setType("");
        donor.setDocumentNumber("");
        donor.setEmail("");

        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).hasSize(6);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doador com nome maior que 255 caracteres")
    public void donorNameInvalidLongLengthFieldTest(){

        donor.setName(generateAnyStringWithLength(256));
        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doador com email inválido")
    public void donorEmailInvalidFieldsTest(){

        donor.setEmail("testeemail.com");
        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doador com 'type' maior que 2 caracteres")
    public void donorTypeInvalidLongLengthFieldTest(){

        donor.setType(generateAnyStringWithLength(3));
        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doador com 'type' menor que 2 caracteres")
    public void donorTypeInvalidShortLengthFieldTest(){

        donor.setType("P");
        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com documento do doador maior que 14 caracteres.")
    public void donorDocumentNumberInvalidLongLengthFieldTest(){

        donor.setDocumentNumber("123456789123456");
        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com documento do doador menor que 11 caracteres.")
    public void donorDocumentNumberInvalidShortLengthFieldTest(){

        donor.setDocumentNumber("1234567891");
        Set<ConstraintViolation<Donor>> violations = validator.validate(donor);
        assertThat(violations).hasSize(1);
    }
}
