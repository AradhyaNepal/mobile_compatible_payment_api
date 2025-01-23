package com.a2.mobile_compatible_payment_api.service;

import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentStatus;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentVendor;
import com.a2.mobile_compatible_payment_api.dto.BasePaymentInitializeResponse;
import com.a2.mobile_compatible_payment_api.dto.PaymentInitializeRequest;
import com.a2.mobile_compatible_payment_api.entity.MyUsers;
import com.a2.mobile_compatible_payment_api.entity.Transaction;
import com.a2.mobile_compatible_payment_api.model.CustomException;
import com.a2.mobile_compatible_payment_api.repository.MembershipRepository;
import com.a2.mobile_compatible_payment_api.repository.TransactionRepository;
import com.a2.mobile_compatible_payment_api.repository.UserRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {
    final private MembershipRepository membershipRepository;
    final private TransactionRepository transactionRepository;
    final private UserRepository userRepository;


    public BasePaymentInitializeResponse paymentInitialize(PaymentInitializeRequest request) throws CustomException {
        var membership = membershipRepository.getByCode(request.getMembershipCode());
        if (membership.isEmpty()) {
            throw new CustomException(StringConstant.invalidMembershipCode);
        }
        var membershipGet = membership.get();

        var dummyUser=userRepository.findById(1);
        if(dummyUser.isEmpty()){
            throw new CustomException(StringConstant.noUserFound);
        }


        private MyUsers user;
        private Instant verifiedDate;
        private String vendorInitRequest;
        private String vendorInitResponse;
        private String vendorVerifyRequest;
        private String vendorVerifyResponse;

        var transaction=Transaction.builder()
                .paymentVendor(request.getPaymentVendor())
                .paymentStatus(PaymentStatus.initial)
                .amountInRs(membershipGet.getMembershipAmountRs())
                .purposeRemark("Membership "+membershipGet.getMembershipCode()+" Payment")
                .user()
                .
                .build();
        var savedTransaction= transactionRepository.save(tra);


    }
}
