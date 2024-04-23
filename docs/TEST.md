# Tests


## Clone repo and run unit tests 

Run tests for the ebics-java-client on linux or MacOs - it mounts sources into a gradle docker container with java and the maven build tool - so you do not need to install java or gradle on your local machine:

    git clone git@github.com:element36-io/ebics-java-service.git

    cd ebics-java-service
    # we test on hyperfridge branch
    git checkout hyperfridge
    docker run -it -v $PWD:/app -w /app  gradle:6-jdk11 gradle clean test


On your host machine, test results are stored `./build/reports/tests/test/index.html`, test artefacts (ebics files) are stored in `./out`. 

If you are interested in the Ebics Client implementation as well, look [here](https://github.com/element36-io/ebics-java-client/blob/master/README.md).
The banking backend is simulated with LibEuFin - look [here](https://github.com/element36-io/LibEuFin). 

# Run and test with Docker 

This starts several docker images: ebics-java-service uses APIs of libeufin which uses Postgres. We use 'watchdog.sh' from the hyperfridge image to scan directory for new banking documents which triggers generation of STARK proofs.  
    
    docker compose pull
    docker compose up -d
    # optional
    docker compose logs -f

Startup will may take a couple of minutes. You should be able to [open Swagger](http://localhost:8093/ebics/swagger-ui/?url=/ebics/v2/api-docs/) and log into [banking bankend](manual/manual.md) with 'foo' and 'superpassword'.

We tested on Linux and MacOs, with issues of building the R0 framework on a Macbook PRO M3 in a container. Anyway, starting containers with pre-made images should be fine. In case you want to build all images locally, check [here](docker-build.md). 

## Test API and download ZK proof

We use 'bash', make sure curl and wget are installed. Versions in comments (from MacOs) are just informative: 


    wget --version
    # GNU Wget 1.24.5 built on darwin23.2.0.
    curl --version
    # curl 8.4.0 (x86_64-apple-darwin23.0) libcurl/8.4.0 (SecureTransport) 
    # LibreSSL/3.3.6 zlib/1.2.12 nghttp2/1.58.0



First create a Payment on the banking backend. Expect HTTP status 200 but no output. 

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
    ls result.json


You should see the output of 'ls result.js'. 

Next we extract the filename of the proof and download it: 

    PROOF=$(cat result.json | grep \
    -o '"receiptUrl":"[^"]*"' | cut -d'"' -f4)
    wget "http://localhost:8093/ebics/$PROOF" -O receipt.json

You should see something like '‘receipt.json’ saved [10423/10423]' as output. 

Now verify the proof with the verifier: 

    # we need the image id and the receipt
    imageid=$(docker compose run hyperfridge cat /app/IMAGE_ID.hex)
    docker compose cp receipt.json hyperfridge:/app/receipt.json 
    docker compose exec -it -e RISC0_DEV_MODE=true hyperfridge verifier \
        verify --imageid-hex="$imageid" --proof-json="/app/receipt.json"

The output start with "Ok": 

    Ok(Commitment { hostinfo: "host:main", iban: "CH4308307000289537312", stmts: [] })

Remark: The empty array for 'stmts' is because we started to work on profing individual transactions. 
This is not part of the grant but we plan to include if it can be done withing reasonable time.   

## Test API manually with Swagger

Open [Swagger](http://localhost:8093/ebics/swagger-ui/?url=/ebics/v2/api-docs/) in your
browser and test the API and follow instructions [here](manual/manual.md) for a manual test.


## Login to simulated banking backend UI

Open [LibFinEu](http://localhost:3000) in your
browser with 'foo' and 'superpassword'. 

Go to [Activity](http://localhost:3000/activity) and select 'CH2108307000289537320' to see the transations you created before. 

Note that this is an external component which also can be used to connect to any bank supporting EBICS. It shows that the protocoll is used in a standard way. 
