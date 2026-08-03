# Docker Image Guide

## Current Status

**Docker is NOT available in your Jenkins environment.** The Jenkins pipeline currently:
- Builds the JAR file
- Runs tests with H2 in-memory database
- Archives the JAR artifact
- Does NOT build Docker images

## Building Docker Image Locally

Since Docker isn't in Jenkins, you can build the Docker image on your local machine:

### Prerequisites
- Docker Desktop installed and running
- MySQL database running (if you want to run the application with MySQL)

### Build Commands

```bash
# Navigate to project directory
cd C:\Users\91934\OneDrive\Desktop\DockerProjectIntegration\DockerProjectIntegeration

# Build the Docker image
docker build -t springboot-app:latest .
```

### Run the Docker Image

```bash
# Run with default configuration (requires MySQL at localhost:3306)
docker run -p 8080:8080 springboot-app:latest

# Run with custom environment variables
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/crud_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  springboot-app:latest
```

## Where Docker Images Are Stored

### Local Docker Images
After building, your image is stored in your local Docker registry:
```bash
# List all local images
docker images

# You should see something like:
# springboot-app   latest   abc123def456   2 minutes ago   500MB
```

### Accessing the Image
- **Local access**: Available on your machine via `docker run springboot-app:latest`
- **Remote access**: Requires pushing to a Docker registry (Docker Hub, AWS ECR, etc.)

## Pushing to Docker Registry

To make the image accessible from other machines:

### Docker Hub
```bash
# Tag the image
docker tag springboot-app:latest your-dockerhub-username/springboot-app:latest

# Login to Docker Hub
docker login

# Push the image
docker push your-dockerhub-username/springboot-app:latest
```

### Pull from Docker Hub
```bash
docker pull your-dockerhub-username/springboot-app:latest
docker run -p 8080:8080 your-dockerhub-username/springboot-app:latest
```

## Adding Docker to Jenkins

If you want to enable Docker builds in Jenkins:

### Option 1: Install Docker on Jenkins Server
1. SSH into your Jenkins server
2. Install Docker: https://docs.docker.com/engine/install/
3. Add Jenkins user to docker group:
   ```bash
   sudo usermod -aG docker jenkins
   ```
4. Restart Jenkins
5. Update Jenkinsfile to include Docker stages again

### Option 2: Use Docker-in-Docker (DinD)
Configure Jenkins agent with Docker-in-Docker support for containerized builds.

### Option 3: Use Kubernetes
If Jenkins runs on Kubernetes, use Kubernetes pods for Docker builds.

## Database Configuration

### For Local Development (MySQL)
The application uses MySQL by default. Ensure MySQL is running:
```bash
# Using Docker
docker run -d -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=Venky@314 \
  -e MYSQL_DATABASE=crud_db \
  mysql:8.0
```

### For Testing (H2)
Tests now use H2 in-memory database - no external database required.

## Accessing the Application

After running the Docker image:
- Application URL: http://localhost:8080
- H2 Console (if enabled): http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:testdb
  - Username: sa
  - Password: (empty)

## Summary

- **Jenkins Pipeline**: Currently builds JAR and runs tests (no Docker)
- **Local Docker**: Build image manually with `docker build -t springboot-app:latest .`
- **Image Location**: Stored in local Docker registry (`docker images`)
- **Remote Access**: Push to Docker Hub or other registry
- **Database**: Production uses MySQL, tests use H2
