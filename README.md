### This repository is made to give documentation about how backend should make payment API which are compatible with mobile. 

### This demostration API, video and ReadMe is independent of backend techstack, mobile techstack and from payment vendor used.

![image](https://github.com/user-attachments/assets/33118b4c-2409-4e80-9c69-2ec7af49568a)
![image](https://github.com/user-attachments/assets/f444973b-f0da-47a9-93ea-9b2c6e5c5f51)
![image](https://github.com/user-attachments/assets/69ef3dcd-2cbf-413e-8ebc-bca0961f318f)


### Available Vendors:

![image](https://github.com/user-attachments/assets/ea0ff055-2476-4119-b720-ef9630c89a5c)

### Initiate Esewa Response (Have Mobile SDK):

![image](https://github.com/user-attachments/assets/252d9fed-6cca-44db-9474-5bef01536836)

### Initiate Khalti Response (Have Mobile SDK):

![image](https://github.com/user-attachments/assets/44c0912d-e842-4144-90d9-b06dff74aab5)

### Initiate ConnectIPS Response (No Mobile SDK):

![image](https://github.com/user-attachments/assets/f340d575-b11c-4e90-82b9-e12d796db44a)

### Intro
For this documentation I have used three payment vendor as example; Esewa, Khalti and ConnectIPS.

Esewa and Khalti are payment vendor which have Mobile SDK available, but ConnectIPS sadly don't have,
so these three serve a perfect example how to make API on different scenarios.

Although the demo payment vendor are only Nepal available vendors, and the backend to make dummy API is Spring boot,
**the documentation is independent of backend and of which vendor you are using.**

### Youtubes and Figmas
You can also watch my YouTube Video to get better understanding of the Flow: (YouTube)[https://youtu.be/u1_ZjulSx-w]

Or You can watch this Figma Design: (Figma)[https://www.figma.com/design/P99aIfSQetM3a7ZjtcH20O/MyPersonal?node-id=2-1261&t=fOTkI1MrPcUb0haR-1]

### Scenario 1: Mobile SDK Available.
Always try to use SDK over showing web on mobile.

Showing web makes mobile unstable, its like what happens when there is lots of male harmone in inside female body. It causes issues.

In Esewa and Khalti there are payment Vendor available, Scenario 1 explains what to do in these scenario.

What to do is simple:

**Read the mobile sdk documentation about which payment vendor you are integrating,***

**Do not read web documentation for Mobile, because mobile is not web.**

#### Esewa
For Esewa, mobile needs clientId, clientSecretId, productId, productName and productPrice.

You can watch it in this documentation [Esewa Offical Docs](https://developer.esewa.com.np/pages/Flutter#overview)

**Initially there is initiate API:**
Request:
```
{
  "paymentVendor": "esewa",
  "membershipCode": "uni"
}
```

This same initiate API is used to initiate for Esewa, Khalti and ConnectIPS. So in paymentVendor its asking from where user want to do payment.

MembershipCode is unique to my application's business logic, its about for what purpose user is doing payment.

If in your system there is only one thing and only one source for/from which user can do payment, then no need to ask paymentVendor and membershipCode.

Response:
```
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
```

From this user gets the required clientId, clientSecret, transactionId and productPrice. 

**Points to note:**
- Since Esewa is third party server, sometimes they might not be available, backend must either provide dynamic list of available vendor, and in init throw exception if unavailable vendor is used.
-  Never make Mobile Team to hard code clientId and clientSecret in there code. It must dynamically come from backend. Even backend must not hardcode it in there code, they must either use .env or use database to store.
- Never make Mobile Team to generate TrasactionId. Because payment is not just about doing the payment, its also about having track of the payment. Sometimes user might have initiated, done payment, but in the middle our server was down, in this case user amount might get deducted but user's transaction for which he/she had done payment in our system might not get done. Storing transaction ID in backend helps customer support to help customers. Also it helps to track hackers, who suddenly become premium user in the system but there is not transaction details in our system. Payment is big deal do not take it easily.
- If you are calling any third party API to initiate, also save the initiateRequest and initiateResponse as log, it helps to keep good track. In features like payment do not be greedy of storage the logs take.

**User doing the payment**
From this initiate response, mobile team will open Esewa SDK on Mobile. Esewa SDK will handle all the complex logic needed to do the payment.

SDK gives mobile: onSuccess and onError callbacks. In onError callbacks, mobile team will show error screen. For better logs, backend can make log API which saves all of the error in MongoDB.

**Verify API**
onSuccess mobile team assume that from Esewa the payment was done, user money is deducted and is sent to our merchant. 

But in backend user transaction is still not saved, if user is doing payment to be premium user, in backend user is still not premium user. 

For this mobile need to call verify API, and in verify API they need to pass refId which Esewa onSuccess.
    
**Request**
```
{
  "transactionId": "0ff3c89d-67e1-4e79-a06f-d45e67d6db73",
  "vendorPaymentId": "refId123123" //We got this from onSuccess callback on Esewa SDK in Mobile
}
```

In request, backend must ask for transactionId which it generated during initiate, and vendorPaymentId. 

**Response**
```
{
  "message": "Membership successfully bought"
}
```
Sending back mobile team success status code so that they can show user proper screen.

**Point to note:**
- When it comes to data consistency and security backend should never trust frontend team. Backend must do cross check from Esewa server whether the user is fraud or not. For that backend will use the vendorPaymentId(i.e RefId in case of Esewa), and using the secretKey hit the Esewa verify API.
- If Esewa says success, backend must automatically make user premium, backend must not ask mobile to call an another API to verify from esewa, and another API to make user premium.
- In entire process Backend must save log of Esewa request and response when they where verifying. Third party API are always prone to change there protocol or send unknown issue, keeping track saves developers sleep when something unexpected happens.

#### Khalti
Khalti is also payment vendor which provides mobile SDK. Just few difference for developers to know is the pidx, 
which is a unique id needed for mobile SDK. 

But this id must be generated in initiate API, where backend need to call
Khalti API whose compulsory fields are: return_url, website_url, amount, purchase_order_id and purchase_order_name.

For return_url, backend must create a static web template which is made of HTML and hosted on backend's specific web url,
or they must ask web developers to make a url.

purchase_order_id is unique transaction id in our system.

After backend calls initiate API, In response you will receive pidx.
```
 {
        "pidx": "bZQLD9wRVWo4CdESSfuSsB",
        "payment_url": "https://test-pay.khalti.com/?pidx=bZQLD9wRVWo4CdESSfuSsB",
        "expires_at": "2023-05-25T16:26:16.471649+05:45",
        "expires_in": 1800
  }
```

For better up to date documentation read https://docs.khalti.com/khalti-epayment/

Coming back to API which mobile SDK consume first is initiate,

**Request**
```
{
  "paymentVendor": "khalti",
  "membershipCode": "uni"
}
```
**Response:**
```
{
  "data": {
    "pidx": "bZQLD9wRVWo4CdESSfuSsB",
    "publicKey": "bZQLD9wRVWo4CdESSfuSsB",
    "transactionId": "8a367c31-1bc2-4b86-8a0b-e7d3ebc40d81",
    "transactionAmountRs": 10000
  },
  "message": "Successfully initialized Payment"
}
```

Khalti SDK needs pidx and public Key to initiate the payment in Khalti SDK.

In my flow Mobile needs to save transactionId so that they can use it when they are verifying,
thats why I am sending it even Khalti SDK don't require it. Since we have passed PIDX, internally Khalti SDK can fetch the transactionId.

**Point to note:**
- Since you are calling third party API to initiate, save the initiateRequest and initiateResponse as log, it helps to keep good track. In features like payment do not be greedy of storage the logs take.

**Verify**
Everything same as esewa, Mobile will call verify once everything is done. And in backend you will do cross check, make user premium; 
and save log of third party verify request and response for future ease if unexpected happens.

**Request**
```
{
  "transactionId": "8a367c31-1bc2-4b86-8a0b-e7d3ebc40d81",
  "vendorPaymentId": "bZQLD9wRVWo4CdESSfuSsB"
}
```

**Response**:
```
{
  "message": "Membership successfully bought"
}
```

### Scenario 2: Sadly no Mobile SDK Available, need to use Web.
#### ConnectIPS:
ConnectIPS is a payment vendor which have provided no Mobile SDK, and it is really sad, it makes mobile unstable because for this payment vendor we need to show web content inside mobile.

In scenarios like this Mobile developer always fight to get API where:
1) Backend provides an HTML content, or an url. This content needs to be opened in integrated web browser inside users mobile app, so backend provided content must have enough javascript which handles all the payment business logic. If backend is asking mobile to do any extra logics, reject it.
2) The problem with opening integrated web browser inside users mobile app is that app will loose control over the user's navigation, user can watch Youtube, Facebook on the website after payment get completed and never comes back. So backend must provide successUrl and failureUrl so that Mobile will know then if user gets redirected to this url in the web then it means the task of opening web is completed and now mobile need to close the web and get its control back.

With this rule in mind here:

**Initiate**
**Request:**
```
{
  "paymentVendor": "connectIPS",
  "membershipCode": "uni"
}
```

**Response:**
```
{
  "data": {
    "paymentWebHTML": "HTML content with enough javascript so that mobile team task is just to render this HTML inside mobile and listen for successURL and errorURL. No business logic in Mobile.",
    "successURL": "http:www.uat-connectips.com/success",
    "transactionId": "57aa3d9c-7028-4b55-b85a-6cdc47eaa314",
    "errorURL": "http:www.uat-connectips.com/error",
    "transactionAmountRs": 10000
  },
  "message": "Successfully initialized Payment"
}
```
Once the mobile catches that user is being navigated to successURL, they will extract the params of connectIPS transaction ID from the successURL to which connectIPS redirect and then they will hit the verify API to save the transaction done information in our server. Same like Esewa and Khalti Backend must do cross check, save logs of third party verify request-response, and make user premium.

**Request:**
```
{
  "transactionId": "57aa3d9c-7028-4b55-b85a-6cdc47eaa314",
  "vendorPaymentId": "230948230948"
}
```
**Response:**
```
{
  "message": "Membership successfully bought"
}
```

Thanks for reading upto here, if you like my documentation, you can give this repo and star, or share this repo which your team who might get benefit from my content.

If you find some issues on my concepts, my docs, my code, or my YouTube you can create an issue in this GitHub repo.




  


