FROM eclipse-temurin:25
COPY ./target/classes/com/napier /tmp/com
WORKDIR /tmp
ENTRYPOINT ["java", "com.napier.Main"]