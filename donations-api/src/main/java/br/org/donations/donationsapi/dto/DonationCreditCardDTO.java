package br.org.donations.donationsapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationCreditCardDTO {

    @Valid
    @NotNull
    private DonorDTO donor;

    @Valid
    @NotNull
    private CreditCardDTO creditCard;

    @NotNull
    @Positive
    private BigDecimal value;
}
