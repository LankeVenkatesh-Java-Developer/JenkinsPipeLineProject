# Jenkins Pipeline Setup Guide

## Prerequisites

1. **Jenkins Installation**
   - Install Jenkins on your Windows machine
   - Ensure Jenkins is running and accessible (usually at http://localhost:8080)

2. **Required Jenkins Plugins**
   Install these plugins via Manage Jenkins → Manage Plugins:
   - Git Plugin
   - Pipeline Plugin (usually installed by default)
   - Docker Pipeline Plugin (optional, for advanced Docker operations)

3. **Required Tools on Jenkins Agent**
   - JDK 17 or later (configured in Jenkins Global Tool Configuration)
   - Maven 3.6+ (configured in Jenkins Global Tool Configuration)
   - Docker Desktop installed and running
   - Git

## Creating the Pipeline Job

1. **Open Jenkins Dashboard**
   - Navigate to http://localhost:8080

2. **Create New Item**
   - Click "New Item"
   - Enter a name (e.g., `DockerProjectIntegeration-Pipeline`)
   - Select "Pipeline" and click OK

3. **Configure Pipeline**
   - Under "Pipeline" section:
     - Definition: `Pipeline script from SCM`
     - SCM: `Git`
     - Repository URL: `https://github.com/LankeVenkatesh-Java-Developer/JenkinsPipeLineProject.git`
     - Branch: `main`
     - Script Path: `Jenkinsfile`

4. **Save Configuration**
   - Click "Apply" then "Save"

## Running the Pipeline

1. **Build Now**
   - Click "Build Now" on the job page
   - Click on the build number to view progress
   - Check "Console Output" for real-time logs

## Pipeline Stages

The pipeline executes the following stages:

1. **Checkout** - Clones the repository from GitHub
2. **Build** - Compiles the Spring Boot application using Maven
3. **Test** - Runs unit tests
4. **Archive** - Saves the JAR file as a build artifact
5. **Docker Build** - Builds a Docker image with tag `springboot-app:BUILD_NUMBER`
6. **Docker Tag Latest** - Tags the image as `springboot-app:latest`

## Post-Build Actions

- **Always**: Publishes test results and cleans workspace
- **Success**: Displays success message with Docker image tag
- **Failure**: Displays failure message

## Running the Docker Image

After successful pipeline execution:

```bash
# Run the container
docker run -p 8080:8080 springboot-app:latest

# Or with a specific build number
docker run -p 8080:8080 springboot-app:1
```

## Troubleshooting

### Docker Permission Issues
If you encounter Docker permission errors:
- Ensure Docker Desktop is running
- Run Jenkins as an administrator
- Or add the Jenkins user to the docker-users group

### Maven Build Failures
- Ensure Maven is configured in Jenkins Global Tool Configuration
- Check that `mvnw.cmd` has execute permissions (Windows usually handles this automatically)

### Git Connection Issues
- Verify the repository URL is correct
- Ensure you have proper credentials if the repository is private
- Configure Git credentials in Jenkins Credentials section

## Optional: Adding Docker Push Stage

To push the Docker image to a registry (Docker Hub, AWS ECR, etc.), add this stage after "Docker Tag Latest":

```groovy
stage('Docker Push') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            bat "docker login -u %DOCKER_USER% -p %DOCKER_PASS%"
            bat "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
            bat "docker push ${DOCKER_IMAGE_NAME}:latest"
        }
    }
}
```

Note: You'll need to create Docker Hub credentials in Jenkins first.


Follow these steps to get your ngrok Authtoken.

Step 1: Sign up or log in

Open:

ngrok Login / Sign Up

Create a free account if you don't already have one.

Step 2: Open the Authtoken page

Go to:

Your Authtoken Dashboard

or directly:

https://dashboard.ngrok.com/get-started/your-authtoken
Step 3: Copy your Authtoken

You'll see a page similar to:

Connect your account

Your Authtoken

***************************************
Copy

Click the Copy button.

Step 4: Use the Authtoken
If using the native ngrok executable

Run:

ngrok config add-authtoken YOUR_AUTHTOKEN
If using Docker

Pass it as an environment variable:

docker run --rm -it \
-e NGROK_AUTHTOKEN=YOUR_AUTHTOKEN \
ngrok/ngrok:latest \
http host.docker.internal:8080

Step 5: Verify

If authentication succeeds, you'll see output similar to:

Session Status      online
Account             Your Name (Free)
Forwarding          https://xxxx.ngrok-free.app -> http://host.docker.internal:8080
Based on your previous message

You already have:

Session Status    online
Forwarding
https://garter-outline-habitable.ngrok-free.dev

This means your Authtoken is already configured correctly. You do not need to configure it again. The tunnel is authenticated and running successfully.

The next step is to configure the GitHub webhook with:

https://garter-outline-habitable.ngrok-free.dev/github-webhook/

If you'd like, I can also walk you through GitHub → Jenkins webhook configuration with screenshots from start to finish.


