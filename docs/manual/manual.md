# UI Tests

Use Swagger for API call: 

## Create a payment

![Click Endpoint ](1.png)
![Try It ](2.png)
![Change Amount ](3.png)

Hit Execute. 

## Generate Dailay Statement 


![Click Endpoint ](4.png)
![Try It ](5.png)
![Change Amount ](6.png)

Note the receiptUrl for later download: 
![Hit Execute ](7.png)

## Download the proof

Add the value of recieptUrl to 'http://localhost:8093/ebics/':

http://localhost:8093/ebics/receipt_XXXXXXXXXXXX.json
