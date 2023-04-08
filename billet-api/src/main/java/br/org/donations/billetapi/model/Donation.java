package br.org.donations.billetapi.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Donation {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Length(max = 8)
    @NotBlank
    private String status;

    @Column(name = "value_paid")
    @NotNull
    @Positive
    private BigDecimal valuePaid;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @NotNull
    private LocalDateTime updatedAt;

    @Column(name = "link_billet")
    @Nullable
    private String linkBillet;

    @Embedded
    @NotNull
    private Donor donor;
}
