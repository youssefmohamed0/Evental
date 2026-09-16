# Evental: Event Ticketing & Seat Reservation API

## Project Overview

A production-ready RESTful backend API built with **Spring Boot 3.x** and **PostgreSQL** for an event ticketing and seat reservation system. This project focuses on reliability under high concurrency, robust role-based access control, and clean architecture.

## Core Capabilities & Features

### Authentication & Authorization
- **Role-Based Access Control (RBAC):** Secure access using JWT with support for three roles: `CUSTOMER`, `ORGANIZER`, and `ADMIN`.
- **Token Management:** Endpoints for login, signup, refreshing tokens, and logout.

### Event & Seat Management
- **Event Creation:** Organizers can create events tied to specific venues, configuring pricing and total seat capacity.
- **Seat Generation:** Seats are generated automatically upon event creation and their availability status is exposed in real-time (`AVAILABLE`, `RESERVED`, `BOOKED`).

### Concurrency & Reservation Locking
- **Safe Transactions:** Customers can reserve multiple seats simultaneously. The system uses transactional locking mechanisms (Optimistic versioning on seats) to prevent double-booking.

### Reservation Lifecycle
- **Status Flow:** `PENDING` → `CONFIRMED` or `CANCELLED` / `EXPIRED`.
- **Auto-Expiry:** A background scheduled task (`@Scheduled`) automatically expires unpaid reservations after a configured timeout, freeing up the held seats.

### Payment Processing
- **Simulated Payments:** Only successful payment confirms a reservation and changes seats to the `BOOKED` state.

### Reporting & Management
- **Customer:** View upcoming reservations, booking history, and manage profile details.
- **Organizer:** Track events, monitor reservations per event, and retrieve detailed reservation information.
- **Admin:** System-wide oversight for all events, venues, and reservations, with the ability to override or force-delete records.

## Technology Stack

- **Framework:** Spring Boot 3.x
- **Data Access:** Spring Data JPA, Hibernate
- **Database:** PostgreSQL
- **Security:** Spring Security with JWT
- **API Documentation:** Swagger / OpenAPI 3.1
- **Background Tasks:** Spring `@Scheduled`

## API Endpoints (Summary)

The API is comprehensively documented with OpenAPI (Swagger) and covers the following domains:

* **Authentication (`/api/auth/**`)**: Registration, Login, Token Refresh, and Logout.
* **Public APIs (`/api/events/**`, `/api/venues/**`)**: Browse public events, view venue details, and check seat availability.
* **Customer Portal (`/api/customer/**`)**: Create/cancel reservations, view reservation history, and process payments.
* **Organizer Portal (`/api/organizer/**`)**: Manage hosted events and view related customer reservations.
* **Admin Portal (`/api/admin/**`)**: Global platform administration of venues, events, and reservations.
* **Profile Management (`/api/profile/**`)**: Get and update authenticated user details.

## Domain Model (Database Schema)

The architecture includes the following primary entities:
- **User:** Manages customers, organizers, and admins (includes refresh tokens).
- **Venue:** Represents the physical location where events are hosted.
- **Event:** Associated with a Venue and an Organizer.
- **Seat:** Managed per event, utilizing optimistic locking (`@Version`) for safe concurrent reservations.
- **Reservation & ReservationItem:** Groups multiple booked seats under a specific customer transaction.
- **Payment:** One-to-One mapping with a reservation to track transactional status.

## Getting Started

1. Clone the repository.
2. Ensure you have **Java 17+** and **PostgreSQL** installed.
3. Configure your database credentials in `application.properties` or `application.yml`.
4. Run the application using Maven/Gradle or directly from your IDE.
5. Access the API documentation via Swagger UI (typically at `http://localhost:9090/swagger-ui.html`).
