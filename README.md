This repository is made to give documentation about how backend should make payment API which  are compatible with mobile.

For this documentation I have used three payment vendor as example; Esewa, Khalti and ConnectIPS.
Esewa and Khalti are payment vendor which have Mobile SDK available, but ConnectIPS sadly don't have,
so these three serve a perfect example how to make API on different scenarios.

Although the demo payment vendor are only Nepal available vendors, and the backend to make dummy API is Spring boot,
the documentation is independent of these two factors.

You can also watch my YouTube Video to get better understanding of the Flow:
Or You can watch this Figma Design:

**Scenario 1: Payment SDK Available.**
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
- Never make Mobile Team to hard code clientId and clientSecret in there code. It must dynamically come from backend. Even backend must not hardcode it in there code, they must either use .env or use database to store.
- Never make Mobile Team to generate TrasactionId. Because payment is not just about doing the payment, its also about having track of the payment. Sometimes user might have initiated, done payment, but in the middle our server was down, in this case user amount might get deducted but user's transaction for which he/she had done payment in our system might not get done. Storing transaction ID in backend helps customer support to help customers. Also it helps to track hackers, who suddenly become premium user in the system but there is not transaction details in our system. Payment is big deal do not take it easily.
- If you are calling any third party API to initiate, like of Esewa, also save the initiateRequest and initiateResponse as log, it helps to keep good track. In features like payment do not be greedy of storage the logs take.

  


