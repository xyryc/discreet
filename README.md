# Discreet 💬

A privacy-focused, real-time messaging application.

## Monorepo Structure

```text
discreet/
├── android/          # Android Client Application (Java)
├── backend/          # Custom Backend Server (Spring Boot 3 + Java 21)
└── README.md
```

## Architecture & Tech Stack

### Android Client (`/android`)
- **Language**: Java / Android SDK
- **Architecture**: MVVM / Clean Architecture
- **Networking**: Retrofit2 (REST) + OkHttp / Scarlet (WebSockets)
- **UI**: DataBinding, Material Design 3

### Backend Server (`/backend`)
- **Framework**: Spring Boot 3.x (Java 21)
- **Security**: Spring Security + JWT Authentication
- **Real-Time Messaging**: Spring WebSocket with STOMP
- **Database & ORM**: PostgreSQL + Spring Data JPA
- **SMS / Verification**: Twilio API (Phone OTP)
- **Object Storage**: MinIO / AWS S3 (Media attachments)

---

## Getting Started

### 1. Backend Setup
```bash
cd backend
./mvnw spring-boot:run
```

### 2. Android Client Setup
1. Open the `android` folder in **Android Studio**.
2. Sync Gradle and build the project.
