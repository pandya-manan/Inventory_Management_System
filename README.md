 
# 📦Inventory Management System (Microservices Architecture)

## 📌 Overview

This project is a **microservices-based Inventory Management System** built using Spring Boot.
It demonstrates real-world backend architecture including service communication, fault tolerance, security, and monitoring.

The system allows:

* User authentication (JWT-based)
* Product management
* Inventory tracking
* Order placement and cancellation

---

## 🏗️ Architecture

```
Client → API Gateway → Microservices → Database
```

### 🔹 Services:

* **Auth Service** – Handles user authentication & JWT token generation
* **Product Service** – Manages product catalog
* **Inventory Service** – Tracks stock levels
* **Order Service** – Handles order placement & cancellation
* **API Gateway** – Single entry point for all services
* **Eureka Server** – Service discovery

---

## 🧰 Tech Stack

* **Backend:** Spring Boot, Spring Security
* **Microservices:** Spring Cloud (Eureka, Gateway, OpenFeign)
* **Database:** MySQL
* **Fault Tolerance:** Resilience4j (Circuit Breaker)
* **API Documentation:** Swagger (OpenAPI)
* **Testing:** JUnit, Mockito
* **Monitoring:** Spring Boot Actuator, Micrometer
* **Logging:** SLF4J + Logback

---

## 🔐 Security

* Implemented **JWT-based authentication**
* Stateless session management
* Protected APIs using Spring Security
* Token-based authorization across services

---

## 🔗 Inter-Service Communication

* Used **OpenFeign** for service-to-service calls
* Integrated with **Eureka Service Discovery**
* Removed hardcoded URLs using service names

---

## ⚡ Fault Tolerance

* Implemented **Circuit Breaker (Resilience4j)**
* Applied at external service call level (Feign clients)
* Fallback methods for service failures

---

## 📊 Monitoring & Observability

* Integrated **Spring Boot Actuator**
* Exposed endpoints:

  * `/actuator/health`
  * `/actuator/metrics`
  * `/actuator/prometheus`
* Added **custom business metrics**:

  * Orders created
  * Orders cancelled
  * Inventory updates

---

## 📝 Logging

* Implemented structured logging using **SLF4J**
* Log levels:

  * INFO → Business operations
  * DEBUG → Internal flow
  * ERROR → Failures with stack trace

---

## 🧪 Testing

* Unit testing using **JUnit & Mockito**
* Mocked dependencies (Feign clients, repositories)
* Covered:

  * Success scenarios
  * Exception handling
  * Edge cases

---

## 🔄 Order Flow

1. User places order via API Gateway
2. Order Service validates product (Product Service)
3. Inventory Service reduces stock
4. Order is created and saved

### 🔁 Cancel Order (Compensation Logic)

* Restores inventory
* Updates order status to `CANCELLED`

---

## 📂 Project Structure

```
auth-service/
product-service/
inventory-service/
order-service/
api-gateway/
discovery-server/
```

---

## ▶️ Running the Project

1. Start **Eureka Server**
2. Start all microservices:

   * Auth
   * Product
   * Inventory
   * Order
3. Start **API Gateway**
4. Access APIs via Gateway:

```
http://localhost:8080/
```

---

## 📌 Key Features

✔ Microservices Architecture
✔ JWT Authentication
✔ Service Discovery (Eureka)
✔ API Gateway Routing
✔ Circuit Breaker (Resilience4j)
✔ Inter-service Communication (Feign)
✔ Logging & Monitoring
✔ Unit Testing

---

## 🎯 Future Enhancements

* Docker containerization
* Prometheus & Grafana dashboards
* Distributed tracing (Zipkin)
* Frontend integration (React)

---

## 👨‍💻 Author

**Manan P**
Software Engineer | Spring Boot | Microservices

---

## ⭐ Summary

This project demonstrates a **production-ready microservices backend system** with scalability, resilience, and observability in mind.
