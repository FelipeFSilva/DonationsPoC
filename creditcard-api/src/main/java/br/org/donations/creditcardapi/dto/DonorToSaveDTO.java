package br.org.donations.creditcardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorToSaveDTO {

    private String name;
    private String type;
    private String documentNumber;
    private String email;
}
