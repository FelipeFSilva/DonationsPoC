package br.org.donations.paymentgateway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String documentNumber;
    @NotBlank
    private String email;
}
