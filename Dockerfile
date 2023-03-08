#Build the Maven project
FROM openjdk:17-jdk as builder
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN ./mvnw clean package

#Build the Tomcat container
FROM openjdk:17-jdk

# Copy fhirFilter war file to webapps.
COPY --from=builder /usr/src/app/target/pacer-index-api.jar pacer-index-api.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/pacer-index-api.jar"]
