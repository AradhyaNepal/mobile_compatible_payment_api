package com.a2.mobile_compatible_payment_api.dto;


import com.a2.mobile_compatible_payment_api.constant.enums.PaymentVendor;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentInitializeRequest {
    @NotEmpty(message = StringConstants.paymentVendorRequired)
    private PaymentVendor paymentVendor;

    @NotEmpty(message = StringConstants.membershipIdRequired)
    private String membershipCode;




}
