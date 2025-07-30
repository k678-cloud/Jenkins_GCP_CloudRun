        stage('Trivy Security Scan'){
            steps {
                echo 'Scanning Docker Image with Trivy'
                sh '''
                    trivy image \
                        --severity HIGH,CRITICAL,MEDIUM,LOW,UNKNOWN \
                        --cache-dir ${WORKSPACE}/.trivy-cache \
                        --no-progress \
                        --format table \
                        -o trivyScanReport.txt \
                        ${IMAGE_NAME}:${IMAGE_TAG}
                '''
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
