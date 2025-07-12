# ---------- Stage 1: Build the application ----------
FROM maven:3.9.6-eclipse-temurin-22 AS build

# Install dependencies for Batik (required at build for SVG/font rendering)
RUN apt-get update && \
    apt-get install -y libfreetype6 fonts-dejavu-core && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy everything into the container
COPY . .

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ---------- Stage 2: Create the runtime image ----------
FROM openjdk:22-jdk-slim

# ✅ Install Batik runtime dependencies again
RUN apt-get update && \
    apt-get install -y libfreetype6 fonts-dejavu-core && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/SocialMediaApp-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 5455

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
