FROM eclipse-temurin:25
COPY ./target/classes/com /tmp/com
WORKDIR /tmp
ENTRYPOINT ["java", "com.napier.App"]
#ENTRYPOINT ["java", "-jar", "/app.jar"]