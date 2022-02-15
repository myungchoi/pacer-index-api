#Build the Maven project
FROM openjdk:8-jdk-alpine as builder
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN ./mvnw clean package

#Build the Tomcat container
FROM openjdk:8-jdk-alpine
RUN apk update
RUN apk add zip

#ENV BASIC_AUTH_USER=user
#ENV BASIC_AUTH_PASSWORD=password

# Copy fhirFilter war file to webapps.
COPY --from=builder /usr/src/app/target/pacer-index-api.jar pacer-index-api.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/pacer-index-api.jar"]
