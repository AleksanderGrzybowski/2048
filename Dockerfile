FROM java:8

COPY build/libs/2048-all.jar /

WORKDIR /

CMD java -jar 2048-all.jar
