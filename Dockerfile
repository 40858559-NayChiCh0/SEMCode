FROM eclipse-temurin:25
COPY ./target/SEMCode-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "SEMCode-1.0-SNAPSHOT-jar-with-dependencies.jar"]