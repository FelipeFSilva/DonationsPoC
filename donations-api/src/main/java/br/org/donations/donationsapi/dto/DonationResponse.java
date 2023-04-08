package br.org.donations.donationsapi.dto;

import br.org.donations.donationsapi.enums.DonationStatusEnum;
import br.org.donations.donationsapi.utils.PaymentCreditCardUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponse {

    @Length(max = 8)
    @NotBlank
    private String status;

    @NotNull
    @Positive
    private BigDecimal valuePaid;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @NotNull
    private DonorResponse donor;

    public static DonationResponse donationCreditCardDTOToDonationResponse(DonationCreditCardDTO donationCreditCardDTO, String paymentStatus){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        boolean isValidPayment = PaymentCreditCardUtils.isValidPayment(paymentStatus);

        return DonationResponse.builder()
                .status(isValidPayment ? DonationStatusEnum.APPROVED.toString() : DonationStatusEnum.REJECTED.toString())
                .valuePaid(donationCreditCardDTO.getValue())
                .createdAt(dateTimeNow)
                .updatedAt(dateTimeNow)
                .donor(DonorResponse.donorDTOToDonorResponse(donationCreditCardDTO.getDonor()))
                .build();
    }

}
