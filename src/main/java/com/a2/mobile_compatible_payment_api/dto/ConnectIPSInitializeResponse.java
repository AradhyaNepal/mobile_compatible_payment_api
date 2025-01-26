package com.a2.mobile_compatible_payment_api.dto;

import lombok.Getter;

@Getter
public class ConnectIPSInitializeResponse extends BasePaymentInitializeResponse {
    final private  String paymentWebHTML;
    final private  String successURL;
    final  private  String errorURL;

    public ConnectIPSInitializeResponse(String paymentWebHTML, String successURL, String transactionId, Double amountRs, String errorURL) {
        super(transactionId, amountRs);
        this.paymentWebHTML =paymentWebHTML;
        this.successURL=successURL;
        this.errorURL=errorURL;

    }
}
