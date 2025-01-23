package com.a2.mobile_compatible_payment_api.controller;

import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.dto.BasePaymentInitializeResponse;
import com.a2.mobile_compatible_payment_api.dto.PaymentInitializeRequest;
import com.a2.mobile_compatible_payment_api.dto.PaymentVerifyRequest;
import com.a2.mobile_compatible_payment_api.model.CustomException;
import com.a2.mobile_compatible_payment_api.model.GenericResponseEntity;
import com.a2.mobile_compatible_payment_api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {


    final private PaymentService paymentService;


    @PostMapping(value = "initiate")
    GenericResponseEntity<BasePaymentInitializeResponse> paymentInitialize(@Valid @RequestBody PaymentInitializeRequest request) throws CustomException {
        return GenericResponseEntity.successWithData(paymentService.paymentInitialize(request), StringConstant.paymentInitializeSuccess);
    }


    @PostMapping(value = "verify")
    GenericResponseEntity<Void> verify(@Valid @RequestBody PaymentVerifyRequest request) throws CustomException {
        paymentService.verifyPayment(request);
        return GenericResponseEntity.successWithMessage(StringConstant.verifyPaymentSuccess);
    }


}
