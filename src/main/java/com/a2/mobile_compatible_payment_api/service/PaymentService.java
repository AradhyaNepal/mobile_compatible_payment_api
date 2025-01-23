package com.a2.mobile_compatible_payment_api.service;

import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentStatus;
import com.a2.mobile_compatible_payment_api.dto.*;
import com.a2.mobile_compatible_payment_api.entity.Membership;
import com.a2.mobile_compatible_payment_api.entity.Transaction;
import com.a2.mobile_compatible_payment_api.model.CustomException;
import com.a2.mobile_compatible_payment_api.repository.MembershipRepository;
import com.a2.mobile_compatible_payment_api.repository.MerchantKeysRepository;
import com.a2.mobile_compatible_payment_api.repository.TransactionRepository;
import com.a2.mobile_compatible_payment_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
                .initiatedDate(Instant.now())
                .amountInRs(membershipGet.getMembershipAmountRs())
                .purposeRemark("Membership " + membershipGet.getMembershipCode() + " Payment with merchant " + defaultMerchantGet.getMerchantEmail())
                .user(dummyUser.get())
                .build());

        return new EsewaInitializeResponse(
                defaultMerchantGet.getEsewaClientId(),
                defaultMerchantGet.getEsewaClientSecret(),
                transaction.getId().toString(),
                transaction.getAmountInRs(),
                defaultMerchantGet.getWebsiteUrl(),
        );


    }

    public void verifyPayment(PaymentVerifyRequest request) throws CustomException {
        var transaction = transactionRepository.findById(UUID.fromString(request.getTransactionId()));
        if (transaction.isEmpty()) {
            throw new CustomException(StringConstant.noTransactionFound);
        }
        var transactionGet = transaction.get();
        transactionGet.setVendorInitRequest("https://rc.esewa.com.np/mobile/transaction?productId=" + request.getTransactionId() + "&amount=" + transactionGet.getAmountInRs());
        transactionGet.setVendorVerifyResponse("[\n" +
                "    {\n" +
                "        \"productId\": \"" + request.getTransactionId() + "\",\n" +
                "        \"productName\": \"Android SDK Payment\",\n" +
                "        \"totalAmount\": \"" + transactionGet.getAmountInRs() + "\",\n" +
                "        \"code\": \"00\",\n" +
                "        \"message\": {\n" +
                "            \"technicalSuccessMessage\": \"Your transaction has been completed.\",\n" +
                "            \"successMessage\": \"Your transaction has been completed.\"\n" +
                "        },\n" +
                "        \"transactionDetails\": {\n" +
                "            \"date\": \"Mon Dec You will get current Date here NPT 2022\",\n" +
                "            \"referenceId\": \"" + request.getVendorPaymentId() + "\",\n" +
                "            \"status\": \"COMPLETE\"\n" +
                "        },\n" +
                "        \"merchantName\": \"Android SDK Payment\"\n" +
                "    }\n" +
                "] ");
        transactionGet.setPaymentStatus(PaymentStatus.success);
        transactionGet.setVerifiedDate(Instant.now());
        transactionRepository.save(transactionGet);
        var user = transactionGet.getUser();
        user.setPremium(true);
        userRepository.save(user);

    }

    public String getPremiumContent() throws CustomException {
        var dummyUser = userRepository.findById(1);
        if (dummyUser.isEmpty()) {
            throw new CustomException(StringConstant.noUserFound);
        }
        if (!dummyUser.get().isPremium()) {
            throw new CustomException(StringConstant.youAreNotPremiumUser, HttpStatus.FORBIDDEN);
        }
        return "This is very premium content which you are viewing since you have paid the membership";
    }

    public List<MembershipResponse> getAllMembership() {
        return membershipRepository.findAll().stream().map(
                e -> MembershipResponse.builder()
                        .membershipName(e.getMembershipName())
                        .membershipAmount(e.getMembershipBenefits())
                        .membershipCode(e.getMembershipCode())
                        .membershipName(e.getMembershipName())
                        .build()
        ).toList();
    }
}
