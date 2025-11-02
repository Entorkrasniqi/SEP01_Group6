pipeline {
    agent any
    tools {
        maven 'MAVEN3'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'maven_test', url: 'https://github.com/Entorkrasniqi/SEP01_Group6.git', changelog: false, poll: false
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }

        stage('Coverage Report') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('Publish Coverage') {
            steps {
                jacoco()
            }
        }
    }
}
