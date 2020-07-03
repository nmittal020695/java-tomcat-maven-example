pipeline {
    
    agent any;
    environment {
      ARTIFACT_ID="javierex/javatest:${BUILD_NUMBER}-${env.BRANCH_NAME}"
      
      //SONAR
      SONAR_URL="http://52.170.155.167:9000"
      SONAR_TOKEN="9c10addc968c43fe861d541d6b08e3ef0acf6cac"
      SONAR_PROJECT="JavaBHDTest"
      
      //Deploy 
      DEPLOYMENT_NODE = getDeploymentNode()

    }
    stages{
        stage('Prepare'){
            steps{
                /*   At this stage the necessary instructions are recorded
                    to clean our environment. 
                */
            
                labelledShell( label: "Clean environment",
                    script: "echo cleaning...")
            }
        }
        stage('Build'){
            agent {
                docker {
                    image 'maven:3-alpine' 
                    args "-v /${HOME}/.m2:/root/.m2 -v ${WORKSPACE}:/app -w /app --entrypoint="
                    reuseNode true
                }
            }
            steps{
                //We clean the project and compile
                sh 'mvn -B -DskipTests clean  package' 
            }
                        
                
        }
        stage('Test'){
            parallel{
                stage('Sonar Analysis'){
                   agent {
                        docker {
                            image 'maven:3-alpine' 
                            args "-v ${HOME}/.m2:/root/.m2 --entrypoint="
                            
                        }
                    }
                    steps{
                        withSonarQubeEnv('Sonar-Server') {
                            sh "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT} -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}"
                        }

                        /*
                            We await the response of the Sonar Webhook
                            If it doesn't finish in 2 minutes, abort the pipeline process
                        */
                        timeout(time: 2, unit: 'MINUTES') {
                             waitForQualityGate abortPipeline: true
                        }
                    }
                }
                stage('Unit Test'){
                    agent {
                        docker {
                            image 'maven:3-alpine' 
                            args "-v ${HOME}/.m2:/root/.m2 --entrypoint="
                        }
                    }
                    steps{
                        sh 'mvn test' 
                    }
                }
                stage('Image Security Test'){
                    steps{
                        labelledShell( label: "Construcción de la Imagen",
                            script: "docker build -t ${ARTIFACT_ID} .")

                        /*
                             We execute the security analysis with the Aqua Microscanner
                        */    
                        aquaMicroscanner imageName: "${ARTIFACT_ID}", notCompliesCmd: 'exit 1', onDisallowed: 'fail', outputFormat: 'html'
                    }
                }
               
            }
        }
        stage('Package'){
            when {
                branch pattern: "development|release|master", comparator: "REGEXP"
            }
            steps{
                script {
                    withDockerRegistry([ credentialsId: "44a219b5-386c-49ae-96f1-a5a57af89886", url: "" ]){
                        def customImage = docker.build("${ARTIFACT_ID}")
                        customImage.push()

                        customImage.push('latest')
                        
                    }
                }
            }
            
        }
        
        stage('Deploy'){
            agent { label DEPLOYMENT_NODE }
            when {
                branch pattern: "development|release|master", comparator: "REGEXP"
            }

            steps{
                /*
                     We launch our kubernetes commands on our node
                */ 
                labelledShell( label: "Creacion de Pod",
                            script: "kubectl apply -f deploy/deployment.yml")

                labelledShell( label: "Creacion de Servicio",
                            script: "kubectl apply -f deploy/service.yml")
               
            }
        }
        
        stage('Automated Tests'){
            when {
                branch pattern: "development|release|master", comparator: "REGEXP"
            }
            parallel {
                stage('Katalon'){
                    /*
                        In this stage we will execute the automated functional tests
                         In this case, it would be with the Katalon application
                    */
                    steps{
                        echo "Katalon testing..."
                    }
                }
                stage('JMeter'){
                    /*
                        In this stage we will execute the Load tests
                        In this case, it would be with the Jmeter application
                    */
                    steps{
                        echo "JMeter testing..."
                    }
                }
                
            }
        }
        
        stage('Deployment to Production'){
            when {
                branch 'master' 
            }
            options { skipDefaultCheckout() }  //Not clone repo in agent

            steps{
                echo "Deployment to production..."
            }
        }
    }
    
}

def getDeploymentNode(){
    script {
        switch(env.BRANCH_NAME) {
          case "master":
            return "staging-node"
            
          case "release":
            return "qa-node"
            
          default:
            return "dev-node" 
            
        }
    }
}

