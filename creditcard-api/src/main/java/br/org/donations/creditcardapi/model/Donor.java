package br.org.donations.creditcardapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Donor {

    @Column(name = "donor_name")
    @Length(max = 255)
    @NotBlank
    private String name;

    @Column(name = "type_person")
    @Length(min = 2, max = 2)
    @NotBlank
    private String type;

    @Column(name = "document_number")
    @Length(min = 11, max = 14)
    @NotBlank
    private String documentNumber;

    @Column
    @Email
    @NotBlank
    private String email;
}
