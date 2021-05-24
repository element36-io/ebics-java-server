# Local build and debugging commands - you might want to remove sudo if not needed
# sudo docker build . -t ebics; 
# sudo docker run ebics -p 8093
# sudo docker run ebics -cp "ebics-cli-1.2.jar:lib/*" org.kopi.ebics.client.EbicsClient --help
# sudo docker run -it --entrypoint sh ebics
# sudo docker run -v $HOME/ebics:/root/ebics ebics -cp "ebics-cli-1.2.jar:lib/*" org.kopi.ebics.client.EbicsClient --sta -o /root/ebics/out sta.txt

FROM gradle:6-jdk8-hotspot as build

# build ebics-client jar and server jars;  
RUN mkdir /app
COPY build.gradle /app
COPY gradle.properties /app
COPY settings.gradle /app
COPY ./src /app/src
# disable git info
ENV GENERATE_GIT_PROPERTIES="false"
WORKDIR /app
RUN gradle clean build bootJar -DGENERATE_GIT_PROPERTIES=$GENERATE_GIT_PROPERTIES
#create runtime for jars
FROM openjdk:8-alpine as runtime
RUN mkdir /app
RUN mkdir /app/lib
WORKDIR /app
COPY --from=build /app/*.jar /app/
COPY --from=build /app/lib/*.jar /app/lib/
COPY --from=build /app/build/libs/app-1.0.0.jar /app
#remove version form jar files in container and note the used version
RUN FN=`(ls app-*.jar | head -1)`;  echo $FN; mv $FN ebics-service.jar; touch $FN.version
RUN FN=`(ls ebics-*.jar | head -1)`;  echo $FN;  mv $FN ebics-cli.jar;  touch $FN.version
#see application.yml
EXPOSE 8093
ENTRYPOINT ["java"]
CMD ["-jar", "ebics-service.jar" ]