pipeline {
    agent any
    options {
        skipDefaultCheckout(true)
    }
    stages {
        stage('Obtener codigo') {
            steps {
                checkout scm
            }
        }
        stage('Probar con Maven') {
            steps {
                sh 'mvn -B test'
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
