# Restaurant Reservation System

A full-stack restaurant reservation management application built with Spring Boot and React, allowing customers to make reservations and administrators to manage them efficiently.

## Prerequisites

- Docker (version 20.10+)
- Docker Compose (version 1.29+)

## Getting Started

### Quick Start with Docker Compose

1. **Clone the repository**:

   ```bash
   git clone <repository-url>
   cd cgi_task_2026
   ```

2. **Build and start the application**:

   ```bash
   docker-compose up --build
   ```

   This command will:
   - Build the Spring Boot backend from `resto-reserv-app/`
   - Build the React frontend from `resto-reserv-app-frontend/`
   - Start both services in containers
   - Link them via Docker network

3. **Access the application**:
   - **Frontend**: [http://localhost:5173](http://localhost:5173)
   - **Backend API**: [http://localhost:8080](http://localhost:8080)

4. **Stop the application**:
   ```bash
   docker-compose down
   ```

### Local Development Setup

#### Backend

```bash
cd resto-reserv-app
./mvnw spring-boot:run
```

#### Frontend

```bash
cd resto-reserv-app-frontend
npm install
npm run dev
```

## Features

### Customer Features

- **Easy Reservation Booking**: Book a table with date, time, party size, and preferences
- **Table Selection**: Choose tables based on available attributes (window seating, smoking area, etc.)
- **Real-time Availability**: Check table availability before confirming reservation
- **Responsive Design**: Works seamlessly on desktop and mobile devices

### Admin Features

- **Dashboard**: View and manage all reservations with filtering and pagination
- **Floor Editor**: Visual editor to manage restaurant floor layout and tables
- **Reservation Management**: View detailed reservation information and history
- **Dynamic Table Management**: Add, remove, and update table attributes

---

**Notes**
