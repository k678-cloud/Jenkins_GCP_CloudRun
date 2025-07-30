pipeline {
    agent any
    tools {
	jdk 'java17015'
	maven 'maven387'
    }
    environment {
	SONAR_SCANNER_HOME = tool 'sonar7'
	IMAGE_NAME = "java-app"
        IMAGE_TAG = "${BUILD_NUMBER}"
	GCP_PROJECT_ID = "focal-dock-440200-u5"
	FULL_IMAGE_NAME = "us-docker.pkg.dev/${GCP_PROJECT_ID}/java-app-repo-02/${IMAGE_NAME}:${IMAGE_TAG}"
	SERVICE_NAME = "java-app-service"
	REGION = "us-central1"
    }
    stages {
        stage('Initialize Pipeline'){
            steps {
                echo 'Initializing Pipeline ...'
		sh 'java -version'
		sh 'mvn -version'
            }
        }
        stage('Checkout GitHub Codes'){
            steps {
                echo 'Checking out GitHub Codes ...'
		checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'jenkins-gcp', url: 'https://github.com/k678-cloud/Jenkins_GCP_CloudRun.git']])
            }
        }
        stage('Maven Build'){
            steps {
                echo 'Building Java App with Maven'
		sh 'mvn clean package'
            }
        }
        stage('JUnit Test of Java App'){
            steps {
                echo 'JUnit Testing'
		sh 'mvn test'
            }
        }
        stage('SonarQube Analysis'){
            steps {
                echo 'Running Static Code Analysis with SonarQube'
		withCredentials([string(credentialsId: 'jmsonar', variable: 'sonarToken')]) {
   			withSonarQubeEnv('sonar') {
				sh '''
					${SONAR_SCANNER_HOME}/bin/sonar-scanner \
  					-Dsonar.projectKey=jenkinsgcp \
  					-Dsonar.sources=. \
  					-Dsonar.host.url=http://192.168.160.128:9000 \
       					-Dsonar.java.binaries=target/classes \
  					-Dsonar.token=$sonarToken
    				'''
			}
		}
            }
        }
        stage('Trivy FS Scan'){
            steps {
                echo 'Scanning File System with Trivy FS ...'
		sh 'trivy fs --format table -o FSScanReport.html'
            }
        }
        stage('Build & Tag Docker Image'){
            steps {
                echo 'Building the Java App Docker Image'
		script {
			sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
		}
            }
        }
	}
	stage('Trivy Security Scan') {
	    steps {
	        script {
	            echo 'Scanning Docker Image with Trivy'
	            sh """
	                trivy image \
	                    --severity HIGH,CRITICAL,MEDIUM,LOW,UNKNOWN \
	                    --no-progress \
	                    --format table \
	                    -o trivyScanReport.txt \
	                    ${IMAGE_NAME}:${IMAGE_TAG}
	            """
	        }
	    }
	}

        }
        stage('Publish Metrics to InfluxDB') {
            steps {
                script {
                    // Extract Trivy summary line from the report
                    def summaryLine = sh(script: "grep '^Total:' trivyScanReport.txt", returnStdout: true).trim()
                    echo "Trivy Summary Line: ${summaryLine}"

                    def matcher = summaryLine =~ /Total: (\\d+) \\(UNKNOWN: (\\d+), LOW: (\\d+), MEDIUM: (\\d+), HIGH: (\\d+), CRITICAL: (\\d+)\\)/

                    if (!matcher) {
                        error "Failed to parse Trivy summary"
                    }

                    def total = matcher[0][1]
                    def unknown = matcher[0][2]
                    def low = matcher[0][3]
                    def medium = matcher[0][4]
                    def high = matcher[0][5]
                    def critical = matcher[0][6]

                    // Publish to InfluxDB
                    influxDbPublisher(
                        selectedTarget: 'influxdb',
                        customProjectName: 'jenkins-org',
                        customData: [
                            trivy_total: total,
                            trivy_unknown: unknown,
                            trivy_low: low,
                            trivy_medium: medium,
                            trivy_high: high,
                            trivy_critical: critical
                        ],
                        customDataTags: [
                            scanner: 'trivy',
                            image: "${IMAGE_NAME}:${IMAGE_TAG}"
                        ]
                    )
                }
            }
        }
	stage('Authenticate with GCP, Tag & Push to Artifact Registry') {
            steps {
		echo 'Authenticate with GCP, tag and Push Image to Artifact Registry'
		// withCredentials([file(credentialsId: 'gcpjmsa', variable: 'gcpCred')]) {
  //   			withEnv(["GOOGLE_APPLICATION_CREDENTIALS=$gcpCred"]) {
		// 		sh '''
		// 			echo Activating GCP service account...
  //                   			// gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS
  //                   			// gcloud config set project $GCP_PROJECT_ID
		       
  //                   			echo Configuring Docker to use gcloud credentials...
  //                   			// gcloud auth configure-docker us-docker.pkg.dev --quiet
  //   				'''
		// 		script {
		// 			sh '''
		// 				gcloud artifacts repositories create java-app-repo-${IMAGE_TAG} --repository-format=docker --location=us --description="Docker repository" --project=$GCP_PROJECT_ID
  //    					'''
		// 			sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${FULL_IMAGE_NAME}"
		// 			sh "docker push ${FULL_IMAGE_NAME}"
		// 			echo "Image pushed to: ${FULL_IMAGE_NAME}"
		// 		}
		// 	}
		// }
            }
        }
	stage('Deploy to Cloud Run') {
		steps {
			echo 'Deploying Image to Google Cloud Run'
			withCredentials([file(credentialsId: 'gcpjmsa', variable: 'gcpCred')]) {
    				withEnv(["GOOGLE_APPLICATION_CREDENTIALS=$gcpCred"]) {
					sh '''
						gcloud run deploy $SERVICE_NAME \
            					--image=$FULL_IMAGE_NAME \
            					--region=$REGION \
            					--platform=managed \
            					--allow-unauthenticated \
		 				--port=8090 \
            					--memory=512Mi \
            					--quiet
     					'''
				}
			}
		}
	}
	stage('Get Cloud Run Service URL') {
            steps {
			echo 'Getting Cloud Run Service URL'
		    	withCredentials([file(credentialsId: 'gcpjmsa', variable: 'gcpCred')]) {
    				withEnv(["GOOGLE_APPLICATION_CREDENTIALS=$gcpCred"]) {
					sh '''
                    				SERVICE_URL=$(gcloud run services describe $SERVICE_NAME \
                        				--platform managed \
                        				--region $REGION \
                        				--format="value(status.url)")
			    			echo "Service deployed successfully!"
                        			echo "Service URL: $SERVICE_URL"
                			'''
					}
				}
            		}
       	 	}
	
}
