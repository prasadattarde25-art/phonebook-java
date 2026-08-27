# Phonebook Application

A production-style full-stack **Phonebook Application** built with **Vue.js 3, Java Spring Boot, Spring Security, JWT, PostgreSQL, Spring Data JPA/Hibernate, Axios, Docker, Docker Compose, and Playwright**.

The application provides secure authentication, complete contact CRUD operations, contact details, search, pagination, PostgreSQL persistence, Dockerized deployment, end-to-end testing, and public demonstration through ngrok.

---

## 🚀 Project Demo

**[Open Phonebook Application](https://quarry-bankroll-juicy.ngrok-free.dev/)**

> **Note:** The public URL is provided through ngrok, so it is temporary and may change when the ngrok tunnel is restarted.

------------

# 1. Project Overview

The Phonebook Application follows a three-tier full-stack architecture:

```text
                         USER
                           |
                           v
                  +----------------+
                  |   Vue.js 3     |
                  |    Frontend    |
                  |     :5173      |
                  +-------+--------+
                          |
                       Axios
                          |
                          v
                  +----------------+
                  | Spring Boot    |
                  | REST API       |
                  |     :8000      |
                  +-------+--------+
                          |
                Spring Security + JWT
                          |
                          v
                  +----------------+
                  | Spring Data    |
                  | JPA / Hibernate |
                  +-------+--------+
                          |
                          v
                  +----------------+
                  | PostgreSQL 16  |
                  |     :5432      |
                  +----------------+
```

All application services can be managed using **Docker Compose**.

---

# 2. Business Objective

The goal of the application is to provide a simple and reliable system for managing contact information.

Users can:

- Login securely
- Create contacts
- View contacts
- View individual contact details
- Search contacts
- Navigate contacts using pagination
- Update contacts
- Delete contacts
- Persist contact information in PostgreSQL

The project demonstrates a complete:

```text
Frontend → REST API → Database
```

workflow using a Java Spring Boot backend.

---

# 3. Technology Stack

| Layer | Technology | Purpose |
|---|---|---|
| Frontend | Vue.js 3 | User interface |
| Routing | Vue Router | Client-side navigation |
| HTTP Client | Axios | Frontend-backend communication |
| Backend | Java Spring Boot | REST API development |
| Security | Spring Security + JWT | Authentication and authorization |
| ORM | Spring Data JPA / Hibernate | Database interaction |
| Database | PostgreSQL 16 | Persistent data storage |
| Build | Maven | Java project build |
| Containerization | Docker | Application containers |
| Orchestration | Docker Compose | Multi-service management |
| Testing | Playwright | End-to-end browser testing |
| CI/CD | GitHub Actions | Automated testing |
| Version Control | Git / GitHub | Source-code management |
| Public Demo | ngrok | Temporary public HTTPS access |

---

# 4. Core Features

## Authentication

- JWT-based login
- Spring Security authentication
- Protected contact APIs
- Bearer token authentication

## Contact Management

Complete CRUD functionality:

- **Create** a new contact
- **Read** all contacts
- **Read** individual contact details
- **Update** contact information
- **Delete** contacts

## Contact Information

Each contact contains:

```text
ID
Name
Phone Number
Email
Address
```

## Search

Contacts can be searched using the search parameter.

Example:

```text
GET /contacts/?search=java&page=1&limit=10
```

## Pagination

The contact list supports server-side pagination.

Example response:

```json
{
  "items": [],
  "total": 200,
  "page": 1,
  "limit": 10,
  "pages": 20
}
```

## Validation

The backend supports duplicate phone/email validation and request validation through the application service layer.

---

# 5. REST API Design

## Authentication

### Login

```text
POST /api/auth/login
```

The login endpoint accepts:

```text
application/x-www-form-urlencoded
```

Parameters:

```text
username=admin
password=admin123
```

Successful response:

```json
{
  "access_token": "<JWT_TOKEN>",
  "token_type": "bearer"
}
```

## Contact APIs

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/contacts` | List contacts with search and pagination |
| POST | `/contacts` | Create a contact |
| GET | `/contacts/{id}` | Get one contact |
| PUT | `/contacts/{id}` | Update a contact |
| DELETE | `/contacts/{id}` | Delete a contact |

Protected contact requests require:

```text
Authorization: Bearer <JWT_TOKEN>
```

---

# 6. Application Workflow

## Login

```text
User
  ↓
Vue.js Login Page
  ↓
POST /api/auth/login
  ↓
Spring Security
  ↓
User Authentication
  ↓
JWT Token
  ↓
Frontend
```

## Create Contact

```text
User
  ↓
Vue Contact Form
  ↓
POST /api/contacts
  ↓
Spring Boot
  ↓
ContactService
  ↓
JPA / Hibernate
  ↓
PostgreSQL
```

## Read Contacts

```text
Vue.js
  ↓
GET /api/contacts
  ↓
Spring Boot
  ↓
ContactService
  ↓
PostgreSQL
  ↓
Paginated JSON Response
```

## Update Contact

```text
Contact Detail
  ↓
PUT /api/contacts/{id}
  ↓
Spring Boot
  ↓
ContactService
  ↓
PostgreSQL
```

## Delete Contact

```text
Delete Button
  ↓
DELETE /api/contacts/{id}
  ↓
Spring Boot
  ↓
ContactService
  ↓
PostgreSQL
```

---

# 7. Project Structure

```text
phonebook-java/
│
├── backend-java/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   ├── test/
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   ├── mvnw
│   │   └── mvnw.cmd
│   │
│   └── target/
│
├── frontend/
│   ├── src/
│   ├── package.json
│   ├── vite.config.js
│   └── Dockerfile
│
├── tests/
│   ├── add-200-contacts.spec.js
│   └── example.spec.js
│
├── .github/
│   └── workflows/
│       ├── ci.yml
│       └── playwright.yml
│
├── docker-compose.yml
├── playwright.config.js
├── package.json
├── package-lock.json
├── .env
└── README.md
```

The active backend is **Spring Boot / Java** under `backend-java`.

---

# 8. Docker Compose

The application uses three main services:

| Service | Technology | Port |
|---|---|---|
| frontend | Vue.js / Vite | 5174 |
| backend | Spring Boot / Java 21 | 8000 |
| db | PostgreSQL 16 | 5432 |

The Java backend connects to PostgreSQL using the Docker service name:

```text
db:5432
```

PostgreSQL data is persisted using the named Docker volume:

```text
postgres_data
```

---

# 9. Environment Configuration

Create a local `.env` file:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=YOUR_PASSWORD
POSTGRES_DB=phonebook
```

Do not commit real credentials to GitHub.

---

# 10. Prerequisites

Install:

- Docker Desktop
- Git
- Node.js
- Java 21

Check Docker:

```powershell
docker --version
docker compose version
```

Check Java:

```powershell
java -version
```

Check Node.js:

```powershell
node --version
npm --version
```

---

# 11. Run the Application with Docker

From the project root:

```powershell
docker compose up -d --build
```

Verify the containers:

```powershell
docker compose ps
```

Expected services:

```text
phonebook-frontend
phonebook-backend
phonebook-db
```

View backend logs:

```powershell
docker compose logs -f backend
```

---

# 12. Application URLs

## Local Frontend

```text
http://localhost:5174
```

## Local Backend

```text
http://localhost:8000
```

The backend is a REST API and exposes the controller-defined endpoints listed in the REST API section.

---

# 13. ngrok Public Demo

Start the application first:

```powershell
docker compose up -d --build
```

Start ngrok:

```powershell
ngrok http 5174
```

The current demonstration URL is:

**https://quarry-bankroll-juicy.ngrok-free.dev/**

Open it in a browser:

```text
https://quarry-bankroll-juicy.ngrok-free.dev/
```

### Important

The ngrok URL is temporary.

If ngrok is restarted, the public URL may change. In that case, update the **Live Demo** section of this README with the new URL.

The frontend continues to use the `/api` path and the Vite proxy, so the ngrok URL should not be hard-coded into the frontend API calls.

---

# 14. Testing

The project uses **Playwright** for end-to-end testing.

Tests are located in:

```text
tests/
```

Install dependencies:

```powershell
npm install
```

Install Playwright browsers:

```powershell
npx playwright install
```

Run all Playwright tests:

```powershell
npx playwright test
```

Run the Chromium load test:

```powershell
npx playwright test tests/add-200-contacts.spec.js --project=chromium
```

The `add-200-contacts.spec.js` test creates 200 unique contacts and verifies successful POST responses.

---

# 15. GitHub Actions CI

The project includes GitHub Actions workflows for automated testing.

Workflow files:

```text
.github/workflows/
├── ci.yml
└── playwright.yml
```

The Playwright workflow installs dependencies, installs Playwright browsers, starts the frontend through the Playwright configuration, and runs the automated browser tests.

Playwright reports are uploaded as GitHub Actions artifacts.

---

# 16. Functional Verification

The application has been functionally tested for:

- JWT login
- Authentication
- CORS
- Contact list
- Contact details
- Create contact
- Update contact
- Delete contact
- Search
- Pagination
- Duplicate validation
- PostgreSQL persistence
- Dockerized Java backend
- Frontend integration
- Playwright end-to-end testing
- GitHub Actions CI
- ngrok public access

---

# 17. Database Persistence

Normal shutdown:

```powershell
docker compose down
```

This keeps the PostgreSQL named volume and its stored data.

To remove the PostgreSQL volume:

```powershell
docker compose down -v
```

> **Warning:** `docker compose down -v` deletes the persisted PostgreSQL data.

---

# 18. Migration: Python to Java

The original backend architecture was:

```text
Vue.js
   |
   v
FastAPI / Python
   |
   v
SQLAlchemy
   |
   v
PostgreSQL
```

The active backend architecture is now:

```text
Vue.js
   |
   v
Spring Boot / Java
   |
   v
Spring Security + JWT
   |
   v
Spring Data JPA / Hibernate
   |
   v
PostgreSQL
```

The migration preserves the application's core:

- CRUD functionality
- Authentication
- Search
- Pagination
- PostgreSQL persistence
- Frontend integration

---

# 19. Final Architecture

```text
                         INTERNET
                            |
                            v
                  +---------------------+
                  |   ngrok HTTPS URL   |
                  +----------+----------+
                             |
                             v
                  +---------------------+
                  |   Vue.js Frontend   |
                  |       :5173         |
                  +----------+----------+
                             |
                           Axios
                             |
                             v
                  +---------------------+
                  | Spring Boot Backend |
                  |       :8000         |
                  +----------+----------+
                             |
                   Spring Security + JWT
                             |
                             v
                  +---------------------+
                  | JPA / Hibernate     |
                  +----------+----------+
                             |
                             v
                  +---------------------+
                  |   PostgreSQL 16     |
                  |       :5432         |
                  +---------------------+

                    Docker Compose
                 manages all services
```

---

# 20. Project Status

The Python-to-Java backend migration is complete.

The current project includes:

- Java Spring Boot backend
- Spring Security + JWT authentication
- PostgreSQL database
- Spring Data JPA / Hibernate
- Vue.js frontend
- CRUD operations
- Search
- Pagination
- Docker deployment
- Playwright end-to-end testing
- GitHub Actions CI
- Public ngrok demonstration

The application is ready for local development, testing, Docker deployment, and public demonstration through ngrok.

---
