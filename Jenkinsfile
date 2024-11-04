pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
    }
    environment {
        SONARQUBE_SERVER_URL = 'http://localhost:9000'
        SONARQUBE_PROJECT_KEY = 'Security_Pipeline'
        DOCKER_IMAGE = 'java_project'     
        WORKSPACE_DIR = "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\Security_Pipeline"
        SONAR_TOKEN = 'sqp_bff11fcbb728a813c946e71554523b989cd4d36d' // Updated token
        SONARQUBE_REPORT_PATH = "${WORKSPACE_DIR}\\reports\\sonarqube-report.txt"
        RECIPIENT_EMAIL = 'rohitjaiswalrj32@gmail.com, shivamahuja056@gmail.com'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', 
                          branches: [[name: '*/main']], 
                          userRemoteConfigs: [[url: 'https://github.com/rohitjaiswalrj32/Java-Project.git']]
                ])
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube_Server') {
                    bat '''
                        mvn clean verify sonar:sonar ^
                            -Dsonar.projectKey=Security_Pipeline ^
                            -Dsonar.projectName=Security_Pipeline ^
                            -Dsonar.host.url=http://localhost:9000 ^
                            -Dsonar.token=sqp_bff11fcbb728a813c946e71554523b989cd4d36d ^
                            -X
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${env.BUILD_NUMBER}")
                }
            }
        }

        post {
        always {
            // Send email with SonarQube report attached
            emailext(
                to: "${RECIPIENT_EMAILS}",
                subject: "Jenkins Job: ${env.JOB_NAME} Build #${env.BUILD_NUMBER} Report",
                body: """
                    Hello,

                    Attached is the SonarQube report for the build.

                    Build Details:
                    - Project: ${env.JOB_NAME}
                    - Build Number: ${env.BUILD_NUMBER}
                    - SonarQube Report Path: ${SONARQUBE_REPORT_PATH}

                    Regards,
                    Jenkins
                """,
                attachmentsPattern: "${SONARQUBE_REPORT_PATH}"
            )
        }
        failure {
            // Send an email if the build fails
            emailext (
                subject: "Jenkins Build Failed: Issues Found in Project",
                body: "The build failed. Please review the SonarQube report for details.",
                attachmentsPattern: "${SONARQUBE_REPORT_PATH}",
                to: "${RECIPIENT_EMAILS}"
            )
        }
        unstable {
            // Send an email if the build is unstable
            emailext (
                subject: "Jenkins Build Unstable: Issues Found in Project",
                body: "The build is unstable. Please review the SonarQube report for details.",
                attachmentsPattern: "${SONARQUBE_REPORT_PATH}",
                to: "${RECIPIENT_EMAILS}"
            )
        }
        success {
            // Send an email if the build succeeds
            emailext (
                subject: "Jenkins Build Succeeded",
                body: "The build completed successfully. Please find the SonarQube report for reference.",
                attachmentsPattern: "${SONARQUBE_REPORT_PATH}",
                to: "${RECIPIENT_EMAILS}"
            )
        }
    }
