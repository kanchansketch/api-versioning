# Spring Boot API Versioning

A Spring Boot project demonstrating **API versioning** using **PATH, HEADER, and QUERY strategies** with **Swagger/OpenAPI documentation**.

---

## Features

- Version your APIs with **PATH, HEADER, or QUERY** strategies.
- Multiple versions supported per endpoint.
- Duplicate mapping detection to prevent conflicts.
- Base path configurable via `application.yml`.
- Swagger UI grouped by API version.
- Fully configurable through properties.
- Spring Boot 3 / Spring WebMVC 6 compatible.

---

## Getting Started

### Prerequisites

- Java 17+  
- Maven 3.8+  
- Spring Boot 3.x  

---

### Installation

```bash
git clone <repo-url>
cd spring-boot-api-versioning
mvn clean install
mvn spring-boot:run
