// Requires the Workspace Cleanup Plugin to be installed before use
// https://jenkins.io/doc/pipeline/steps/ws-cleanup/
pipeline {
    agent { label 'master' }
    stages {
        stage('Build project artifacts') {
            steps {
                sh "./mvnw -B -DskipTests=true -T 1C clean package"
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', onlyIfSuccessful: true
            archiveArtifacts artifacts: 'configuration.yml', onlyIfSuccessful: true
            cleanWs cleanWhenFailure: false
        }
    }
}
