FROM eclipse-temurin:21-jdk

RUN useradd -ms /bin/bash appuser

WORKDIR /app

COPY target/*.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]