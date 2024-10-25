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
        TRIVY_CACHE = "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\Security_Pipeline\\trivy-cache"
        WORKSPACE_DIR = "C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\Security_Pipeline"
        SONAR_TOKEN = 'sqp_bff11fcbb728a813c946e71554523b989cd4d36d' // Updated token
        SONARQUBE_REPORT_PATH = "${WORKSPACE_DIR}\\reports\\sonarqube-report.txt"
        TRIVY_REPORT_PATH = "${WORKSPACE_DIR}\\trivy-report.txt"
        RECIPIENT_EMAIL = 'rohitjaiswalrj32@gmail.com'
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

        stage('Trivy Scan') {
            steps {
                script {
                    

                    // Run the Trivy scan and save the report
                    bat '''
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v C:/ProgramData/Jenkins/.jenkins/workspace/java-sonar:/root/.cache/ -v C:/ProgramData/Jenkins/.jenkins/workspace/trivy-db:/root/.trivy aquasec/trivy image --scanners vuln --timeout 15m --format table -o ${TRIVY_REPORT_PATH} %DOCKER_IMAGE%:%BUILD_NUMBER%
                    '''
                    
                    // Output message
                    echo "Trivy scan report generated at: ${trivyReportPath}"
                }
            }
        }

        post {
        always {
            // Send email with both SonarQube and Trivy reports attached
            emailext(
                to: "${RECIPIENT_EMAIL}",
                subject: "Jenkins Job: ${env.JOB_NAME} Build #${env.BUILD_NUMBER} Reports",
                body: """
                    Hello,

                    Attached are the SonarQube and Trivy scan reports for the build.

                    Build Details:
                    - Project: ${env.JOB_NAME}
                    - Build Number: ${env.BUILD_NUMBER}
                    - SonarQube Report Path: ${SONARQUBE_REPORT_PATH}
                    - Trivy Report Path: ${TRIVY_REPORT_PATH}

                    Regards,
                    Jenkins
                """,
                attachmentsPattern: "${SONARQUBE_REPORT_PATH}, ${TRIVY_REPORT_PATH}"
            )
        }
    }
}
}
