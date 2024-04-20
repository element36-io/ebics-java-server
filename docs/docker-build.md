# Build locally with docker

Clone repos which are necessary into the parent directory of ebics-java-service. 


    git clone git@github.com:element36-io/LibEuFin.git
    cd LibEuFin
    git checkout hyperfridge
    # needed by the LibEuFin framework
    ./bootstrap
    
Clone the code which creates STARK proofs: 

    cd ..
    git clone git@github.com:element36-io/hyperfridge-r0.git


Build locally (about 1 hour) - note speciality for Linux vs. MacOs:

    cd ..
    cd ebics-java-service
    docker compose -f docker-compose-build.yml build

On MacOs:

    docker compose -f docker-compose-build.yml build --build-arg DOCKERFILE="DockerfileMaxOs"

Start container

    docker compouse up -d
    docker compose logs



