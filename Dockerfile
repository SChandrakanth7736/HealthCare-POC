# ── Stage 1: Build the JAR ────────────────────────────────────────────────────
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# Copy dependency manifests first for better layer caching
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/

# Copy source and build (skip tests – handled in CI)
COPY src/ src/
RUN sed -i 's/\r$//' gradlew 2>/dev/null || true && \
    gradle build -x test --no-daemon

# ── Stage 2: Run the application ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/HealthCare-POC-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
