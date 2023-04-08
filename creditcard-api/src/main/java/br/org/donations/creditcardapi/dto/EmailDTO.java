package br.org.donations.creditcardapi.dto;

import br.org.donations.creditcardapi.model.Donation;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

    @NotBlank
    private String donorName;
    @NotBlank
    private String documentNumber;
    @NotBlank
    private String typePerson;
    @NotBlank
    private String email;
    @NotBlank
    private String statusDonation;
    @NotBlank
    private BigDecimal donationValue;
    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime donationCreatedAt;

    public static EmailDTO donationToEmailDTO(Donation donation){
        return EmailDTO.builder()
                .donorName(donation.getDonor().getName())
                .documentNumber(donation.getDonor().getDocumentNumber())
                .typePerson(donation.getDonor().getType())
                .email(donation.getDonor().getEmail())
                .statusDonation(donation.getStatus())
                .donationValue(donation.getValuePaid())
                .donationCreatedAt(donation.getCreatedAt())
                .build();
    }

}
