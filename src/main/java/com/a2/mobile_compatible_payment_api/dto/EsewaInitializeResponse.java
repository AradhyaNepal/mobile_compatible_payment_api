package com.a2.mobile_compatible_payment_api.dto;

import lombok.Getter;

@Getter
public class EsewaInitializeResponse extends BasePaymentInitializeResponse {
    final private String clientId;
    final private String clientSecret;

    EsewaInitializeResponse(String clientId, String clientSecret, String transactionId, Double amountRs) {
        super(transactionId, amountRs);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
