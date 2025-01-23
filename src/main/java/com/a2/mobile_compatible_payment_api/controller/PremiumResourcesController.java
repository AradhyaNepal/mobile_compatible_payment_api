package com.a2.mobile_compatible_payment_api.controller;

import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.dto.MembershipResponse;
import com.a2.mobile_compatible_payment_api.dto.PaymentInitializeRequest;
import com.a2.mobile_compatible_payment_api.entity.Membership;
import com.a2.mobile_compatible_payment_api.model.CustomException;
import com.a2.mobile_compatible_payment_api.model.GenericResponseEntity;
import com.a2.mobile_compatible_payment_api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/api/v1/premium-content")
@RestController
@RequiredArgsConstructor
public class PremiumResourcesController {


    final private PaymentService paymentService;


    @GetMapping()
    GenericResponseEntity<String> premiumContent() throws CustomException {
        return GenericResponseEntity.successWithData(paymentService.getPremiumContent(), StringConstant.premiumContentFetched);
    }

    @GetMapping(value = "all-membership")
    GenericResponseEntity<List<MembershipResponse>> allMembership() {
        return GenericResponseEntity.successWithData(paymentService.getAllMembership(), StringConstant.premiumContentFetched);
    }


}
