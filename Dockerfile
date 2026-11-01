FROM eclipse-temurin:25
WORKDIR /tmp
COPY ./target/devops.jar /tmp/devops.jar
ENTRYPOINT ["java", "-jar", "/tmp/devops.jar", "db:3306", "30000"]
