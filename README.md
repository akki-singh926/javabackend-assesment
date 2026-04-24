# Grid07 Backend Assignment – Social Engagement System

## Overview

This project is a **high-performance backend system** built using **Spring Boot, PostgreSQL, and Redis**, designed to simulate a social platform with **bots and users interacting on posts**.

It focuses on:

* Real-time engagement scoring
* Concurrency-safe operations
* Distributed rate limiting
* Smart notification batching

---

## Key Features

### Phase 1 – Core APIs

* Create Post API
* Add Comment API (User/Bot)
* Clean layered architecture (Controller → Service → Repository)

---

### Phase 2 – Virality Engine (Redis)

Real-time scoring system:

| Action        | Score |
| ------------- | ----- |
| Bot Reply     | +1    |
| Human Like    | +20   |
| Human Comment | +50   |

✔ Implemented using Redis `INCR` for atomic updates
✔ Ensures high performance and thread safety

---

### Phase 2 – Atomic Locks

#### Horizontal Cap

* Max **100 bot replies per post**
* Enforced using Redis atomic counters

#### Vertical Cap

* Max comment depth = **20**

#### Cooldown Cap

* Bot ↔ User interaction limited to **once every 10 minutes**
* Implemented using Redis TTL keys

---

### Phase 3 – Notification Engine

#### Redis Throttling

* If user already notified → store messages in Redis List
* Else → send instant notification + set cooldown

#### CRON Scheduler

* Runs every **5 minutes**
* Aggregates notifications:

  ```
  Bot X and N others interacted with your posts
  ```
* Clears Redis queue after processing

---

### Phase 4 – Testing & Edge Cases

#### Race Condition Test

* Simulated **200 concurrent bot requests**
* System correctly limited to **100 comments**

#### Stateless Architecture

* No in-memory storage used
* All state handled via Redis

#### Data Integrity

* DB writes occur **only after Redis validation**
* Prevents invalid or excessive inserts

---

## Tech Stack

* **Backend:** Spring Boot (Java 17)
* **Database:** PostgreSQL (Docker)
* **Cache & Locks:** Redis (Docker)
* **ORM:** Spring Data JPA
* **Build Tool:** Maven

---

## Project Structure

```
com.grid07_backend
│
├── controller     # REST APIs
├── service        # Business logic
├── repo           # Database access
├── model          # Entities
├── dto            # Request/Response objects
├── scheduler      # CRON jobs
└── config         # Configurations
```

---

## Running the Project

### 1. Start Docker Services

```bash
docker-compose up -d
```

Services:

* PostgreSQL → `localhost:5433`
* Redis → `localhost:6379`

---

### 2. Run Spring Boot App

```bash
Run Grid07BackendApplication
```

---

### 3. API Base URL

```
http://localhost:8080
```

---

## API Endpoints

### - Create Post

```
POST /api/posts
```

### - Add Comment

```
POST /api/posts/{postId}/comments
```

### - Like Post

```
POST /api/posts/{postId}/like
```

---

## Redis Keys Design

| Purpose        | Key Format                          |
| -------------- | ----------------------------------- |
| Virality Score | post:{id}:virality_score            |
| Bot Count      | post:{id}:bot_count                 |
| Cooldown       | cooldown:bot_{botId}:human_{userId} |
| Notifications  | user:{id}:pending_notifs            |

---

## Concurrency Test Example

```bash
for i in {1..200}
do
  curl -X POST http://localhost:8080/api/posts/1/comments \
  -H "Content-Type: application/json" \
  -d '{"authorId":1001,"content":"Bot comment","depthLevel":1}' &
done

wait
```

✔ Expected DB result:

```
100 comments only
```

---

## Design Principles

* **Redis as Gatekeeper** → Handles limits & concurrency
* **PostgreSQL as Source of Truth** → Stores final data
* **Stateless Backend** → No in-memory storage
* **Atomic Operations** → Prevent race conditions

---

## Key Learnings

* Handling high concurrency using Redis
* Designing rate-limited systems
* Implementing distributed locks
* Building scalable notification systems

---

## Future Improvements

* Replace dummy user/bot identification with real mapping
* Add authentication (JWT)
* Use Kafka for event-driven notifications
* Add pagination & feed APIs

---

## Author

**Abhishek Kumar**

---

## Final Note

This project demonstrates **production-level backend engineering concepts**, including:

* Concurrency control
* Distributed system design
* Real-time processing

---
