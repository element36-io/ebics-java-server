# Build locally with docker

Clone repos which are necessary into the parent directory of ebics-java-service. 


    git clone git@github.com:element36-io/LibEuFin.git
    cd LibEuFin
    git checkout hyperfridge
    
Clone the code which creates STARK proofs: 

    cd ..
    git clone git@github.com:element36-io/hyperfridge-r0.git


Build locally - note speciality for Linux vs. MacOs may take several hours: 

    cd ..
    cd ebics-java-service
    docker compose -f docker-compose-build.yml -e DOCKERFILE:DockerfileLinux build

On MacOs:

    docker compose -f docker-compose-build.yml -e DOCKERFILE:DockerfileMaxOs build
    

Start container

    docker compouse up -d
    docker compose logs



