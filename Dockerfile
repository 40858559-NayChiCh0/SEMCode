FROM eclipse-temurin:25
COPY ./target/SEMCode-0.1.0.2.jar /tmp/SEMCode.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "SEMCode.jar"]