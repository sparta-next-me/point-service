pipeline {
    agent any

    environment {
        // 1. 서비스 정보 정의 (point-service로 변경)
        APP_NAME        = "point-service"
        NAMESPACE       = "next-me"

        // 2. GHCR 레지스트리 정보
        REGISTRY        = "ghcr.io"
        GH_OWNER        = "sparta-next-me"
        IMAGE_REPO      = "point-service"
        FULL_IMAGE      = "${REGISTRY}/${GH_OWNER}/${IMAGE_REPO}:latest"

        // 시간대 설정
        TZ              = "Asia/Seoul"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Jenkins에 등록된 point-service 전용 Credential 사용
                withCredentials([
                    file(credentialsId: 'point-env', variable: 'ENV_FILE')
                ]) {
                    sh '''
                      set -a
                      . "$ENV_FILE"
                      set +a
                      ./gradlew clean bootJar --no-daemon
                    '''
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'ghcr-credential',
                        usernameVariable: 'USER',
                        passwordVariable: 'TOKEN'
                    )
                ]) {
                    sh """
                      docker build -t ${FULL_IMAGE} .
                      echo "${TOKEN}" | docker login ${REGISTRY} -u "${USER}" --password-stdin
                      docker push ${FULL_IMAGE}
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                // K3s 설정파일과 포인트 서비스용 .env 파일을 사용하여 배포
                withCredentials([
                    file(credentialsId: 'k3s-kubeconfig', variable: 'KUBECONFIG_FILE'),
                    file(credentialsId: 'point-env', variable: 'ENV_FILE')
                ]) {
                    sh '''
                      export KUBECONFIG=${KUBECONFIG_FILE}

                      # 1. K8s 매니페스트(YAML)에 적힌 시크릿 이름인 'promotion-env'로 생성
                      # (참고: YAML 파일의 envFrom에 promotion-env라고 되어 있으므로 이름을 맞춰야 함)
                      echo "Updating K8s Secret: promotion-env for point-service..."
                      kubectl delete secret promotion-env -n ${NAMESPACE} --ignore-not-found
                      kubectl create secret generic promotion-env --from-env-file=${ENV_FILE} -n ${NAMESPACE}

                      # 2. 쿠버네티스 매니페스트 적용 (point-service.yaml)
                      echo "Applying manifests from point-service.yaml..."
                      kubectl apply -f point-service.yaml -n ${NAMESPACE}

                      # 3. 배포 모니터링: 롤링 업데이트 상태 확인
                      # YAML의 ReadinessProbe(60초) 설정 때문에 가동까지 약 1분 이상 소요될 수 있음
                      echo "Monitoring rollout status for ${APP_NAME}..."
                      kubectl rollout status deployment/point-service -n ${NAMESPACE}

                      # 4. 최종 확인
                      kubectl get pods -n ${NAMESPACE} -l app=point-service
                    '''
                }
            }
        }
    }

    post {
        always {
            echo "Cleaning up local docker image..."
            sh "docker rmi ${FULL_IMAGE} || true"
        }
        success {
            echo "Successfully deployed ${APP_NAME} to Kubernetes Cluster!"
        }
        failure {
            echo "Deployment failed. Check point-service Pod status and Actuator Health endpoint."
        }
    }
}