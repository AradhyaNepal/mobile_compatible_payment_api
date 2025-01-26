This repository is made to give documentation about how backend should make payment API which  are compatible with mobile. All the internals in the code is dummy, since the agenda was to demostrate end API.
![image](https://github.com/user-attachments/assets/33118b4c-2409-4e80-9c69-2ec7af49568a)


For this documentation I have used three payment vendor as example; Esewa, Khalti and ConnectIPS.
Esewa and Khalti are payment vendor which have Mobile SDK available, but ConnectIPS sadly don't have,
so these three serve a perfect example how to make API on different scenarios.

Although the demo payment vendor are only Nepal available vendors, and the backend to make dummy API is Spring boot,
the documentation is independent of these two factors.

You can also watch my YouTube Video to get better understanding of the Flow:
Or You can watch this Figma Design:


**Scenario 1: Payment SDK Available.**
Always try to use SDK over showing web on mobile.
Showing web makes mobile unstable, its like what happens when there is lots of male harmone in inside female body. It causes issues.
Example used are Esewa and Khalti.
First read the mobile sdk documentation which ever payment vendor you are integrating.

**Esewa**
For Esewa, mobile needs clientId, clientSecretId, productId, productName and productPrice.
You can watch it in this documentation https://developer.esewa.com.np/pages/Flutter#overview

Initially there is iniate API:
/initiate : 
Request:
{
  "paymentVendor": "esewa",
  "membershipCode": "uni"
}

This iniate API works for Esewa, Khalti and ConnectIPS.
So in paymentVendor its asking from where user want to do payment.
MembershipCode is unique to my application's business logic, its about for what user is doing payment.
If there is only one thing for which user can do payment in that specific API no need to ask it, same with payment vendor.

Response:
{
  "data": {
    "clientId": "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R",
    "clientSecret": "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==",
    "transactionId": "0ff3c89d-67e1-4e79-a06f-d45e67d6db73",
    "website": "www.aradhyagopalnepal.com.np",
    "transactionAmountRs": 10000
  },
  "message": "Successfully initialized Payment"
}

From this user gets the required clientId, clientSecret, transactionId and productPrice.
Regarding productName which is required for payment SDK, as per my project business logic, 
user picks us membership from a dropdown, and from that Mobile passes the membership code in initiate API.
From the dropdown, Mobile team have the productName. But if you want to pass custom, the data must be sent from the Backend.

Points to note:
- Since Esewa is third party server, sometimes they might not be available, backend must either provide dynamic list of available vendor, and in init throw exception if unavailable vendor is used.
-  Never make Mobile Team to hard code clientId and clientSecret in there code. It must dynamically come from backend. Even backend must not hardcode it in there code, they must either use .env or use database to store.
- Never make Mobile Team to generate TrasactionId. Because payment is not just about doing the payment, its also about having track of the payment. Sometimes user might have initiated, done payment, but in the middle our server was down, in this case user amount might get deducted but user's transaction for which he/she had done payment in our system might not get done. Storing transaction ID in backend helps customer support to help customers. Also it helps to track hackers, who suddenly become premium user in the system but there is not transaction details in our system. Payment is big deal do not take it easily.
- If you are calling any third party API to initiate, also save the initiateRequest and initiateResponse as log, it helps to keep good track. In features like payment do not be greedy of storage the logs take.

User doing the payment
From this initiate response, mobile team will open Esewa SDK on Mobile. Esewa SDK will handle all the complex logic needed to do the payment.
And gives mobile onSuccess and onError callbacks. In onError callbacks, mobile team will show error screen. For better logs, backend can make log API which saves all of the error in MongoDB.

/verify
onSuccess mobile team assume that from Esewa the payment was done, user money is deducted and is sent to our merchant. But in backend user transaction is still not saved, if user is doing payment to be premium user, in backend user is still not premium user. For this mobile need to call verify API, and in verify API they need to pass refId which Esewa onSuccess.

Request
{
  "transactionId": "0ff3c89d-67e1-4e79-a06f-d45e67d6db73",
  "vendorPaymentId": "refId123123"
}

In request, backend must ask for transactionId which it generated during initiate, and vendorPaymentId. 

Response:
{
  "message": "Membership successfully bought"
}
Sending back mobile team success status code so that they can show user proper screen.

Point to note:
- When it comes to data consistency and security backend should never trust frontend team. Backend must do cross check from Esewa server whether the user is fraud or not. For that backend will use the vendorPaymentId(i.e RefId in case of Esewa), and using the secretKey hit the Esewa verify API.
- If Esewa says success, backend must automatically make user premium, backend must not ask mobile to call an another API to verify from esewa, and another API to make user premium.
- In entire process Backend must save log of Esewa request and response when they where verifying. Third party API are always prone to change there protocol or send unknown issue, keeping track saves developers sleep when something unexpected happens.

**Khalti**
Khalti is also payment vendor which provides mobile SDK. Just few difference for developers to know is the pidx, 
which is a unique id needed for mobile SDK. But this id must be generated in initiate API, where backend need to call
Khalti API whose compulsory fields are: return_url, website_url, amount, purchase_order_id and purchase_order_name.
For return_url, backend must create a static web template which is made of HTML and hosted on backend's specific web url,
or they must ask web developers to make a url.

purchase_order_id is unique transaction id in our system.
In response you will receive pidx.
 {
        "pidx": "bZQLD9wRVWo4CdESSfuSsB",
        "payment_url": "https://test-pay.khalti.com/?pidx=bZQLD9wRVWo4CdESSfuSsB",
        "expires_at": "2023-05-25T16:26:16.471649+05:45",
        "expires_in": 1800
  }

  For better up to date documentation read https://docs.khalti.com/khalti-epayment/

Coming back to API which mobile SDK consume first is initiate,
Request
{
  "paymentVendor": "khalti",
  "membershipCode": "uni"
}
Response:
{
  "data": {
    "pidx": "bZQLD9wRVWo4CdESSfuSsB",
    "publicKey": "bZQLD9wRVWo4CdESSfuSsB",
    "transactionId": "8a367c31-1bc2-4b86-8a0b-e7d3ebc40d81",
    "transactionAmountRs": 10000
  },
  "message": "Successfully initialized Payment"
}

Khalti SDK needs pidx and public Key to initiate the payment in Khalti SDK.
In my flow Mobile needs to save transactionId so that they can use it when they are verifying,
thats why I am sending it even Khalti SDK don't require it. Since we have passed PIDX, internally Khalti SDK can fetch the transactionId.

- Since you are calling third party API to initiate, save the initiateRequest and initiateResponse as log, it helps to keep good track. In features like payment do not be greedy of storage the logs take.

verify/
Everything same as esewa, Mobile will call verify once everything is done. And in backend you will do cross check, make user premium; 
and save log of third party verify request and response for future ease if unexpected happens.
Request
{
  "transactionId": "8a367c31-1bc2-4b86-8a0b-e7d3ebc40d81",
  "vendorPaymentId": "bZQLD9wRVWo4CdESSfuSsB"
}
Response:
{
  "message": "Membership successfully bought"
}










  


