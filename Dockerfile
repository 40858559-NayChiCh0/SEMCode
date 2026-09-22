FROM eclipse-temurin:25
WORKDIR /tmp
COPY ./target/*.jar /tmp/app.jar
ENTRYPOINT ["java", "-jar", "/tmp/app.jar"]