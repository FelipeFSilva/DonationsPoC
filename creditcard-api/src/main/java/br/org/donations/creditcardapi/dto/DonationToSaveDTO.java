package br.org.donations.creditcardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationToSaveDTO {

    private String status;
    private BigDecimal valuePaid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DonorToSaveDTO donor;
}
