FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src src

RUN chmod +x mvnw && ./mvnw -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /workspace/target/quarkus-app /app/quarkus-app

EXPOSE 8080
ENV QUARKUS_HTTP_HOST=0.0.0.0
ENTRYPOINT ["java", "-jar", "/app/quarkus-app/quarkus-run.jar"]
