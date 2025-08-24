~~# Spring Boot API Versioning

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

```~~

Here is the `README.md` content in a plain text block, which should be easy to copy and paste directly into your file.

```
# Spring Boot API Versioning

A **Spring Boot 3** project demonstrating **API versioning** using multiple strategies: **PATH, HEADER, and QUERY**, with **Swagger/OpenAPI documentation** and duplicate mapping detection.

## Table of Contents

* [Features](#features)

* [Architecture & Flow](#architecture--flow)

* [Getting Started](#getting-started)

* [Configuration](#configuration)

* [API Versioning Strategies](#api-versioning-strategies)

* [Controller Example](#controller-example)

* [Swagger / OpenAPI](#swagger--openapi)

* [Duplicate Mapping Detection](#duplicate-mapping-detection)

* [Configuration Properties](#configuration-properties)

* [License](#license)

---

## Features

* Supports multiple API versioning strategies: **PATH**, **HEADER**, **QUERY**

* Multiple versions per endpoint

* Detects duplicate mappings at startup

* Configurable base path (`/api`) for PATH strategy

* Swagger/OpenAPI support with version grouping

* Fully configurable via `application.yml` or `application.properties`

* Spring Boot 3 / Spring WebMVC 6 compatible

* Logs all registered API mappings

---

## Architecture & Flow

```

Client Request
|
v
ApiVersionHandlerMapping

  * Detects @ApiVersion annotation
  * Determines versioning strategy
    |
    v
    Versioned Request Mapping
  * PATH: /api/v{n}/...
  * HEADER: X-API-Version
  * QUERY: ?version=n
    |
    v
    Controller Method
    |
    v
    Response

<!-- end list -->

```

* **PATH strategy**: prepends `/api/v{version}` to controller path.

* **HEADER strategy**: keeps original path; version selected via `X-API-Version` header.

* **QUERY strategy**: keeps original path; version selected via query parameter (`?version=n`).

---

## Getting Started

### Prerequisites

* Java 17+

* Maven 3.8+

* Spring Boot 3.x

### Installation

```

git clone \<repo-url\>
cd spring-boot-api-versioning
mvn clean install
mvn spring-boot:run

```

---

## Configuration (`application.yml`)

```

spring:
application:
name: demo

apiversioning:
strategy: path         \# path | header | query
base-path: /api        \# Base path for PATH versioning
header-name: X-API-Version
query-param: version

springdoc:
group-configs:
\- group: v1
paths-to-match: /api/v1/\*\*
\- group: v2
paths-to-match: /api/v2/\*\*
\- group: v3
paths-to-match: /api/v3/\*\*

```

---

## API Versioning Strategies

### PATH

```

GET /api/v1/users
GET /api/v2/users

```

### HEADER

```

GET /users
Header: X-API-Version: 1

GET /users
Header: X-API-Version: 2

```

### QUERY

```

GET /users?version=1
GET /users?version=2

```

---

## Controller Example

```

@RestController
@RequestMapping("/users")
@ApiVersion({1,2})
public class UserController {

```
@GetMapping
public String getUsers() {
return "Users list v1/v2";
}

@ApiVersion(3)
@GetMapping
public String getUsersV3() {
return "Users list v3";
}

@GetMapping("/test")
public String getUsersTest() {
return "Test endpoint (v1 default)";
}
```

}

```

---

## Swagger / OpenAPI

* Swagger UI: `http://localhost:8080/swagger-ui/index.html`

* OpenAPI JSON docs: `http://localhost:8080/v3/api-docs`

Version-specific Swagger groups:

```

/v3/api-docs/v1
/v3/api-docs/v2
/v3/api-docs/v3

```

> Note: HEADER/QUERY strategy keeps the same path; version is selected via header or query parameter.

---

## Duplicate Mapping Detection

* The application **fails at startup** if the same path + HTTP method + version is registered more than once:

```

IllegalStateException: Duplicate mapping detected: /users already mapped

```

* Ensures safe multi-version API deployment and prevents runtime conflicts.

---

## Configuration Properties

| Property | Description | Default |
| :--- | :--- | :--- |
| `apiversioning.strategy` | API versioning strategy: `path`, `header`, `query` | `path` |
| `apiversioning.base-path` | Base path for PATH strategy | `/api` |
| `apiversioning.header-name` | Header name for HEADER strategy | `X-API-Version` |
| `apiversioning.query-param` | Query parameter for QUERY strategy | `version` |

---

## License

MIT License – Feel free to use, modify, and distribute.
```