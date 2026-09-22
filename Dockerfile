FROM eclipse-temurin:25
WORKDIR /tmp
COPY ./target/SEMCode-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp/SEMCode.jar
ENTRYPOINT ["java", "-jar", "SEMCode.jar"]