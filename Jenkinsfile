pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {


        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/LankeVenkatesh-Java-Developer/DockerProjectWithIntegration.git'
            }
        }

        stage('Build') {
            steps {
                dir('DockerProjectIntegeration') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package'
                }
            }
        }

        stage('Test') {
            steps {
                dir('DockerProjectIntegeration') {
                    sh './mvnw test'
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'DockerProjectIntegeration/target/*.jar'
            }
        }

        stage('Docker Build') {
            steps {
                dir('DockerProjectIntegeration') {
                    sh 'docker build -t springboot-app:${BUILD_NUMBER} .'
                }
            }
        }
    }

    post {

        always {
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
        }

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}