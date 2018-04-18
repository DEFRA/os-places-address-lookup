pipeline {
    agent { label 'master' }
    stages {
        stage('Build project artifacts') {
            steps {
                sh 'printenv'
                sh "./mvnw -B -DskipTests=true -T 1C clean package"
            }
        }
        post {
            always {
                archiveArtifacts artifacts: 'target/*.jar', onlyIfSuccessful: true
                cleanWs cleanWhenFailure: false
            }
        }
    }
}
