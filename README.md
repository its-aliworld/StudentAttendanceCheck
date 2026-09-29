<<<<<<< HEAD
# Student Attendance Management System

Full-stack student attendance website.

## Stack
- Backend: Spring Boot 3, Java 17, Spring Data JPA, Spring Security, MySQL
- Frontend: React + Vite
- Database: MySQL

## Features
- Admin/teacher login
- Student management
- Subject management
- Mark present/absent
- Attendance history
- Attendance percentage
- Dashboard

## Demo credentials
Teacher:
email: admin@example.com
password: admin123

Student:
email: student@example.com
password: student123

## 1. Database
Create a MySQL database:

CREATE DATABASE attendance_db;

Then update backend/src/main/resources/application.properties if your MySQL username/password differs.

The application creates tables automatically.

## 2. Backend
Requirements:
- Java 17+
- Maven 3.9+
- MySQL 8+

From backend:

mvn spring-boot:run

Backend runs on:
http://localhost:8080

## 3. Frontend
Requirements:
- Node.js 18+

From frontend:

npm install
npm run dev

Frontend:
http://localhost:5173

## API
POST /api/auth/login
GET  /api/students
POST /api/students
GET  /api/subjects
POST /api/subjects
POST /api/attendance
GET  /api/attendance/student/{studentId}
GET  /api/attendance/summary/{studentId}

## Screenshots

### Login Page
![Login Page](Screenshots/login.png)

### Dashboard
![Dashboard](Screenshots/TeacherDashboard.png)
![Attendance Page](Screenshots/StudentDashboard.png)

### Attendance History
![Attendance Page](Screenshots/Attendancehistory.png)
