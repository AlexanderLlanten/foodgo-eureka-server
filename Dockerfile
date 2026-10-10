# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp clean verify
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/target/foodgo-eureka-server-2.0.0.jar app.jar
USER 10001
EXPOSE 8761
HEALTHCHECK --interval=10s --timeout=5s --start-period=90s --retries=12 CMD curl --fail --silent http://127.0.0.1:8761/ > /dev/null || exit 1
ENTRYPOINT ["java","-jar","/app/app.jar"]
