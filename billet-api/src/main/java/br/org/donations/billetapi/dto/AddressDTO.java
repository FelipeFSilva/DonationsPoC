package br.org.donations.billetapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NotBlank
    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$")
    private String addressNumber;

    @NotBlank
    @Size(max = 100)
    private String street;

    @NotNull
    private Integer number;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 2)
    private String state;
}
