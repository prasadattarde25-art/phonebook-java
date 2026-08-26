# Phonebook Application

A production-style full-stack Phonebook Application built with **Vue.js
3, Java Spring Boot, Spring Security, JWT, PostgreSQL, JPA/Hibernate,
Axios, Docker, and Docker Compose**.

## Architecture

``` text
Vue.js 3 (:5173)
      |
    Axios
      |
Spring Boot REST API (:8000)
      |
Spring Security + JWT
      |
Spring Data JPA / Hibernate
      |
PostgreSQL 16 (:5432)
```

## Features

-   JWT-based login and authentication
-   Contact CRUD operations
-   Contact detail view
-   Search
-   Pagination
-   Duplicate phone/email validation
-   PostgreSQL persistence
-   Dockerized frontend, Java backend, and database
-   Playwright end-to-end tests
-   ngrok public demonstration support

## Technology Stack

  Layer             Technology
  ----------------- -----------------------------
  Frontend          Vue.js 3
  Routing           Vue Router
  HTTP Client       Axios
  Backend           Java Spring Boot
  Security          Spring Security + JWT
  ORM               Spring Data JPA / Hibernate
  Database          PostgreSQL 16
  Build             Maven
  Containers        Docker + Docker Compose
  Testing           Playwright
  Version Control   Git / GitHub

## REST API

### Authentication

``` text
POST /api/auth/login
```

The login endpoint accepts `application/x-www-form-urlencoded`
parameters:

``` text
username=admin
password=admin123
```

Successful response:

``` json
{
  "access_token": "<JWT_TOKEN>",
  "token_type": "bearer"
}
```

### Contacts

  Method   Endpoint           Purpose
  -------- ------------------ --------------------------------------
  GET      `/contacts/`       List contacts with search/pagination
  POST     `/contacts/`       Create contact
  GET      `/contacts/{id}`   Get one contact
  PUT      `/contacts/{id}`   Update contact
  DELETE   `/contacts/{id}`   Delete contact

Protected contact requests use:

``` text
Authorization: Bearer <JWT_TOKEN>
```

Search/pagination example:

``` text
GET /contacts/?search=java&page=1&limit=10
```

## Project Structure

``` text
phonebook-java/
├── backend/                 # Original Python backend retained during migration
├── backend-java/            # Active Java Spring Boot backend
│   ├── src/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
├── frontend/
│   └── src/
├── tests/                   # Playwright tests
├── docker-compose.yml
├── .env
├── package.json
├── playwright.config.js
└── README.md
```

The active Docker Compose backend is `backend-java`.

## Docker Compose

Three services are managed by Docker Compose:

  Service    Technology                Port
  ---------- ----------------------- ------
  frontend   Vue.js / Vite             5173
  backend    Spring Boot / Java 21     8000
  db         PostgreSQL 16             5432

The Java backend connects to PostgreSQL through the Docker service name:

``` text
db:5432
```

PostgreSQL data is persisted in the `postgres_data` named volume.

## Environment

Create a local `.env` file:

``` env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=YOUR_PASSWORD
POSTGRES_DB=phonebook
```

Do not commit real credentials.

## Prerequisites

-   Docker Desktop
-   Git
-   Node.js
-   Java 21 for local Java development

Check Docker:

``` powershell
docker --version
docker compose version
```

Check Java:

``` powershell
java -version
```

## Run with Docker

From the project root:

``` powershell
docker compose up -d --build
```

Verify:

``` powershell
docker compose ps
```

View Java backend logs:

``` powershell
docker compose logs -f backend
```

Expected containers:

``` text
phonebook-frontend
phonebook-backend
phonebook-db
```

## Application URLs

Frontend:

``` text
http://localhost:5173
```

Backend:

``` text
http://localhost:8000
```

The backend is a REST API; use the controller-defined endpoints listed
above.

## ngrok Public Demo

Start the application first:

``` powershell
docker compose up -d --build
```

Then expose the frontend:

``` powershell
ngrok http 5173
```

ngrok provides a temporary HTTPS URL for demonstrations. The URL can
change when the tunnel is restarted.

## Testing

Playwright end-to-end tests are stored in:

``` text
tests/
```

Install dependencies:

``` powershell
npm install
```

Run the configured Playwright test command from `package.json`.

## Functional Verification

The migrated application has been functionally verified for:

-   Login and JWT authentication
-   CORS
-   Contact list
-   Contact details
-   Create
-   Update
-   Delete
-   Search
-   Pagination
-   PostgreSQL persistence
-   Dockerized Java backend
-   Frontend integration
-   ngrok public access

## Database Persistence

Normal shutdown:

``` powershell
docker compose down
```

This keeps the PostgreSQL named volume.

To remove the database volume:

``` powershell
docker compose down -v
```

> Warning: `docker compose down -v` deletes the persisted PostgreSQL
> data.

## Migration: Python to Java

The original backend architecture was:

``` text
Vue.js
   |
FastAPI / Python
   |
SQLAlchemy
   |
PostgreSQL
```

The active backend architecture is now:

``` text
Vue.js
   |
Spring Boot / Java
   |
Spring Security + JWT
   |
Spring Data JPA / Hibernate
   |
PostgreSQL
```

The migration preserves the phonebook application's core CRUD, search,
pagination, authentication, and persistence functionality.

## Final Architecture

``` text
Internet
   |
   v
ngrok HTTPS URL
   |
   v
Vue.js Frontend :5173
   |
   v
Spring Boot Backend :8000
   |
   v
PostgreSQL :5432
```

Docker Compose manages the application services.

## Project Status

The Python-to-Java backend migration is complete. The Java backend,
Docker deployment, PostgreSQL integration, JWT authentication, frontend
integration, CRUD operations, search, pagination, and public ngrok flow
have been tested successfully.
