# GlobeTrotter

A full-stack, multi-city trip planning application: a RESTful API built with Spring Boot, paired with a React single-page app frontend. Users plan trips composed of ordered city stops, attach activities (with cost and duration) to each stop, track a running budget against a trip's limit, and publish a read-only public link for others to view — or copy — their itinerary. The whole stack (Postgres, backend, frontend) is containerized for one-command local startup.

## Overview

GlobeTrotter lets a signed-up user build a trip, add one or more **stops** (a city plus a date range), and attach **activities** to each stop (sightseeing, food, transport, etc. with an estimated cost and duration). A `BudgetController` rolls those activity costs up into a per-trip budget summary so the user can see spend against their `budgetLimit` as they plan. Any trip can be shared as a public, read-only link (`/public/{slug}`) that renders without authentication, and other users can "copy" a shared trip into their own account as a starting point for their own itinerary.

## Key Features

- **User Authentication & Authorization** — Stateless JWT auth via Spring Security (`/api/auth/signup`, `/api/auth/login`); BCrypt-hashed passwords; a `JwtAuthenticationFilter` validates the bearer token on every request and a custom `AuthEntryPoint` returns a clean 401 for anonymous access to protected routes.
- **Trip Planning** — Create, update, and delete trips with a name, description, date range, and optional budget limit.
- **Stops & Activities** — Each trip is broken into ordered stops (city + date range); each stop can hold multiple activities, each with its own category, duration, and estimated cost.
- **Budget Tracking** — `GET /api/budgets/trip/{tripId}` aggregates activity costs across a trip's stops so the UI can show spend vs. the trip's budget limit.
- **City Catalog** — A searchable/browsable city catalog (with a "popular cities" endpoint) used to build trip stops, seeded with demo data via Flyway.
- **Public Sharing & Trip Copying** — Any trip can be published behind a unique `publicSlug` for anonymous viewing, and any authenticated user can clone a shared trip into their own account via `POST /api/trips/copy/{publicSlug}`.
- **Role-Aware Data Model** — Users carry a `USER`/`ADMIN` role (added via a dedicated Flyway migration) as a foundation for role-gated features.

## Tech Stack

### Backend

- **Language:** Java 17
- **Framework:** Spring Boot 4
- **Database:** PostgreSQL
- **Persistence:** Spring Data JPA & Hibernate
- **Database Migrations:** Flyway
- **Boilerplate Reduction:** Lombok
- **Security:** Spring Security & jjwt (JSON Web Tokens)
- **Build Tool:** Maven (via the included wrapper, `mvnw`)
- **Testing:** JUnit 5, Mockito, and Testcontainers (for real-Postgres integration tests)

### Frontend

- **Library:** React 19 (Vite)
- **Routing:** react-router-dom
- **HTTP:** native `fetch` (no axios) via a small hand-rolled API client
- **State:** React Context (`AuthContext`) + component state — no Redux
- **Styling:** hand-written CSS with a shared design-token stylesheet (no CSS framework)

### Infrastructure

- **Containerization:** Docker & Docker Compose (Postgres + Spring Boot backend + nginx-served frontend)

## Project Structure

Backend (`backend/src/main/java/com/RJR/GlobeTrotter/`) follows a standard layered architecture:

- `controller/` — REST API endpoints handling HTTP requests (`AuthController`, `TripController`, `StopController`, `StopActivityController`, `ActivityController`, `CityController`, `BudgetController`).
- `service/` — Core business logic layer, one service per domain concept.
- `repository/` — Spring Data JPA interfaces for database interaction.
- `entity/` — JPA domain models (`User`, `Trip`, `Stop`, `StopActivity`, `Activity`, `City`, `Role`).
- `dto/request/` & `dto/response/` — Data Transfer Objects for client-server communication.
- `security/` — JWT filter/service, `CustomUserDetailsService`, `CurrentUserProvider`, and the auth entry point.
- `config/` — `SecurityConfig` (filter chain, CORS, password encoding).
- `exception/` — Global exception handling (`GlobalExceptionHandler`, `ResourceNotFoundException`, `EmailAlreadyInUseException`, `InvalidCredentialsException`).

Database migrations (`backend/src/main/resources/db/migration/`) are plain versioned Flyway SQL scripts, from initial table creation through a seed-data migration (`V8__seed_dummy_data.sql`) used for local/demo data.

Frontend (`frontend/src/`):

- `api/` — `client.js` (fetch wrapper + token storage) plus one module per resource: `auth.js`, `trips.js`, `stops.js`, `stopActivities.js`, `activities.js`, `cities.js`.
- `pages/` — Route-level views: `Login`, `Signup`, `Dashboard`, `TripDetails`, `PublicTrip`.
- `components/layout/` — `Navbar` and `Footer`.
- `components/ui/` — Shared UI primitives: `Button`, `Card`, `Input`, `Modal`.
- `context/` — `AuthContext` — token/user persisted to `localStorage`, exposes `isAuthenticated`/`loading`.
- `App.jsx` — Route table: public `/login`, `/signup`, `/public/:slug`; authenticated `/dashboard`, `/trips/:id` behind a `ProtectedRoute` guard.

## Getting Started

### Prerequisites

- JDK 17
- Node.js 20+ (only for running the frontend outside Docker)
- Docker and Docker Compose

### Local Development Setup

**1. Configure environment variables**

Copy the example env files and fill in real values:

