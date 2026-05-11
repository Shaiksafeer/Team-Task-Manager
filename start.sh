#!/bin/bash

# Navigate to the backend directory
cd backend

# The JAR file path (based on pom.xml artifactId and version)
JAR_PATH="target/backend-0.0.1-SNAPSHOT.jar"

# Check if the JAR file exists, if not, build the project
if [ ! -f "$JAR_PATH" ]; then
    echo "JAR file not found at $JAR_PATH. Building the project..."
    # Ensure maven is installed or use ./mvnw if available
    if command -v mvn &> /dev/null; then
        mvn clean package -DskipTests
    else
        echo "Error: 'mvn' command not found. Please ensure Maven is installed."
        exit 1
    fi
fi

# Run the application
# We use the PORT environment variable provided by Railway, defaulting to 8080
echo "Starting the application on port ${PORT:-8080}..."
java -jar "$JAR_PATH" --server.port=${PORT:-8080}
