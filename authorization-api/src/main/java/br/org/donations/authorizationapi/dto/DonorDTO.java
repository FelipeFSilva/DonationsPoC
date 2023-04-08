package br.org.donations.authorizationapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9]{11,14}$")
    private String documentNumber;

    @NotBlank
    @Email
    private String email;
}
