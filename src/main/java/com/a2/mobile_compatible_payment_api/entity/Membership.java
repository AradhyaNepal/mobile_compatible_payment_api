package com.a2.mobile_compatible_payment_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Membership")
public class Membership {
    @Id
    @GeneratedValue
    private Integer id;


    @Column(unique = true, nullable = false)
    private String membershipCode;

    @NotNull
    private String membershipName;

    @NotNull
    private String membershipBenefits;

    @NotNull
    private Double membershipAmountRs;


}
