package com.a2.mobile_compatible_payment_api.dto;

import lombok.Getter;

@Getter
public class EsewaInitializeResponse extends BasePaymentInitializeResponse {
    final private String clientId;
    final private String clientSecret;
    final  private  String website;

    public EsewaInitializeResponse(String clientId, String clientSecret, String transactionId, Double amountRs,String website) {
        super(transactionId, amountRs);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.website=website;
    }
}
