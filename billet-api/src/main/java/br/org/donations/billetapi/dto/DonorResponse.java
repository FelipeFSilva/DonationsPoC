package br.org.donations.billetapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorResponse {
    @Length(max = 255)
    @NotBlank
    private String name;

    @Length(min = 2, max = 2)
    @NotBlank
    private String type;

    @Length(min = 11, max = 14)
    @NotBlank
    private String documentNumber;

    @Email
    @NotBlank
    private String email;
}
