package br.org.donations.paymentgateway.dto;

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
public class PaymentBilletDTO {
    @NotNull
    private DonorDTO donor;

    @NotNull
    private AddressDTO address;

    @NotNull
    @Positive
    private BigDecimal value;
}
