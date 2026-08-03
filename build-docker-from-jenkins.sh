#!/bin/bash

# Script to download latest JAR from Jenkins and build Docker image
# This script should be run on a machine with Docker installed

# Configuration
JENKINS_URL="http://localhost:8080"
JOB_NAME="JenkinsProjectIntegeration-Pipeline"
DOCKER_IMAGE_NAME="springboot-app"
DOCKER_IMAGE_TAG="latest"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Building Docker Image from Jenkins Build ===${NC}"

# Get the latest build number
echo "Fetching latest build number from Jenkins..."
LATEST_BUILD=$(curl -s "${JENKINS_URL}/job/${JOB_NAME}/lastBuild/buildNumber")

if [ -z "$LATEST_BUILD" ]; then
    echo -e "${RED}Error: Could not fetch build number from Jenkins${NC}"
    echo "Make sure Jenkins is running and the job exists"
    exit 1
fi

echo -e "${GREEN}Latest build number: ${LATEST_BUILD}${NC}"

# Download the JAR file
echo "Downloading JAR from Jenkins..."
JAR_URL="${JENKINS_URL}/job/${JOB_NAME}/${LATEST_BUILD}/artifact/target/*.jar"
DOWNLOAD_DIR="./jenkins-artifacts"
mkdir -p "$DOWNLOAD_DIR"

# Download the artifact
curl -s -o "${DOWNLOAD_DIR}/app.jar" "${JENKINS_URL}/job/${JOB_NAME}/${LATEST_BUILD}/artifact/target/DockerProjectIntegeration-0.0.1-SNAPSHOT.jar"

if [ ! -f "${DOWNLOAD_DIR}/app.jar" ]; then
    echo -e "${RED}Error: Failed to download JAR file${NC}"
    echo "URL: ${JENKINS_URL}/job/${JOB_NAME}/${LATEST_BUILD}/artifact/target/DockerProjectIntegeration-0.0.1-SNAPSHOT.jar"
    exit 1
fi

echo -e "${GREEN}JAR downloaded successfully${NC}"

# Build Docker image using the downloaded JAR
echo "Building Docker image..."
docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -f Dockerfile.jenkins .

if [ $? -eq 0 ]; then
    echo -e "${GREEN}=== Docker image built successfully ===${NC}"
    echo "Image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
    echo "Build: Jenkins build #${LATEST_BUILD}"
    
    # Cleanup
    rm -rf "$DOWNLOAD_DIR"
    
    echo ""
    echo "To run the container:"
    echo "docker run -p 8080:8080 ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
else
    echo -e "${RED}Error: Docker build failed${NC}"
    exit 1
fi
