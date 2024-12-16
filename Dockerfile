# Use an OpenJDK base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build folder into the container
COPY target/boot-board-0.0.1-SNAPSHOT.jar boot-board-0.0.1-SNAPSHOT.jar

# Expose the application port (e.g., 8080 for Spring Boot)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "boot-board-0.0.1-SNAPSHOT.jar"]


