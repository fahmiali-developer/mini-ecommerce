# --- Stage 1: Build JAR pakai Maven + JDK 8 ---
FROM maven:3.8.8-eclipse-temurin-8 AS builder
WORKDIR /app

# Copy pom.xml dulu agar dependency ter-cache selama belum berubah
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code dan build JAR (skip test dulu di build image jika test butuh DB aktif)
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Stage 2: Runtime image ---
FROM eclipse-temurin:8-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]