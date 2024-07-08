# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/document-processor-0.0.1-SNAPSHOT.jar /app/document-processor-0.0.1-SNAPSHOT.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "document-processor-0.0.1-SNAPSHOT.jar"]
