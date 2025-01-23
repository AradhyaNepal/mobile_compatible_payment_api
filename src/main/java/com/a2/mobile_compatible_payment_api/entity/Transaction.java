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
    private PaymentVendor paymentVendor;

    @NotNull
    private PaymentStatus paymentStatus;

    @NotNull
    private Instant initiatedDate;

    @NotNull
    private Double amountInRs;

    @NotNull
    private String purposeRemark;

    private Instant verifiedDate;
    private String vendorInitRequest;
    private String vendorInitResponse;
    private String vendorVerifyRequest;
    private String vendorVerifyResponse;


}
