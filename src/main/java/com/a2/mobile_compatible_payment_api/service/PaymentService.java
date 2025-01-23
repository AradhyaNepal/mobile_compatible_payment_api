package com.a2.mobile_compatible_payment_api.service;

import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.dto.BasePaymentInitializeResponse;
import com.a2.mobile_compatible_payment_api.dto.PaymentInitializeRequest;
import com.a2.mobile_compatible_payment_api.model.CustomException;
import com.a2.mobile_compatible_payment_api.model.GenericResponseEntity;
import com.a2.mobile_compatible_payment_api.repository.MembershipRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
    final private MembershipRepository membershipRepository;

    public BasePaymentInitializeResponse paymentInitialize(PaymentInitializeRequest request) {
        var membership = membershipRepository.getByCode(request.getMembershipCode());
        if (membership.isEmpty()) {
            throw new CustomException(StringConstant.invalidMembershipCode);
        }
        var membershipGet = membership.get();



    }
}
