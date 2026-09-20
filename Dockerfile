# Dockerfile for SistemaScuolaLingue Spring Boot Application
# Multi-stage build for smaller image size

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Create a non-root user for security
RUN addgroup -g 1001 -S javauser && \
    adduser -S -u 1001 -S javauser -G javauser
USER 1001:1001

# Expose the port the application runs on
EXPOSE 8080

# Set environment variables with defaults
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]