package com.a2.mobile_compatible_payment_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BasePaymentInitializeResponse {
    private String transactionId;
    private  Double transactionAmountRs;
}
