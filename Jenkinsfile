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
                    // Define the output report file path
                    def trivyReportPath = "${WORKSPACE_DIR}/trivy-report.json"

                    // Run the Trivy scan and save the report
                    bat '''
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v C:/ProgramData/Jenkins/.jenkins/workspace/java-sonar:/root/.cache/ -v C:/ProgramData/Jenkins/.jenkins/workspace/trivy-db:/root/.trivy aquasec/trivy image --scanners vuln --timeout 15m --format json -o ${trivyReportPath} %DOCKER_IMAGE%:%BUILD_NUMBER%
                    '''
                    
                    // Output message
                    echo "Trivy scan report generated at: ${trivyReportPath}"
                }
            }
        }

        stage('Report Generation') {
            steps {
                script {
                    
                    //SonarQube_Report
                    def reportDir = "${env.WORKSPACE}/reports" // Directory for reports
                    
                    // Use bat to create the directory on Windows
                    bat "mkdir \"${reportDir}\"" // Create the directory if it doesn't exist
                    
                    def sonarReportUrl = "${SONARQUBE_SERVER_URL}/api/project_analyses/search?project=${SONARQUBE_PROJECT_KEY}"
                    
                    // Use bat for curl command on Windows, ensure correct syntax
                    def response = bat(script: "curl -s -u ${SONAR_TOKEN}: \"${sonarReportUrl}\"", returnStdout: true)
                    
                    def reportFilePath = "${reportDir}/sonarqube-report.txt" // Define the report file path
                    
                    writeFile(file: reportFilePath, text: response) // Save the report
                    echo "SonarQube Analysis Report saved at: ${reportFilePath}"


                    //Trivy_Report
                    def trivyReportFilePath = "${reportDir}/trivy-report.json" // Define the Trivy report file path
                    writeFile(file: trivyReportFilePath, text: readFile(trivyReportPath)) // Save the Trivy report
                    echo "Trivy Scan Report saved at: ${trivyReportFilePath}"
                }
            }
        }
        
        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'reports/*', allowEmptyArchive: true
                echo "Archived reports successfully."
            }
        }
    }
}
