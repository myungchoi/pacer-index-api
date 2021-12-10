#Build the Maven project
FROM openjdk as builder
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN ./mvnw clean package

#Build the Tomcat container
FROM openjdk
#RUN apk update
#RUN apk add zip

# Copy fhirFilter war file to webapps.
COPY --from=builder /usr/src/app/target/pacer-index-api.jar pacer-index-api.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/pacer-index-api.jar"]
