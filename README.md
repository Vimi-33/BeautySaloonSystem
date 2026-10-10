# Beauty Salon Appointment Booking System

A full-stack web application for booking and managing beauty salon appointments, built with **Spring Boot**, **Thymeleaf**, **Spring Security**, and **MySQL**.

The system enables customers to browse salon services, pick preferred time slots, and schedule appointments online, while providing administrators with a dedicated dashboard to manage bookings, services, and schedules.

---

## Features

### Customer Portal
- **User Authentication**: Secure registration and login using Spring Security.
- **Service Browsing**: Explore beauty treatments, pricing, and estimated durations.
- **Online Booking**: # Beauty Salon Appointment Booking System

A full-stack web application for booking and managing beauty salon appointments, built with **Spring Boot**, **Thymeleaf**, **Spring Security**, and **MySQL**.

The system enables customers to browse salon services, pick preferred time slots, and schedule appointments online, while providing administrators with a dedicated dashboard to manage bookings, services, and schedules.

---

## Features

### Customer Portal
- **User Authentication**: Secure registration and login using Spring Security.
- **Service Browsing**: Explore beauty treatments, pricing, and estimated durations.
- **Online Booking**: Schedule appointments with automatic time-slot availability checks.
- **Booking History**: View past and upcoming appointments.

### Admin Dashboard
- **Appointment Management**: View, approve, reschedule, or cancel bookings.
- **Service Management**: Add, update, or remove salon services and pricing.
- **Role-Based Access**: Role-restricted views for `ROLE_USER` and `ROLE_ADMIN`.

---

## Tech Stack

- **Backend**: Java 17+, Spring Boot (Spring MVC, Spring Data JPA, Spring Security)
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap / Tailwind CSS
- **Database**: MySQL
- **Database Migration**: Flyway
- **Build Tool**: Maven

---

## Database Schema Overview

The core relational structure includes:
- **`users`**: System users, password hashes, and assigned roles.
- **`services`**: Salon offerings (name, description, duration, price).
- **`appointments`**: Reservation records (customer ID, service ID, date, time slot, status).

---

## Prerequisites

Make sure you have installed:
- [JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [MySQL Server 8.0+](https://dev.mysql.com/downloads/installer/)
- [Git](https://git-scm.com/)

---

## Getting Started

### 1. Configure Local Database Credentials

Change the USERNAME and PASSWORD in your `.pom` (`pom.xml`) and `.yaml` / `.properties` file to your local database info:

#### In `application.yaml` (or `application.properties`):
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/SaloonManagement?createDatabaseIfNotExist=true
    username: YOUR_LOCAL_MYSQL_USERNAME
    password: YOUR_LOCAL_MYSQL_PASSWORD
