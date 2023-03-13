FROM openjdk:18-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/service
ADD tempo.jar /usr/local/service/tempo.jar
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 8080
CMD ./run.sh