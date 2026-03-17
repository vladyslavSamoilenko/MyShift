# 🕒 ShiftManager Pro (MyShift)

A full-stack employee shift management application designed to streamline scheduling, track working hours, and manage team roles. Built with a modern microservices-friendly architecture and fully containerized using Docker.

## ✨ Key Features

### 👨‍💼 Admin / Manager Dashboard
* **Employee Management:** Add, edit, and manage employee profiles and contact details.
* **Role-Based Access Control:** Assign roles (`ADMIN`, `MANAGER`, `WORKER`) to control system access.
* **Shift Scheduling:** Interactive weekly calendar grid to create, view, and assign shifts to employees.

### 👷 Worker Dashboard
* **Personalized View:** Workers only see their assigned shifts.
* **Real-time Status Tracking:** Interactive shift controls:
  * ▶️ Start Shift (`PRESENT`)
  * ⏸️ Take a Break (`BREAK_START` / `BREAK_END`)
  * ⏹️ End Shift (`FINISHED`)

### 🔐 Security & Architecture
* **JWT Authentication:** Secure login and token-based API communication.
* **Dockerized Environment:** One-click deployment using Docker Compose.

---

## 🛠️ Tech Stack

**Frontend:**
* React 18 (Vite)
* Tailwind CSS (Styling & UI)
* React Router DOM (Routing)
* Axios (API Client with Interceptors)

**Backend:**
* Java 21
* Spring Boot 3.5.0
* Spring Security (JWT Auth)
* Spring Data JPA
* Flyway (Database Migrations)

**Database & DevOps:**
* PostgreSQL 17
* Docker & Docker Compose
* Nginx (Frontend Web Server)

---

## 🚀 Getting Started (Run Locally)

The easiest way to run the application is using Docker. You don't need to install Java, Node.js, or PostgreSQL on your local machine—Docker will handle everything.

### Prerequisites
* [Docker](https://www.docker.com/get-started) and Docker Compose installed.
* Git installed.

### Installation & Run

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/vladyslavSamoilenko/MyShift.git](https://github.com/vladyslavSamoilenko/MyShift.git)
   cd MyShift```
2. Start the application using Docker Compose:
   ```docker-compose up -d```
3. Access the application:

   Frontend (UI): http://localhost:3000

   Backend (API): http://localhost:8087
4. Stopping the Application
   ```docker-compose down```
---

### Project Structure
* /backend - Spring Boot Java application, REST API, Security configurations, and Flyway migration scripts.

* /client/myShift-ui - React frontend application, Tailwind configurations, and UI components.

* docker-compose.yml - Infrastructure configuration to link the DB, Backend, and Frontend containers.
