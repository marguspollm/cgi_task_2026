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
- **Table Selection**: Choose tables based on available attributes
- **Real-time Availability**: Check table availability before confirming reservation
- **Responsive Design**: Works seamlessly on desktop and mobile devices

### Admin Features

- **Dashboard**: View and manage all reservations with filtering
- **Floor Editor**: Visual editor to manage restaurant floor layout and tables

## Development process

### **Prequsites made:**

- Each reservation books a table for 2 hours
- The restaurant is open 24/7
- For this demo a simple CORS policy is used

### Notes

When the application starts, tables and reservations are generated randomly. Triggering the random generators is done with AI help (noted in code).

Set up Docker with docker-compose, better would be two separate Docker environments, but as this is a demo, one docker-compose for both is sufficient.

At first created a simple backend application with Maps as a way of persisting data, but later decided to change it to H2 database. That took bit longer beacuse of rewriting tests and had bit of help from AI to write database queries in repository(noted in code).

Implemented MealDB API call for getting random meals to show user after creating a reservation. The frontend Carousel components visual layout was helped with AI.

Creating drag and drop for tables took a lot of time ~12hr and was a mix of StackOverflow/Reddit/MDN docs reasearch and trying. The final visual placement was helped with AI.

Writing tests took alot of time, in total ~12hr, mainly because of making refactorings in code.

Alot of time was spent making frontend components and pages, checking if they work, error handlings for forms and backend. The frontend visual UI and components layouts are made with help from AI.

In total the development for this version of the task took ~1 week.

#### TODO:

- Some tests might still be missing, especially the edge cases
- There are no frontend tests(there is Playwright framework for E2E or Jest)
- Right now times are generated from 00:00-23:00 on frontend - better option, admin sets open times per day and based on user selection of the day - the times are shown
