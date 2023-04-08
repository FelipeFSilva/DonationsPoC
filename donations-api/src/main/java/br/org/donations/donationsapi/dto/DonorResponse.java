package br.org.donations.donationsapi.dto;

import br.org.donations.donationsapi.enums.TypePersonEnum;
import br.org.donations.donationsapi.utils.DocumentNumberUtils;
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

    public static DonorResponse donorDTOToDonorResponse(DonorDTO donorDTO){
        String documentNumber = donorDTO.getDocumentNumber();
        boolean isCNPJ = DocumentNumberUtils.documentNumberIsCNPJ(documentNumber);

        return DonorResponse.builder()
                            .name(donorDTO.getName())
                            .email(donorDTO.getEmail())
                            .type(isCNPJ ? TypePersonEnum.PJ.toString() : TypePersonEnum.PF.toString())
                            .documentNumber(isCNPJ ? documentNumber : DocumentNumberUtils.hideCPFNumbers(documentNumber))
                            .build();
    }
}
