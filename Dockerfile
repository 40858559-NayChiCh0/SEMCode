FROM eclipse-temurin:25
WORKDIR /tmp
COPY ./target/*-jar-with-dependencies.jar /tmp/app.jar
ENTRYPOINT ["java", "-jar", "/tmp/app.jar"]