package com.a2.mobile_compatible_payment_api.service;

import com.a2.mobile_compatible_payment_api.constant.StringConstant;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentStatus;
import com.a2.mobile_compatible_payment_api.constant.enums.PaymentVendor;
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
        var dummyUserGet = dummyUser.get();
        if (dummyUserGet.isPremium()) {
            throw new CustomException(StringConstant.userAlreadyPremium);
        }

        var defaultMerchant = merchantKeysRepository.getDefaultMerchant();
        if (defaultMerchant.isEmpty()) {
            throw new CustomException(StringConstant.noDefaultMerchantFound);
        }
        var defaultMerchantGet = defaultMerchant.get();
        var transactionT = Transaction.builder()
                .paymentVendor(request.getPaymentVendor())
                .paymentStatus(PaymentStatus.initial)
                .initiatedDate(Instant.now())
                .amountInRs(membershipGet.getMembershipAmountRs())
                .purposeRemark("Membership " + membershipGet.getMembershipCode() + " Payment with merchant " + defaultMerchantGet.getMerchantEmail())
                .user(dummyUserGet)
                .build();
        if (request.getPaymentVendor() == PaymentVendor.esewa) {
            var transaction = transactionRepository.save(transactionT);
            return new EsewaInitializeResponse(
                    defaultMerchantGet.getEsewaClientId(),
                    defaultMerchantGet.getEsewaClientSecret(),
                    transaction.getId().toString(),
                    transaction.getAmountInRs(),
                    defaultMerchantGet.getWebsiteUrl()
            );
        } else if (request.getPaymentVendor() == PaymentVendor.khalti) {
            var dummyPIDX = "bZQLD9wRVWo4CdESSfuSsB";

            transactionT.setPaymentVendorId(dummyPIDX);
            transactionT.setVendorInitRequest("{\n" +
                    "  \"return_url\": \"https://example.com/payment/\",\n" +
                    "  \"website_url\": \"https://example.com/\",\n" +
                    "  \"amount\": 1300,\n" +
                    "  \"purchase_order_id\": \"test12\",\n" +
                    "  \"purchase_order_name\": \"test\",\n" +
                    "  \"customer_info\": {\n" +
                    "      \"name\": \"Khalti Bahadur\",\n" +
                    "      \"email\": \"example@gmail.com\",\n" +
                    "      \"phone\": \"9800000123\"\n" +
                    "  },\n" +
                    "  \"amount_breakdown\": [\n" +
                    "      {\n" +
                    "          \"label\": \"Mark Price\",\n" +
                    "          \"amount\": 1000\n" +
                    "      },\n" +
                    "      {\n" +
                    "          \"label\": \"VAT\",\n" +
                    "          \"amount\": 300\n" +
                    "      }\n" +
                    "  ],\n" +
                    "  \"product_details\": [\n" +
                    "      {\n" +
                    "          \"identity\": \"1234567890\",\n" +
                    "          \"name\": \"Khalti logo\",\n" +
                    "          \"total_price\": 1300,\n" +
                    "          \"quantity\": 1,\n" +
                    "   \"unit_price\": 1300\n" +
                    "      }\n" +
                    "  ],\n" +
                    "  \"merchant_username\": \"merchant_name\",\n" +
                    "  \"merchant_extra\": \"merchant_extra\"\n" +
                    "}");
            transactionT.setVendorInitResponse("  {\n" +
                    "        \"pidx\": \"bZQLD9wRVWo4CdESSfuSsB\",\n" +
                    "        \"payment_url\": \"https://test-pay.khalti.com/?pidx=bZQLD9wRVWo4CdESSfuSsB\",\n" +
                    "        \"expires_at\": \"2023-05-25T16:26:16.471649+05:45\",\n" +
                    "        \"expires_in\": 1800\n" +
                    "    }");
            var transaction = transactionRepository.save(transactionT);
            return new KhaltiInitializeResponse(
                    dummyPIDX,
                    defaultMerchantGet.getKhaltiPublic(),
                    transaction.getId().toString(),
                    transaction.getAmountInRs()
            );

        } else {
            var transaction = transactionRepository.save(transactionT);
            return new ConnectIPSInitializeResponse(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "    <title>Dummy Payment Page</title>\n" +
                            "    <style>\n" +
                            "        body {\n" +
                            "            font-family: Arial, sans-serif;\n" +
                            "            background-color: #f4f4f9;\n" +
                            "            display: flex;\n" +
                            "            justify-content: center;\n" +
                            "            align-items: center;\n" +
                            "            height: 100vh;\n" +
                            "            margin: 0;\n" +
                            "        }\n" +
                            "        .payment-container {\n" +
                            "            background: #fff;\n" +
                            "            padding: 20px;\n" +
                            "            border-radius: 10px;\n" +
                            "            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\n" +
                            "            width: 100%;\n" +
                            "            max-width: 400px;\n" +
                            "        }\n" +
                            "        .payment-container h2 {\n" +
                            "            text-align: center;\n" +
                            "            margin-bottom: 20px;\n" +
                            "        }\n" +
                            "        .form-group {\n" +
                            "            margin-bottom: 15px;\n" +
                            "        }\n" +
                            "        .form-group label {\n" +
                            "            display: block;\n" +
                            "            margin-bottom: 5px;\n" +
                            "            font-weight: bold;\n" +
                            "        }\n" +
                            "        .form-group input {\n" +
                            "            width: 100%;\n" +
                            "            padding: 10px;\n" +
                            "            border: 1px solid #ccc;\n" +
                            "            border-radius: 5px;\n" +
                            "            font-size: 14px;\n" +
                            "        }\n" +
                            "        .form-group input:focus {\n" +
                            "            border-color: #007bff;\n" +
                            "            outline: none;\n" +
                            "            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);\n" +
                            "        }\n" +
                            "        .submit-button {\n" +
                            "            width: 100%;\n" +
                            "            padding: 10px;\n" +
                            "            background-color: #007bff;\n" +
                            "            color: #fff;\n" +
                            "            border: none;\n" +
                            "            border-radius: 5px;\n" +
                            "            font-size: 16px;\n" +
                            "            cursor: pointer;\n" +
                            "        }\n" +
                            "        .submit-button:hover {\n" +
                            "            background-color: #0056b3;\n" +
                            "        }\n" +
                            "    </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <div class=\"payment-container\">\n" +
                            "        <h2>Payment Page</h2>\n" +
                            "        <form action=\"/submit-payment\" method=\"POST\">\n" +
                            "            <div class=\"form-group\">\n" +
                            "                <label for=\"card-number\">Card Number</label>\n" +
                            "                <input type=\"text\" id=\"card-number\" name=\"card-number\" placeholder=\"1234 5678 9012 3456\" required>\n" +
                            "            </div>\n" +
                            "            <div class=\"form-group\">\n" +
                            "                <label for=\"expiry-date\">Expiration Date</label>\n" +
                            "                <input type=\"month\" id=\"expiry-date\" name=\"expiry-date\" required>\n" +
                            "            </div>\n" +
                            "            <div class=\"form-group\">\n" +
                            "                <label for=\"cvv\">CVV</label>\n" +
                            "                <input type=\"password\" id=\"cvv\" name=\"cvv\" placeholder=\"123\" maxlength=\"3\" required>\n" +
                            "            </div>\n" +
                            "            <div class=\"form-group\">\n" +
                            "                <label for=\"amount\">Amount</label>\n" +
                            "                <input type=\"number\" id=\"amount\" name=\"amount\" placeholder=\"Enter amount\" required>\n" +
                            "            </div>\n" +
                            "            <button type=\"submit\" class=\"submit-button\">Pay Now</button>\n" +
                            "        </form>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>\n",
                    "http:www.uat-connectips.com/success",
                    transaction.getId().toString(),
                    transaction.getAmountInRs(),
                    "http:www.uat-connectips.com/error"
            );
        }


    }

    public void verifyPayment(PaymentVerifyRequest request) throws CustomException {
        var transaction = transactionRepository.findById(UUID.fromString(request.getTransactionId()));
        if (transaction.isEmpty()) {
            throw new CustomException(StringConstant.noTransactionFound);
        }
        var transactionGet = transaction.get();
        if (transactionGet.getPaymentVendor() == PaymentVendor.esewa) {
            transactionGet.setVendorVerifyRequest("https://rc.esewa.com.np/mobile/transaction?productId=" + request.getTransactionId() + "&amount=" + transactionGet.getAmountInRs());
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
            transactionGet.setPaymentVendorId(request.getVendorPaymentId());
        } else if (transactionGet.getPaymentVendor() == PaymentVendor.khalti) {
            transactionGet.setVendorVerifyRequest("{\n" +
                    "   \"pidx\": \"HT6o6PEZRWFJ5ygavzHWd5\"\n" +
                    "}");
            transactionGet.setVendorVerifyResponse("{\n" +
                    "   \"pidx\": \"HT6o6PEZRWFJ5ygavzHWd5\",\n" +
                    "   \"total_amount\": 1000,\n" +
                    "   \"status\": \"Completed\",\n" +
                    "   \"transaction_id\": \"GFq9PFS7b2iYvL8Lir9oXe\",\n" +
                    "   \"fee\": 0,\n" +
                    "   \"refunded\": false\n" +
                    "}");
        } else {
            transactionGet.setVendorVerifyRequest("jflkdsjf");
            transactionGet.setVendorVerifyResponse("aksjdfjalsdkf");

        }
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
                        .membershipAmount(e.getMembershipAmountRs())
                        .membershipCode(e.getMembershipCode())
                        .membershipName(e.getMembershipName())
                        .membershipBenefits(e.getMembershipBenefits())
                        .build()
        ).toList();
    }
}
