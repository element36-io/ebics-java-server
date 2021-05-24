## Overview

This project wraps a [java-based command-line tool](https://github.com/element36-io/ebics-java-client)
into a REST service. It can connect with your bank using 
[Ebics](https://en.wikipedia.org/wiki/Electronic_Banking_Internet_Communication_Standard) 
standard.

Setting it up successfully means, that you can 
- Read daily statements including account balance and transactions of multiple accounts
- Trigger payments from your bank account(s)

## Start the service & take a look at the API


    docker run -p 8093:8093 e36io/ebics-service 


Open [Swagger on localhost:8093](http://localhost:8093/ebics/swagger-ui/?url=/ebics/v2/api-docs/) in your 
browser and test the `simulate` service. For other APIs you need to set up and connect to your
banks Ebics API. 

## Setup
 
Ebics (Electronic Banking Internet Communication Standard) defines the protocol and a series of document to access and exchange banking data. 
You need to talk to your bank first and request to provide access to the Ebics interface.
Alternatively you can ask about ISO20022 or an interface to transfer Camt.053 and Pain.001 or Pain.002
files, which should be well known among banks. Most banks offer the service for free. 

A crucial part to understand of Ebics is the key-exchange- roughly it works like this:  

- Generate keypair and send public to bank with the ebics client
- Bank sends their client specific key via a physically printed letter to you
- Add this key to the client 
- All configurations for the setup are done in one specific file: $HOME/ebics/ebics.txt

 Check [ebics.org](https://www.ebics.org/en/home) for details. 

## Supported banks and national flavours

Ebics standard was initiated by banks German/French partnership later adopted by Austria and Switzerland
and also by Banks across Europe - you will find many bank supporting Ebics, a general list of all
banks supporting Ebics was not found. 

There are national flavours regarding the data on bank transfers and bank statements, which are
represented by national changes of the original Ebics XML documents. For example Switzerland: 

- In addition to a reference field (e.g. invoice 1234) you may use a dedicated
  "reference number" on payment slips  which you don't find in other countries
- Account statements, status reports are ZIP files available through separate Ebics commands. 
  Generally it is easy to add new commands to the `ebics-java-client` library. 

### Switzerland

A selection of test interfaces for Ebics and test environments in Switzerland: 

| Bank  | URL for testing/validating | more info |
|---|---|---|
| SIX Group | [validation](https://validation.iso-payments.ch/)  | [Url](http://www.six-interbank-clearing.com/de/home/standardization/iso-payments/customer-bank/implementation-guidelines.html) |
| Credit Suisse  | [test-API](https://credit-suisse.com/iso20022test) | [help](https://iso20022test.credit-suisse.com/help) |
| PostFinance | [register](https://isotest.postfinance.ch/corporates/) ||
| Raiffeisen  | [test-API](http://raiffeisen.ch/testbank) ||
| UBS  | [register](https://ubs-paymentstandards.ch/login) | [info](https://www.ubs.com/ch/de/swissbank/unternehmen/zahlungsverkehr/harmonisierung/testplattform-iso-20022.html) ||
| ZKB  | [test-platform](https://testplattform.zkb.ch/) | [help](https://testplattform.zkb.ch/help) |

### Other countries

Besides France, Germany, Austria and Switzerland many other countries are covered with an Ebics- Service. 

Non-exhaustive examples of European banks providing Ebics information:  

- [BNP Paribas](https://cashmanagement.bnpparibas.com/our-solutions/solution/global-ebics)
- [Santander Cash Nexus](https://www.santandercashnexus.com/information_en.html)
- [DZ Bank](https://firmenkunden.dzbank.de/content/firmenkunden/de/homepage/leistungen/Zahlungsverkehr/zugang_zum_konto/ebics.html)
- [Sparkasse](https://www.sparkasse.de/unsere-loesungen/firmenkunden/electronic-banking/online-banking-ebics.html)
- [Bank Austria](https://www.bankaustria.at/files/EBServices_23082013_final.pdf)
- [Hypo Vorarlberg](https://www.hypovbg.at/firmenkunden/digital-banking/sicherheit/ebics)

## Adapt to national flavours 

The setup process does not need to be adapted. With the homogenization of European 
banking interfaces (SEPA, ISO20022) you may use a common and minimal 
set information for working with bank interfaces which is reflected by the REST interface of this
project. Anyway you might need to adapt to local flavour of your country or your bank, which is 
can be done in the `io.element36.cash36.ebics.strategy.*` package. Examples: 


- Adapt conversion between Ebics-XML documents: E.g. `io.element36.cash36.ebics.strategy.implStatementCamt53_001` for 
mapping of daily statements to Json Response. 
- Add new commands to ebics-java-cli: look for `org.kopi.ebics.interfaces.OrderType` which hold commands available at the command line but
 which are also directly transferred to the Ebics server as commands. Look for national mapping tables if you face this issue. E.g. 
 [Switzerland](https://www.six-group.com/dam/download/banking-services/interbank-clearing/en/standardization/ebics/mapping-table.pdf)
 and [Austria](https://www.stuzza.at/de/download/ebics/418-btf-mappingtabelle-at-v20210506.html)

You may check out www.ebics.ch, www.ebics.de. www.ebics.at, www.ebics.org. 

## Kudos and references: 

The project is forked form [Ebics Java Client](https://github.com/uwemaurer/ebics-java-client/), 
which was based on a [sourceforge project](https://sourceforge.net/p/ebics/). 

Main differences with this fork from ebics-java-client form uwemaurrer: 

- Fixed vulnerabilities of bouncycastle and log4j
- Support for new commands used in Switzerland.
- Jar file in maven central repository.
- Docker image is automatically built from master branch on Dockerhub. 
- Changed documentation for usage with docker

