# University System

A research-oriented university management system with a terminal UI. Supports course registration, grading, research tracking, messaging, tech support, and more.

## Requirements

- **Java 25** or later

## Build

```bash
./mvnw clean package
```

## Run

```bash
java -jar target/university-system-1.0.jar
```

Or compile and run directly:

```bash
./mvnw clean compile
java -cp target/classes university.Main
```

## Quick Start

On first launch, the system seeds itself with mock data (users, courses, research papers, etc.). Use these credentials to log in:

| Role | Email | Password |
|------|-------|----------|
| Admin | alice@uni.edu | admin123 |
| Manager | bob@uni.edu | manager123 |
| Teacher | henry@uni.edu | teacher123 |
| Student | jack@uni.edu | student123 |
| Grad Student | leo@uni.edu | student123 |
| Tech Support | nick@uni.edu | tech123 |

## Features

- **Authentication** — all users log in via email + password
- **Course management** — registration, approvals, grading, transcripts
- **Research** — publish papers, calculate h-index, join projects
- **Messaging** — employees send messages and complaints
- **News & Journals** — subscribe to university journals, comment on news
- **Tech Support** — create, accept, reject, and complete support requests
- **Admin panel** — full user CRUD, system logs
- **Manager panel** — approve enrollments, assign teachers, statistics
- **i18n** — switch between English, Russian, and Kazakh at any time

## Persistence

Data is saved to `university_system.dat` on every logout or menu exit. Delete this file to reset to fresh mock data.
