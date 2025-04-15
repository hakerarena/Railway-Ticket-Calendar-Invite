# ===== Stage 1: Build the JAR =====
FROM eclipse-temurin:24-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and give it execute permissions
COPY mvnw .
RUN chmod +x mvnw

# Copy wrapper directory
COPY .mvn .mvn

# Copy pom.xml and download dependencies first
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# Copy the rest of the app source code
COPY src ./src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# ===== Stage 2: Run the app =====
FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copy the built JAR
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]

