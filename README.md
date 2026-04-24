# Grid07 Backend Assignment – Social Engagement System

## Overview

This project is a scalable backend system built using Spring Boot, PostgreSQL, and Redis. It simulates a social engagement platform where users and bots interact with posts in real time.

The system focuses on:

* Real-time engagement scoring
* Concurrency-safe operations
* Distributed rate limiting
* Efficient notification batching

---

## Key Features

### Phase 1 – Core APIs

* Create Post API
* Add Comment API (User/Bot)
* Clean layered architecture (Controller → Service → Repository)

---

### Phase 2 – Virality Engine (Redis)

A real-time scoring system based on interactions:

| Action        | Score |
| ------------- | ----- |
| Bot Reply     | +1    |
| Human Like    | +20   |
| Human Comment | +50   |

* Implemented using Redis INCR for atomic updates
* Ensures high performance and thread safety

---

### Phase 2 – Constraints & Controls

#### Horizontal Limit

* Maximum 100 bot replies per post
* Enforced using Redis atomic counters

#### Vertical Limit

* Maximum comment depth = 20

#### Cooldown Mechanism

* Bot ↔ User interaction allowed once every 10 minutes
* Implemented using Redis TTL keys

---

### Phase 3 – Notification Engine

#### Redis Throttling

* If user already notified → store messages in Redis List
* Else → send instant notification and set cooldown

#### Scheduled Aggregation

* CRON job runs every 5 minutes
* Aggregates notifications:

Bot X and N others interacted with your post

* Clears Redis queue after processing

---

### Phase 4 – Testing & Edge Cases

#### Concurrency Testing

* Simulated 200 concurrent bot requests
* System correctly limited to 100 comments

#### Stateless Architecture

* No in-memory storage used
* All state handled via Redis

#### Data Integrity

* Database writes occur only after Redis validation
* Prevents invalid or excessive inserts

---

## Tech Stack

* Backend: Spring Boot (Java 17)
* Database: PostgreSQL (Docker)
* Cache & Locks: Redis (Docker)
* ORM: Spring Data JPA
* Build Tool: Maven

---

## Project Structure

com.grid07_backend

├── controller     # REST APIs
├── service        # Business logic
├── repo           # Database access
├── model          # Entities
├── dto            # Request/Response objects
├── scheduler      # CRON jobs
└── config         # Configurations

---

## Running the Project

### 1. Start Docker Services

docker-compose up -d

Services:

* PostgreSQL → localhost:5433
* Redis → localhost:6379

---

### 2. Run Spring Boot App

Run the main class:
Grid07BackendApplication

---

### 3. API Base URL

http://localhost:8080

---

## API Endpoints

Create Post
POST /api/posts

Add Comment
POST /api/posts/{postId}/comments

Like Post
POST /api/posts/{postId}/like

---

## Redis Key Design

| Purpose        | Key Format                         |
| -------------- | ---------------------------------- |
| Virality Score | post:{id}:virality_score           |
| Bot Count      | post:{id}:bot_count                |
| Cooldown       | cooldown:bot_{botId}:user_{userId} |
| Notifications  | user:{id}:pending_notifications    |

---

## Concurrency Test Example

for i in {1..200}
do
curl -X POST http://localhost:8080/api/posts/1/comments 
-H "Content-Type: application/json" 
-d '{"authorId":1001,"content":"Bot comment","depthLevel":1}' &
done

wait

Expected DB result:
100 comments only

---

## Design Principles

* Redis as Control Layer → Handles concurrency & rate limits
* PostgreSQL as Source of Truth → Stores final data
* Stateless Backend → No in-memory storage
* Atomic Operations → Prevent race conditions

---

## Key Learnings

* Handling high concurrency using Redis
* Designing rate-limited systems
* Implementing distributed locks
* Building scalable notification systems

---

## Future Improvements

* Add proper user/bot identity mapping
* Implement authentication (JWT)
* Introduce Kafka for event-driven notifications
* Add pagination and feed APIs

---

## Author

Akash Kumar

---

## Final Note

This project demonstrates production-level backend engineering concepts including concurrency control, distributed system design, and real-time processing.
