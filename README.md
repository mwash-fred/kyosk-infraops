# InfraOps Kubernetes Deployment with CI/CD Pipeline

This documentation provides a step-by-step guide for setting up, containerizing, and deploying a Spring Boot application using Minikube and Kubernetes. I have also included a GitHub Actions CI/CD pipeline for automated builds and Docker image uploads to Docker Hub.

---

## 📦 Objective
My goal for this project is to:
- **Containerize** the application using a `Dockerfile`.
- **Automate CI/CD** with GitHub Actions.
- **Deploy the Application** to a Minikube cluster using Kubernetes manifests.
- **Ensure Accessibility** via a NodePort service.

---

## 📦 Step 1: Clone the GitHub Repository
To get started, clone the GitHub repository containing the InfraOps project:

```bash
git clone https://github.com/mwash-fred/kyosk-infraops.git
cd kyosk-infraops
```

---

## 📂 Project Structure
```plaintext
k8s-manifests/
├── infraops-deployment.yaml
├── infraops-service.yaml
├── namespace.yaml
├── postgres-deployment.yaml
├── postgres-service.yaml
├── pvc.yaml
src/
├── Dockerfile
├── build.gradle
```

---

## 🚀 Prerequisites
Ensure you have the following installed and configured:
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)
- [Docker](https://docs.docker.com/get-docker/)

---

## 📦 Step 2: Build the Docker Image
I created the following `Dockerfile` to containerize the Spring Boot application. GitHub Actions takes care of this as shown in the screenshot below.

```dockerfile
# Use the official Gradle image with JDK 21 for the build stage
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy all project files
COPY . .

# Build the application using Gradle, skipping tests for faster build
RUN gradle clean build -x test

# Use Eclipse Temurin JDK for the runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar infraops.jar

# Expose port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "infraops.jar"]
```

---

## 📦 Step 3: Kubernetes Setup with Minikube
### Start Minikube:
```bash
minikube start
```

### Verify Minikube Setup:
```bash
kubectl get nodes
```

---

## 📦 Step 4: Deploy Kubernetes Resources
I have created multiple Kubernetes manifests for both the InfraOps application and the PostgreSQL database.

### Apply Namespace:
```bash
kubectl apply -f k8s-manifests/namespace.yaml
```

### Apply Persistent Volume Claim (PVC):
```bash
kubectl apply -f k8s-manifests/pvc.yaml
```

### Deploy PostgreSQL:
```bash
kubectl apply -f k8s-manifests/postgres-deployment.yaml
kubectl apply -f k8s-manifests/postgres-service.yaml
```

### Deploy InfraOps Application:
```bash
kubectl apply -f k8s-manifests/infraops-deployment.yaml
kubectl apply -f k8s-manifests/infraops-service.yaml
```

---

## 📦 Step 5: Verify the Deployment
### Check Running Pods and Services:
```bash
kubectl get pods -n infraops-namespace
kubectl get svc -n infraops-namespace
```

### Get the Service URL:
```bash
minikube service infraops-service -n infraops-namespace --url
```

---

## 📦 Step 6: Accessing the Application
I accessed the deployed application using the service URL provided by Minikube. For example:

```plaintext
[http://192.168.49.2:30000/api/v1/car-brands](http://192.168.49.2:30000/api/v1/car-brands)
```
Please use the service URL provided by Minikube and add the following endpoint `/api/v1/car-brands`.

### ✅ Screenshot: Application Output
(Attach your screenshot here)

---

## 📦 Step 7: CI/CD Automation with GitHub Actions
I have automated the build and deployment process using GitHub Actions. The pipeline builds the Docker image and pushes it to Docker Hub whenever I push to the `main` branch.

### `.github/workflows/main.yml`
```yaml
name: Build and Push Docker Image

on:
  push:
    branches:
      - main

jobs:
  build_and_push:
    name: Build and Push Docker Image to Docker Hub
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_HUB_USERNAME }}
          password: ${{ secrets.DOCKER_HUB_PASSWORD }}

      - name: Build Docker Image
        run: |
          docker build -t ${{ secrets.DOCKER_HUB_USERNAME }}/infraops:0.0.1 .

      - name: Push Docker Image
        run: |
          docker push ${{ secrets.DOCKER_HUB_USERNAME }}/infraops:0.0.1

      - name: Logout from Docker Hub
        run: docker logout
```

### ✅ Screenshot: GitHub Actions Pipeline Result
(Attach your screenshot here)

---

## 📦 Step 8: Troubleshooting
- **Pods Not Starting:** Check the logs using `kubectl logs <pod-name>`.
- **Service Not Accessible:** Ensure the NodePort is correctly configured and within the valid range `30000-32767`.
- **Database Connectivity:** Ensure both the PostgreSQL and InfraOps deployments are in the same namespace.

---

## 📦 Step 9: Cleanup Process
To delete all Kubernetes resources and stop Minikube, run:
```bash
kubectl delete -f k8s-manifests/
minikube stop
minikube delete
```

---

## 📦 Deliverables Summary
- ✅ **Source Code:** Minimal Spring Boot application.
- ✅ **Dockerfile:** For containerization.
- ✅ **Kubernetes Manifests:** For Minikube deployment.
- ✅ **CI/CD Pipeline:** Automated with GitHub Actions.
- ✅ **Screenshots:** To validate successful deployment.

---

## ✅ Conclusion
I successfully containerized and deployed a Spring Boot application using Minikube and Kubernetes. The CI/CD pipeline automates the process by building and pushing the Docker image to Docker Hub.

---

### 📸 Screenshots Used:
1. Application Output (Browser)
2. GitHub Actions Pipeline Result
3. Minikube Service Verification

