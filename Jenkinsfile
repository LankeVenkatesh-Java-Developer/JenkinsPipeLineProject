pipeline {
agent any

```
options {
    timestamps()
    disableConcurrentBuilds()
}

stages {
    stage('Checkout') {
        steps {
            git branch: 'main', url: 'https://github.com/LankeVenkatesh-Java-Developer/JenkinsPipeLineProject.git'
        }
    }

    stage('Build') {
        steps {
            sh 'chmod +x mvnw'
            sh './mvnw clean package -DskipTests'
        }
    }

    stage('Test') {
        steps {
            sh './mvnw test'
        }
    }

    stage('Archive') {
        steps {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }

    stage('Docker Build') {
        steps {
            sh 'docker build -t springboot-app:${BUILD_NUMBER} .'
        }
    }
}

post {
    always {
        junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
    }

    success {
        echo 'Pipeline completed successfully.'
    }

    failure {
        echo 'Pipeline failed.'
    }
}
```

}
