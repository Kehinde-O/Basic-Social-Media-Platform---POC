#!/bin/bash

# Social Media Platform Startup Script
# This script includes JVM arguments to handle Java module system warnings

echo "Starting Social Media Platform..."
echo "=================================="

# Set JVM arguments to handle native access warnings
export MAVEN_OPTS="--enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED --add-opens java.desktop/java.awt.font=ALL-UNNAMED"

# Run the application
mvn spring-boot:run

echo "Application stopped."
