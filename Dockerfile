FROM openjdk:8-jdk

COPY . /2048
WORKDIR /2048

RUN ./gradlew clean test shadowJar

FROM openjdk:8-jre-alpine

COPY --from=0 /2048/build/libs/2048-all.jar /

WORKDIR /

CMD java -Xmx30m -jar 2048-all.jar
