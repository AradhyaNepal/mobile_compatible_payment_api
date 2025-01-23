package com.a2.mobile_compatible_payment_api.entity;


import com.a2.mobile_compatible_payment_api.constant.enums.PaymentStatus;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentVendor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentVendor paymentVendor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @NotNull
    private Instant initiatedDate;

    @NotNull
    private Double amountInRs;

    @NotNull
    private String purposeRemark;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private  MyUsers user;

    private Instant verifiedDate;
    private String vendorInitRequest;
    private String vendorInitResponse;
    private String vendorVerifyRequest;
    private String vendorVerifyResponse;





}
