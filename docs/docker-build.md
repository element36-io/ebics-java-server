# Build locally with docker

Clone repos which are necessary into the parent directory of ebics-java-service. 


    git clone git@github.com:element36-io/LibEuFin.git
    cd LibEuFin
    git checkout hyperfridge
    # needed by the LibEuFin framework to initialize linked repos
    ./bootstrap
    
Clone the code which creates STARK proofs: 

    cd ..
    git clone git@github.com:element36-io/hyperfridge-r0.git


Build locally (about 1 hour) - note speciality for Linux vs. MacOs:

    cd ..
    cd ebics-java-service
    docker compose -f docker-compose-build.yml build

On MacOs - also make sure that Docker gets enough ressources: 

    DOCKERFILE=DockerfileMaxOs docker compose -f docker-compose-build.yml build

On a Appro Macbook PRO M3, the risc-zero built may be failing, whereas on older Macs it works.  Modify 'docker-compose-build.yml' to swith local build on and off, e.g. for hyperfridge look for:  

    hyperfridge: 
        container_name: fridge
        # image: e36io/hyperfridge-r0:latest
        build: 
        context: ../hyperfridge-r0

and change to: 

   hyperfridge: 
        container_name: fridge
        image: e36io/hyperfridge-r0:latest
        # build: 
        # context: ../hyperfridge-r0
    

Start container

    docker compose -f docker-compose-build.yml up -d
    docker compose -f docker-compose-build.yml logs -f



