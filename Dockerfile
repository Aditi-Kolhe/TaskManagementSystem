# Start with the official Java image as the base
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven/Gradle build output JAR to the container
COPY target/your-app.jar app.jar

# Expose the port your application runs on
EXPOSE 5454

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
