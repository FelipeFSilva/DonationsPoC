package br.org.donations.billetapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
