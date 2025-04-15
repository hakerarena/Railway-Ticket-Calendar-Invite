# Use a base JDK image
FROM eclipse-temurin:24-jdk-alpine

# Set working directory
WORKDIR /app

# Copy build files
COPY target/*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
