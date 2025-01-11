# InfraOps Project - Kubernetes Deployment with CI/CD

## 📦 Project Overview
This project demonstrates a **Spring Boot** backend service deployed on **Minikube** using **Kubernetes**. It also includes a **PostgreSQL** database configured to run on port **5232**, managed with **Kubernetes manifests** and automated using **GitHub Actions** for CI/CD.

---

## 🎯 Objectives
- **Application:** Spring Boot REST API with a `/health` endpoint.
- **Database:** PostgreSQL (running on port **5232** externally, **5432** internally).
- **Containerization:** Dockerized using a `Dockerfile`.
- **CI/CD Automation:** GitHub Actions to build and deploy the Docker image.
- **Kubernetes Deployment:** Minikube with YAML manifests.

---

## 📁 Project Structure
```
.
├── gradle/                          # Gradle build system
├── src/                             # Application source code
├── Dockerfile                      # For containerizing the application
├── docker-compose.yml              # Optional for local testing with Compose
├── k8s-manifests/                  # Kubernetes YAML manifests
│   ├── infraops-deployment.yaml
│   ├── infraops-service.yaml
│   ├── namespace.yaml
│   ├── postgres-service.yaml
│   └── pvc.yaml
├── .github/workflows/docker-build.yml  # GitHub Actions for CI/CD
├── README.md                       # This documentation file
```

---

## 🚀 Getting Started

### ✅ Prerequisites:
- **Docker**
- **Minikube**
- **Kubectl**
- **PostgreSQL Client**

### ✅ Clone the Repository
```bash
git clone <repo-url>
cd infraops
```

### ✅ Start Minikube
```bash
minikube start
```

### ✅ Apply Kubernetes Manifests
```bash
kubectl apply -f k8s-manifests/namespace.yaml
kubectl apply -f k8s-manifests/pvc.yaml
kubectl apply -f k8s-manifests/postgres-deployment.yaml
kubectl apply -f k8s-manifests/postgres-service.yaml
kubectl apply -f k8s-manifests/infraops-deployment.yaml
kubectl apply -f k8s-manifests/infraops-service.yaml
```

### ✅ Verify Pod and Service Status
```bash
kubectl get pods -n infraops-namespace
kubectl get services -n infraops-namespace
```

### ✅ Expose the Application
```bash
minikube service infraops-service -n infraops-namespace
```

### ✅ Access PostgreSQL from Local Machine
```bash
minikube service infraops-postgres -n infraops-namespace --url
psql -h <minikube-ip> -p 5232 -U admin -d your_database
```

---

## 📦 CI/CD Pipeline (GitHub Actions)

### ✅ **Workflow Summary:**
1. **Triggers:** On `push` to the `main` branch.
2. **Steps:**
    - Checkout Code
    - Build the Docker Image
    - Push the Image to **Docker Hub**
    - Apply Kubernetes Manifests

### ✅ **`.github/workflows/docker-build.yml` CI/CD Pipeline:**
```yaml
name: Build and Push Docker Image

on:
  push:
    branches:
      - main

jobs:
  build_and_push:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
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
```

### ✅ **Adding GitHub Secrets:**
- `DOCKER_HUB_USERNAME`: Your Docker Hub Username
- `DOCKER_HUB_PASSWORD`: Your Docker Hub Personal Access Token

---

## 📦 Design Decisions & Justifications

1. **Minikube for Local Kubernetes Testing:**
    - Minikube provides a local Kubernetes environment ideal for testing before cloud deployments.

2. **Namespace Usage:**
    - Resources are isolated using the `infraops-namespace` to avoid conflicts with other services.

3. **Database Port Mapping:**
    - PostgreSQL runs internally on **port 5432** and is exposed as **5232** for local testing.

4. **CI/CD with GitHub Actions:**
    - Automates the build, push, and deployment process to ensure consistency.

---

## ✅ Troubleshooting

### **Check Pod Logs:**
```bash
kubectl logs -n infraops-namespace -l app=infraops-app
kubectl logs -n infraops-namespace -l app=infraops-postgres
```

### **Delete and Reapply Resources:**
```bash
kubectl delete -f k8s-manifests/ --namespace=infraops-namespace
kubectl apply -f k8s-manifests/
```

