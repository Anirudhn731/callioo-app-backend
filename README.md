# 📁 Callioo Backend

Backend API and business logic for the Callioo video-conference & meeting scheduling application.

---

## 🚀 Overview

The backend is built with **Spring Boot** and provides:

- Authentication (login/signup)
- JWT-based security
- REST APIs for user, meeting, schedule management
- WebSocket / STOMP support for real-time updates
- Integration with a self-hosted Jitsi Meet server for video conferencing
- Persistence via MySQL (or any JDBC database)
- Meeting scheduling logic (dates, recurrences, validations)

---

## 🧰 Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Web + WebSocket (SockJS / STOMP)
- JPA / Hibernate
- MySQL
- Maven

---

## 📦 Project Setup

### 1. Clone

```bash
git clone https://github.com/Anirudhn731/callioo-app-backend
cd callioo-app-backend
```
