package com.a2.mobile_compatible_payment_api.dto;

import lombok.Getter;

@Getter
public class KhaltiInitializeResponse extends BasePaymentInitializeResponse {
    final private String pidx;
    final private String publicKey;

    public KhaltiInitializeResponse(String pidx, String publicKey, String transactionId, Double amountRs) {
        super(transactionId, amountRs);
        this.pidx = pidx;
        this.publicKey = publicKey;
    }
}
