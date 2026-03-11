FROM eclipse-temurin:21-jdk-alpine AS builder
LABEL authors="Jeremy"
WORKDIR /workspace

COPY gradlew gradlew.bat* ./
COPY gradle/ gradle/
RUN chmod +x gradlew

COPY settings.gradle.kts ./
COPY build.gradle.kts ./
COPY simulation-core/build.gradle.kts simulation-core/
COPY simulation-api/build.gradle.kts simulation-api/

RUN ./gradlew :simulation-api:dependencies --no-daemon --quiet || true

COPY simulation-core/src simulation-core/src
COPY simulation-api/src simulation-api/src

RUN ./gradlew :simulation-api:bootJar --no-daemon -x test


FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=builder /workspace/simulation-api/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]