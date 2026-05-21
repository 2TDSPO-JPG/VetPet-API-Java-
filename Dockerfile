FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/javaspg-0.0.1-SNAPSHOT.jar app.jar

RUN useradd -m springuser

USER springuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]