# ===== Stage 1: Cache dependencies =====
FROM eclipse-temurin:24-jdk-alpine AS dependencies

WORKDIR /app

COPY mvnw .
RUN chmod +x mvnw
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# ===== Stage 2: Build the JAR =====
FROM eclipse-temurin:24-jdk-alpine AS build

WORKDIR /app

COPY --from=dependencies /root/.m2 /root/.m2
COPY . .

# 🔥 Fix permission again after full source is copied
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

# ===== Stage 3: Run the app =====
FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
