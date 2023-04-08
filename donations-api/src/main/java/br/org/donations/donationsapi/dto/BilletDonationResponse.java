package br.org.donations.donationsapi.dto;

import br.org.donations.donationsapi.enums.DonationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BilletDonationResponse extends DonationResponse {

    private String link;

    public static BilletDonationResponse donationBilletDTOToDonationResponse(DonationBilletDTO donationBilletDTO, String link){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return BilletDonationResponse.builder()
                .status(DonationStatusEnum.PENDING.toString())
                .valuePaid(donationBilletDTO.getValue())
                .createdAt(dateTimeNow)
                .updatedAt(dateTimeNow)
                .donor(DonorResponse.donorDTOToDonorResponse(donationBilletDTO.getDonor()))
                .link(link)
                .build();
    }
}
