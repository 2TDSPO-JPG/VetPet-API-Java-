```dockerfile
# Etapa 1: build da aplicação
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

# Etapa 2: imagem final
FROM eclipse-temurin:21-jre

RUN useradd -ms /bin/bash appuser

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```
