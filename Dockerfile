# Use official lightweight OpenJDK image
FROM openjdk:22-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy the JAR file into the container
COPY target/SocialMediaApp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
