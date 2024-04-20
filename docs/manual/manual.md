# UI Tests

Use OpenAPI/Swagger to crate a payment and doenload the Proof: [http://localhost:8093/ebics/swagger-ui/?url=/ebics/v2/api-do](http://localhost:8093/ebics/swagger-ui/?url=/ebics/v2/api-docs/)

User: 
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

Replace XYZ with the value of recieptUrl to download the proof:  
http://localhost:8093/ebics/receipt_XYZ.json
