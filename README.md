# Spam Detector Web Application

A professional, full-stack web application for detecting spam messages using a keyword and rule-based heuristic approach. Built with React (Vite + Tailwind CSS) and Spring Boot.

## Features

- **Authentication System**: Secure JWT-based login and registration using BCrypt password encryption.
- **Spam Detection Engine**: Fast, efficient spam analysis using pre-compiled regex and keyword matching. Evaluates URLs, uppercase frequency, and special characters.
- **Analytics Dashboard**: Real-time statistics of total checks, spam vs safe ratio, and recent activity.
- **History Tracking**: Automatically saves all spam checks to a PostgreSQL database for future reference.
- **Modern UI**: Fully responsive, dark-mode compatible interface built with Tailwind CSS and Lucide React icons.

## Tech Stack

- **Frontend**: React 19, React Router DOM v7, Tailwind CSS v4, Vite, Axios, React Hot Toast.
- **Backend**: Java 17, Spring Boot 3.2.x, Spring Security, Spring Data JPA, JWT (io.jsonwebtoken).
- **Database**: PostgreSQL.

## Getting Started

### Prerequisites

- Node.js (v18 or higher)
- Java Development Kit (JDK) 17
- PostgreSQL Server installed and running locally
- Maven

### 1. Database Setup

Create a new PostgreSQL database:
```sql
CREATE DATABASE spam_detector;
```

*(Note: Spring Data JPA is configured to automatically generate the necessary tables `users` and `spam_checks` on application startup).*

### 2. Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Configure your database credentials. You can pass them as environment variables or update `src/main/resources/application.properties`:
   ```properties
   DB_URL=jdbc:postgresql://localhost:5432/spam_detector
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   The backend will start on `http://localhost:8080`.

### 3. Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install the dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
   The frontend will start on `http://localhost:5173`.

## API Documentation

### Authentication endpoints
- `POST /api/auth/register` - Register a new user.
- `POST /api/auth/login` - Authenticate and receive a JWT.

### Spam endpoints (Requires Bearer Token)
- `POST /api/spam/check` - Submit a message for spam analysis.
- `GET /api/spam/history` - Retrieve the current user's spam check history.

### Dashboard endpoints (Requires Bearer Token)
- `GET /api/dashboard/stats` - Retrieve total checks and spam distribution statistics.

## Project Structure

```text
Spam-Detection/
├── backend/
│   ├── src/main/java/com/spamdetector/backend/
│   │   ├── controller/     # REST APIs
│   │   ├── dto/            # Request/Response models
│   │   ├── entity/         # JPA Entities (User, SpamCheck)
│   │   ├── enums/          # Enums (Role)
│   │   ├── exception/      # Global error handling
│   │   ├── repository/     # Data Access Layer
│   │   ├── security/       # JWT and Spring Security Configuration
│   │   └── service/        # Business Logic (SpamService, AuthService)
│   └── pom.xml             # Maven dependencies
└── frontend/
    ├── src/
    │   ├── components/     # Reusable UI (Navbar, Sidebar)
    │   ├── context/        # React Context (AuthContext)
    │   ├── layouts/        # Page Wrappers (DashboardLayout)
    │   ├── pages/          # Full Views (Login, Register, Dashboard, SpamChecker)
    │   ├── routes/         # ProtectedRoute logic
    │   ├── services/       # Axios API integrations
    │   ├── App.jsx         # Router configuration
    │   └── index.css       # Tailwind entry point
    └── package.json        # NPM scripts and dependencies
```
