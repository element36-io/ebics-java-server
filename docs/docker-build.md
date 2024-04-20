# Build locally with docker

Clone repos which are necessary into the parent directory of ebics-java-service. 


    git clone git@github.com:element36-io/LibEuFin.git
    cd LibEuFin
    git checkout hyperfridge
    
    cd ..
    git git@github.com:element36-io/hyperfridge-r0.git


Build locally, may take several hours: 

    cd ..
    cd ebics-java-service
    docker compose -f docker-compose-build.yml build


Start container

    docker compouse up -d
    docker compose logs

    

