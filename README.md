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

| Action        | Score |
| ------------- | ----- |
| Bot Reply     | +1    |
| Human Like    | +20   |
| Human Comment | +50   |

* Redis INCR used for atomic updates
* Ensures high performance and thread safety

---

### Constraints & Controls

* Max 100 bot replies per post (Redis counter)
* Max comment depth = 20
* Cooldown: Bot ↔ User interaction once per 10 minutes (Redis TTL)

---

### Notification Engine

* Redis List used for batching notifications
* Instant + delayed notification system
* CRON runs every 5 minutes

Example:
Bot X and N others interacted with your post

---

### Testing & Edge Cases

* 200 concurrent requests simulated
* System correctly capped at 100 comments
* Stateless backend (no in-memory storage)
* DB writes only after Redis validation

---

## Tech Stack

* Spring Boot (Java 17)
* PostgreSQL (Docker)
* Redis (Docker)
* Spring Data JPA
* Maven

---

## Project Structure

com.grid07_backend

├── controller
├── service
├── repo
├── model
├── dto
├── scheduler
└── config

---

## Running the Project

### 1. Start Services

docker-compose up -d

* PostgreSQL → localhost:5433
* Redis → localhost:6379

---

### 2. Run Application

Run:
Grid07BackendApplication

---

### 3. Base URL

http://localhost:8080

---

## API Endpoints

POST /api/posts
POST /api/posts/{postId}/comments
POST /api/posts/{postId}/like

---

## API Testing (Postman Collection)

You can directly test all APIs using the provided Postman collection.

📌 Collection includes:

### 1. Create Post

POST http://localhost:8080/api/posts

Body:
{
"authorId": 1,
"content": "Test Post"
}

---

### 2. Add Human Comment

POST http://localhost:8080/api/posts/1/comments

Body:
{
"authorId": 1,
"content": "Human comment",
"depthLevel": 1
}

---

### 3. Add Bot Comment

POST http://localhost:8080/api/posts/1/comments

Body:
{
"authorId": 1001,
"content": "Bot comment",
"depthLevel": 1
}

---

### 4. Like Post

POST http://localhost:8080/api/posts/1/like

Body:
{
"authorId": 1,
"content": "Human comment",
"depthLevel": 1
}

---

Full collection exported from Postman:


---

## Redis Key Design

| Purpose        | Key Format                         |
| -------------- | ---------------------------------- |
| Virality Score | post:{id}:virality_score           |
| Bot Count      | post:{id}:bot_count                |
| Cooldown       | cooldown:bot_{botId}:user_{userId} |
| Notifications  | user:{id}:pending_notifications    |

---

## Concurrency Test

for i in {1..200}
do
curl -X POST http://localhost:8080/api/posts/1/comments &
done

Expected:
100 comments only

---

## Design Principles

* Redis → concurrency & rate limiting
* PostgreSQL → source of truth
* Stateless backend
* Atomic operations

---

## Key Learnings

* High concurrency handling with Redis
* Distributed locking
* Rate-limited system design
* Scalable notification batching

---

## Future Improvements

* JWT authentication
* Kafka integration
* Pagination & feed APIs
* Real user/bot mapping

---

## Author

Akash Kumar

---

## Final Note

This project demonstrates production-level backend concepts including concurrency control, distributed systems, and real-time processing.
