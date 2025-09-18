# Multi-stage build for Spring Boot application
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Create app directory
WORKDIR /app

# Create uploads directory
RUN mkdir -p uploads data

# Copy the built JAR file
COPY --from=build /app/target/media-board-backend-1.0.0.jar app.jar

# Expose port
EXPOSE 10000

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=production
ENV PORT=10000

# Run the application
CMD ["java", "-jar", "app.jar"]
