# Build stage
FROM gradle:8.12-jdk21 AS build
WORKDIR /app

# Copy only necessary files for dependency resolution
COPY settings.gradle build.gradle gradle.properties ./
COPY authentication-service/build.gradle ./authentication-service/

# Download dependencies
RUN gradle :authentication-service:dependencies --no-daemon || true

# Copy source code
COPY authentication-service/src ./authentication-service/src

# Build the application
RUN gradle :authentication-service:bootJar --no-daemon

# Runtime stage
FROM amazoncorretto:21.0.5-alpine
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/authentication-service/build/libs/authentication-service.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/api/auth/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