```bash
cp .env.example .env
cp backend/.env.example backend/.env
cp frontend/.env.example frontend/.env
```

`JWT_SECRET` is required — the app reads it from `backend/src/main/resources/application.yaml` with no default, so it won't boot without one. Generate a base64-encoded secret:

```bash
openssl rand -base64 64
```

**2. Start the database**

The root `docker-compose.yml` spins up Postgres, publishing it on the host port set by `POSTGRES_PORT` in `.env` (default `5433`, to avoid clashing with a local Postgres install):

```bash
docker-compose up -d postgres
```

If you're running the backend natively (step 3) against this container, make sure `SPRING_DATASOURCE_URL` in your shell matches, e.g. `jdbc:postgresql://localhost:5433/globetrotter`.

**3. Run the backend**

`backend/.env` documents the variables `application.yaml` needs (`JWT_SECRET`, `JWT_EXPIRATION_MS`, `APP_CORS_ALLOWED_ORIGINS`) but, since it isn't auto-loaded by Spring Boot, export them into your shell first:

```powershell
# PowerShell
Get-Content backend/.env | ForEach-Object { if ($_ -match '^([^#=]+)=(.*)$') { Set-Item "env:$($matches[1])" $matches[2] } }
```

```bash
# bash
set -a; source backend/.env; set +a
```

Then start the app with the included Maven wrapper:

```bash
cd backend
./mvnw spring-boot:run
```

The application connects to Postgres, applies Flyway migrations, and starts on port **8080**.

**4. Run the frontend**

In a separate terminal:

```bash
cd frontend
npm install
npm run dev
```

The SPA starts on port **5173** and talks to the backend at the URL configured in `frontend/.env` (`VITE_API_BASE_URL`, default `http://localhost:8080/api`).

### Run via Docker Compose (Full Stack)

Run the database, backend, and frontend together as containers from the project root:

```bash
docker-compose up -d --build
```

- **Backend:** `http://localhost:${BACKEND_PORT}` (default `5000`, mapped to container port 8080)
- **Postgres:** `localhost:${POSTGRES_PORT}` (default `5433`)
- **Frontend:** `http://localhost:${FRONTEND_PORT}` (default `3000`) — served by nginx from the Vite production build

The containerized frontend is built with `VITE_API_BASE_URL` baked in at build time, so double-check `frontend/.env` points at the backend port you intend to use (`5000` for Compose, `8080` for a natively-run backend) before building the image.

## Testing

Backend tests cover controllers, services, DTOs, security components, and exception handling, with Testcontainers available for tests that need a real Postgres instance rather than mocks.

Run the test suite:

```bash
cd backend
./mvnw test
```

## API Documentation

All endpoints are prefixed with `/api`. Endpoints under `/api/auth/**` and `/api/trips/public/**` are publicly accessible; every other route requires a valid `Authorization: Bearer <token>` header.

### Authentication (`/api/auth`)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/signup` | Register a new user account and return a JWT. |
| POST | `/api/auth/login` | Authenticate a user and return a JWT. |

### Trips (`/api/trips`)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/trips/public/{publicSlug}` | View a shared trip by its public slug. *(public)* |
| POST | `/api/trips` | Create a new trip for the authenticated user. |
| GET | `/api/trips` | List trip summaries for the authenticated user. |
| GET | `/api/trips/{tripId}` | Get full details for one of the user's trips. |
| PUT | `/api/trips/{tripId}` | Update a trip's details. |
| DELETE | `/api/trips/{tripId}` | Delete a trip. |
| POST | `/api/trips/{tripId}/share` | Publish the trip and get back its public slug/URL. |
| POST | `/api/trips/copy/{publicSlug}` | Copy a publicly shared trip into the authenticated user's account. |

### Stops (`/api/stops`)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/stops` | Add a stop (city + date range) to a trip. |
| GET | `/api/stops/trip/{tripId}` | List all stops for a trip. |
| GET | `/api/stops/{stopId}` | Get a single stop. |
| PUT | `/api/stops/{stopId}` | Update a stop. |
| DELETE | `/api/stops/{stopId}` | Delete a stop. |

### Stop Activities (`/api/stop-activities`)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/stop-activities` | Attach an activity to a stop. |
| GET | `/api/stop-activities/stop/{stopId}` | List all activities attached to a stop. |
| GET | `/api/stop-activities/{stopActivityId}` | Get a single stop-activity link. |
| PUT | `/api/stop-activities/{stopActivityId}` | Update a stop-activity link (e.g. scheduling, cost override). |
| DELETE | `/api/stop-activities/{stopActivityId}` | Remove an activity from a stop. |

### Activities (`/api/activities`)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/activities` | List available activities. |
| GET | `/api/activities/{activityId}` | Get a single activity. |
| POST | `/api/activities` | Create a new activity. |
| PUT | `/api/activities/{activityId}` | Update an activity. |
| DELETE | `/api/activities/{activityId}` | Delete an activity. |

### Cities (`/api/cities`)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/cities` | List/browse the city catalog. |
| GET | `/api/cities/popular` | List cities ranked by popularity. |
| GET | `/api/cities/{cityId}` | Get a single city. |
| POST | `/api/cities` | Add a city to the catalog. |
| PUT | `/api/cities/{cityId}` | Update a city. |
| DELETE | `/api/cities/{cityId}` | Delete a city. |

### Budgets (`/api/budgets`)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/budgets/trip/{tripId}` | Get an aggregated budget summary (spend vs. limit) for a trip. |
