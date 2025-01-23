package com.a2.mobile_compatible_payment_api.dto;


import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentVendor;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentVerifyRequest {
    @NotEmpty(message = StringConstant.transactionIdRequired)
    private String transactionId;

    @NotEmpty(message = StringConstant.vendorPaymentIdRequired)
    private String vendorPaymentId;




}
