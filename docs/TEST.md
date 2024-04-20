# Tests


## Run unit tests 

![Coverage](../.github/badges/jacoco.svg)

Run tests for the ebics-java-client on linux - it mounts sources into a gradle docker container with java and the maven build tool - so you do not need to install java or gradle on your local machine:

    git clone  https://github.com/element36-io/ebics-java-service.git
    # or git clone git@github.com:element36-io/ebics-java-service.git

    cd ebics-java-service
    # we test on hyperfridge branch
    git checkout hyperfridge
    docker run -it -v $PWD:/app -w /app  gradle:6-jdk11 gradle clean test


On your host machine, test results are stored `./build/reports/tests/test/index.html`, test documents are stored in `./out`. With minimum Java 8 and Maven run tests on your host machine with `gradle test`, again see `./build/reports/tests/test/index.html` for test results.

For test coverage: `./build/reports/jacoco/test/html/index.html`.
Test for vulnerabilities `gradle dependencyCheckAggregate`- see report in `./build/reports`.

If you are interested in the Ebics Client implementation as well, look [here](https://github.com/element36-io/ebics-java-client/blob/master/README.md).


# Run and test with Docker 

This starts several docker images: ebics-java-service  => libeufin => Postgres. We use 'watchdog.sh' from the hyperfridge image to scan directory for new banking documents which triggers generation of STARK proofs.  

    
    docker compose pull
    docker compose up -d
    # optional
    docker compose logs -f

Startup will take some time - up to 3 o5 minutes. 
You should be able to [open Swagger](http://localhost:8093/ebics/swagger-ui/?url=/ebics/v2/api-docs/) and log into [banking bankend](manual/manual.md) which we will look at later.

We tested on MacOs. In case this is not working, you may 

## Test API and download ZK proof

First create a Payment on the banking backend:  

    curl -X 'POST' \
        'http://localhost:8093/ebics/api-v1/createOrder' \
        -H 'accept: */*' \
        -H 'Content-Type: application/json' \
        -d '{
            "amount": "123",
            "clearingSystemMemberId": "HYPLCH22XXX",
            "currency": "EUR",
            "msgId": "emtpy",
            "nationalPayment": true,
            "ourReference": "empty",
            "pmtInfId": "empty",
            "purpose": "0x9A0cab4250613cb8437F06ecdEc64F4644Df4D87",
            "receipientBankName": "Hypi Lenzburg AG",
            "receipientCity": "Baar",
            "receipientCountry": "CH",
            "receipientIban": "CH1230116000289537313",
            "receipientName": "element36 AG",
            "receipientStreet": "Bahnmatt",
            "receipientStreetNr": "25",
            "receipientZip": "6340",
            "sourceBic": "HYPLCH22XXX",
            "sourceIban": "CH2108307000289537320"
        }'

Download daily statement which should inluce prior payment and the STARK: 

    curl -X 'GET' \
        'http://localhost:8093/ebics/api-v1/bankstatements' \
        -H 'accept: */*' -o result.json

Extract the filename of the proof and download it: 

    PROOF=$(cat result.json | grep \
    -o '"receiptUrl":"[^"]*"' | cut -d'"' -f4)
    wget "http://localhost:8093/ebics/$PROOF" -O receipt.json

Verify the proof with the verifier: 

    # we need the image id and the receipt
    imageid=$(docker run fridge cat /app/IMAGE_ID.hex)
    docker cp receipt.json fridge:/app/receipt.json 
    docker exec -it -e RISC0_DEV_MODE=true fridge verifier \
        verify --imageid-hex="$imageid" --proof-json="/app/receipt.json"


## Test API manually with Swagger

Open [Swagger](http://localhost:8093/ebics/swagger-ui/?url=/ebics/v2/api-docs/) in your
browser and test the API and follow instructions [here](manual/manual.md) for a manual test.


## Login to simulated banking backend UI

Open [LibFinEu](http://localhost:3000) in your
browser with 'foo' and 'superpassword'. 

Go to [Activity](http://localhost:3000/activity) and select 'CH2108307000289537320' to see the transations you created before. 

Note that this is an external component which also can be used to connect to any bank supporting EBICS. It shows that the protocoll is used in a standard way. 
