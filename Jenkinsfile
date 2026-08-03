pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        DOCKER_IMAGE_NAME = "springboot-app"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvnw.cmd test'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                bat "docker build -t %DOCKER_IMAGE_NAME%:%DOCKER_TAG% ."
            }
        }

        stage('Docker Tag Latest') {
            steps {
                bat "docker tag %DOCKER_IMAGE_NAME%:%DOCKER_TAG% %DOCKER_IMAGE_NAME%:latest"
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            cleanWs()
        }

        success {
            echo "Build Successful"
            echo "Docker Image: ${env.DOCKER_IMAGE_NAME}:${env.DOCKER_TAG}"
        }

        failure {
            echo "Build Failed"
        }
    }
}