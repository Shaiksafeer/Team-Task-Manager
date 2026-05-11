# Team Task Manager

A robust, production-ready full-stack application for managing team projects and tasks. Built with a modern tech stack featuring Spring Boot 4 (Java 21) and React 19.

## 🚀 Overview

Team Task Manager is designed to streamline project workflows and enhance team collaboration. It provides distinct roles for Administrators and Members, allowing for granular control over project creation, task assignment, and progress tracking.

### Key Features

*   **Role-Based Access Control (RBAC):** Secure authentication and authorization for Admins and Members.
*   **Project Management:** Admins can create, update, and delete projects, and assign members to them.
*   **Task Management:** Full CRUD operations for tasks with priority levels (Low, Medium, High) and status tracking (Todo, In Progress, Done).
*   **Interactive Dashboard:** Real-time statistics on project progress, pending tasks, and overdue alerts.
*   **Modern UI/UX:** Clean, responsive interface built with React 19 and styled with glassmorphism effects.
*   **API Documentation:** Integrated Swagger UI for easy API exploration and testing.

---

## 🛠️ Tech Stack

### Backend
*   **Framework:** Spring Boot 4 (Java 21)
*   **Database:** PostgreSQL
*   **Security:** Spring Security with JWT (JSON Web Tokens)
*   **Documentation:** SpringDoc OpenAPI (Swagger)
*   **Build Tool:** Maven
*   **ORM:** Spring Data JPA / Hibernate

### Frontend
*   **Framework:** React 19 (Vite)
*   **State Management:** Context API
*   **Routing:** React Router 7
*   **Icons:** Lucide React & React Icons
*   **Styling:** Vanilla CSS (Modern CSS variables and Flexbox/Grid)
*   **HTTP Client:** Axios

---

## 📂 Project Structure

```text
Team-Task-Manager/
├── backend/               # Spring Boot Application
│   ├── src/main/java/     # Source code
│   ├── src/main/resources/# Configuration (application.yml)
│   └── pom.xml            # Maven dependencies
├── frontend/              # React Application
│   ├── src/               # React components, pages, and services
│   ├── public/            # Static assets
│   └── package.json       # Node dependencies
└── README.md              # Project documentation
```

---

## ⚙️ Getting Started

### Prerequisites
*   Java 21+
*   Node.js 18+
*   PostgreSQL
*   Maven

### Backend Setup

1.  Navigate to the backend directory:
    ```bash
    cd backend
    ```
2.  Configure environment variables. You can set these in your IDE or system:
    -   `DATABASE_URL`: `jdbc:postgresql://localhost:5432/team_manager`
    -   `DATABASE_USER`: Your DB username
    -   `DATABASE_PASS`: Your DB password
    -   `JWT_SECRET`: A long, secure random string
    -   `FRONTEND_URL`: `http://localhost:5173`
3.  Build and run the application:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
4.  API Documentation will be available at: `http://localhost:8080/swagger-ui.html`

### Frontend Setup

1.  Navigate to the frontend directory:
    ```bash
    cd frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Create a `.env` file based on `.env.example`:
    ```bash
    VITE_API_URL=http://localhost:8080/api
    ```
4.  Start the development server:
    ```bash
    npm run dev
    ```
5.  Access the app at: `http://localhost:5173`

---

## 📡 API Endpoints (Summary)

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user | Public |
| `POST` | `/api/auth/login` | Login and receive JWT | Public |
| `GET` | `/api/projects` | List all assigned projects | Auth |
| `POST` | `/api/projects` | Create a new project | Admin |
| `GET` | `/api/tasks/my-tasks`| Get tasks assigned to current user | Auth |
| `PATCH`| `/api/tasks/{id}/status`| Update task status | Auth |
| `GET` | `/api/dashboard/admin`| Get admin dashboard stats | Admin |

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
