package br.org.donations.authorizationapi.dto.billet;

import br.org.donations.authorizationapi.dto.DonorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BilletDonationDTO {
    DonorDTO donor;
    AddressDTO address;
    BigDecimal value;
}
