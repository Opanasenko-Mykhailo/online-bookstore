# Online Bookstore REST API

This is a Spring Boot application for an **Online Bookstore**. It provides CRUD operations for **Books, Authors, and Genres**, along with search functionality. The project uses **Spring Web, Spring Data JPA, Hibernate, MySQL, and Lombok**.

---

## Table of Contents

1. [Technologies](#technologies)
2. [Setup Instructions](#setup-instructions)
3. [Running the Application](#running-the-application)
4. [REST API Endpoints](#rest-api-endpoints)

   * [Books](#books)
   * [Authors](#authors)
   * [Genres](#genres)
5. [Example Requests & Responses](#example-requests--responses)
6. [Feedback](#feedback)

---

## Technologies

* Java 17+
* Spring Boot 3+
* Spring Web
* Spring Data JPA
* Hibernate
* MySQL 8+
* Lombok
* Jakarta Validation
* JUnit 5 + Mockito (unit tests)
* MockMvc (integration tests)

---

## Setup Instructions

1. **Install MySQL** and create a database:

```sql
CREATE DATABASE bookstore_db;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON bookstore_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

2. **Configure `application.properties`:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

3. **Build the project:**

```bash
mvn clean install
```

---

## Running the Application

```bash
mvn spring-boot:run
```

* The server runs on **`http://localhost:8080`**
* Swagger/OpenAPI documentation can be added if needed.

---

## REST API Endpoints

### Books

| Method | Endpoint                     | Description            |
| ------ | ---------------------------- | ---------------------- |
| GET    | `/api/books`                 | Get all books          |
| GET    | `/api/books/{id}`            | Get book by ID         |
| POST   | `/api/books`                 | Create a new book      |
| PUT    | `/api/books/{id}`            | Update book by ID      |
| DELETE | `/api/books/{id}`            | Delete book by ID      |
| GET    | `/api/books/search?title=…`  | Search books by title  |
| GET    | `/api/books/search?author=…` | Search books by author |
| GET    | `/api/books/search?genre=…`  | Search books by genre  |

---

### Authors

| Method | Endpoint            | Description         |
| ------ | ------------------- | ------------------- |
| GET    | `/api/authors`      | Get all authors     |
| GET    | `/api/authors/{id}` | Get author by ID    |
| POST   | `/api/authors`      | Create a new author |
| PUT    | `/api/authors/{id}` | Update author by ID |
| DELETE | `/api/authors/{id}` | Delete author by ID |

---

### Genres

| Method | Endpoint           | Description        |
| ------ | ------------------ | ------------------ |
| GET    | `/api/genres`      | Get all genres     |
| GET    | `/api/genres/{id}` | Get genre by ID    |
| POST   | `/api/genres`      | Create a new genre |
| PUT    | `/api/genres/{id}` | Update genre by ID |
| DELETE | `/api/genres/{id}` | Delete genre by ID |

---

## Example Requests & Responses

### 1. Create a Book

**Request:**

```http
POST /api/books
Content-Type: application/json

{
  "title": "The Hobbit",
  "price": 20.5,
  "quantity": 10,
  "author": { "id": 1 },
  "genre": { "id": 1 }
}
```

**Response:**

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "title": "The Hobbit",
  "price": 20.5,
  "quantity": 10,
  "author": { "id": 1, "name": "J.R.R. Tolkien" },
  "genre": { "id": 1, "name": "Fantasy" }
}
```

---

### 2. Get All Books

**Request:**

```http
GET /api/books
```

**Response:**

```json
[
  {
    "id": 1,
    "title": "The Hobbit",
    "price": 20.5,
    "quantity": 10,
    "author": { "id": 1, "name": "J.R.R. Tolkien" },
    "genre": { "id": 1, "name": "Fantasy" }
  }
]
```

---

### 3. Search Books by Author

**Request:**

```http
GET /api/books/search?author=Tolkien
```

**Response:**

```json
[
  {
    "id": 1,
    "title": "The Hobbit",
    "price": 20.5,
    "quantity": 10,
    "author": { "id": 1, "name": "J.R.R. Tolkien" },
    "genre": { "id": 1, "name": "Fantasy" }
  }
]
```

---

### 4. Validation Error Example

**Request:**

```http
POST /api/books
Content-Type: application/json

{
  "title": "",
  "price": -5,
  "quantity": 0
}
```

**Response:**

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "title": "must not be blank",
  "price": "must be greater than 0",
  "author": "must not be null",
  "genre": "must not be null"
}
```

---

### 5. Not Found Example

**Request:**

```http
GET /api/books/999
```

**Response:**

```http
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "error": "Book not found with id: 999"
}
```

---

## Feedback

* **Ease of Using AI:** Very straightforward; generating boilerplate code for Spring Boot was fast and efficient.
* **Time Spent:** About 2-3 hours to integrate controllers, services, validation, tests, and documentation.
* **Did the Code Work Immediately:** Most of the generated code worked, but minor adjustments were needed for validation handling and proper HTTP status codes.
* **Adjustments Needed:** Added exception handlers, validation annotations, and search query handling.
* **Challenges:** Handling `@Valid` validation errors properly and integrating MockMvc tests with H2/MySQL setup.
* **Useful Prompts Learned:** Asking AI to generate full CRUD, integration tests, unit tests, and example README sections significantly accelerates development.