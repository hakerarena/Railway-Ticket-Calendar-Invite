# ===== Stage 1: Build the JAR =====
FROM eclipse-temurin:24-jdk-alpine AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (for Docker caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

# Now copy the rest of the source and build the app
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ===== Stage 2: Run the app =====
FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
