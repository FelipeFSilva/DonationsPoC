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
public class DonorDTOTest {

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
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com nome de doador vazio.")
    public void donorNameEmptyFieldTest(){
        donationDTO.getDonor().setName("");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com nome do doador nulo.")
    public void donorNameNullFieldTest(){
        donationDTO.getDonor().setName(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com documento do doador vazio.")
    public void donorDocumentEmptyFieldTest(){
        donationDTO.getDonor().setDocumentNumber("");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(2);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com documento do doador nulo.")
    public void donorDocumentNullFieldTest(){
        donationDTO.getDonor().setDocumentNumber(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com documento do doador maior que 14 caracteres.")
    public void donorDocumentInvalidLongLengthFieldTest(){
        donationDTO.getDonor().setDocumentNumber("123456789123456");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com documento do doador menor que 11 caracteres.")
    public void donorDocumentInvalidShortLengthFieldTest(){
        donationDTO.getDonor().setDocumentNumber("1234567890");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com documento do doador com letras.")
    public void donorDocumentInvalidFieldTest(){
        donationDTO.getDonor().setDocumentNumber("A1234567891");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com email do doador vazio.")
    public void donorEmailEmptyFieldTest(){
        donationDTO.getDonor().setEmail("");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com email do doador nulo.")
    public void donorEmailNullFieldTest(){
        donationDTO.getDonor().setEmail(null);
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Deve ocorrer erro de validação ao enviar doação com email do doador inválido.")
    public void donorEmailInvalidFieldTest(){
        donationDTO.getDonor().setEmail("emailteste.com");
        Set<ConstraintViolation<DonationDTO>> violations = validator.validate(donationDTO);
        assertThat(violations).hasSize(1);
    }
}
