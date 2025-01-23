package com.a2.mobile_compatible_payment_api.service;

import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentStatus;
import com.a2.mobile_compatible_payment_api.dto.BasePaymentInitializeResponse;
import com.a2.mobile_compatible_payment_api.dto.EsewaInitializeResponse;
import com.a2.mobile_compatible_payment_api.dto.PaymentInitializeRequest;
import com.a2.mobile_compatible_payment_api.entity.Transaction;
import com.a2.mobile_compatible_payment_api.model.CustomException;
import com.a2.mobile_compatible_payment_api.repository.MembershipRepository;
import com.a2.mobile_compatible_payment_api.repository.MerchantKeysRepository;
import com.a2.mobile_compatible_payment_api.repository.TransactionRepository;
import com.a2.mobile_compatible_payment_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
    final private MembershipRepository membershipRepository;
    final private TransactionRepository transactionRepository;
    final private MerchantKeysRepository merchantKeysRepository;
    final private UserRepository userRepository;


    public BasePaymentInitializeResponse paymentInitialize(PaymentInitializeRequest request) throws CustomException {
        var membership = membershipRepository.getByCode(request.getMembershipCode());
        if (membership.isEmpty()) {
            throw new CustomException(StringConstant.invalidMembershipCode);
        }
        var membershipGet = membership.get();

        var dummyUser = userRepository.findById(1);
        if (dummyUser.isEmpty()) {
            throw new CustomException(StringConstant.noUserFound);
        }
        var defaultMerchant = merchantKeysRepository.getDefaultMerchant();
        if (defaultMerchant.isEmpty()) {
            throw new CustomException(StringConstant.noDefaultMerchantFound);
        }
        var defaultMerchantGet = defaultMerchant.get();
        var transaction = transactionRepository.save(Transaction.builder()
                .paymentVendor(request.getPaymentVendor())
                .paymentStatus(PaymentStatus.initial)
                .amountInRs(membershipGet.getMembershipAmountRs())
                .purposeRemark("Membership " + membershipGet.getMembershipCode() + " Payment with merchant " + defaultMerchantGet.getMerchantEmail())
                .user(dummyUser.get())
                .build());

        return new EsewaInitializeResponse(
                defaultMerchantGet.getEsewaClientId(),
                defaultMerchantGet.getEsewaClientSecret(),
                transaction.getId().toString(),
                transaction.getAmountInRs()
        );


    }
}
