# JavaTask - Task Management API

A RESTful API for task management built with Spring Boot and PostgreSQL.

## Project Specification

### Tech Stack
- **Java Version**: 21
- **Spring Boot**: 4.1.1
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA (Hibernate)
- **Additional Libraries**:
  - Lombok (for reducing boilerplate code)
  - Spring Boot Validation (for request validation)

### Features
- Create, Read, Update, Delete (CRUD) operations for tasks
- Filter tasks by status (TODO, IN_PROGRESS, DONE)
- Automatic timestamp management (createdAt, updatedAt)
- RESTful API design with standardized response format
- Request validation with Jakarta Validation

### Project Structure
```
src/main/java/com/refresh/javatask/
├── JavataskApplication.java          # Main application entry point
├── controller/
│   └── TaskController.java           # REST API endpoints
├── entity/
│   ├── Task.java                     # Task entity with JPA annotations
│   └── APIResponse.java              # Standardized API response wrapper
├── repository/
│   └── TaskRepository.java           # JPA repository interface
├── requestdto/
│   └── TaskRequest.java              # DTO for task creation/update
├── responsedto/
│   └── APIResponse.java              # Standardized API response wrapper
└── service/
    ├── TaskService.java              # Service interface
    └── TaskServiceImpl.java          # Service implementation
```

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** (for building the project)
- **PostgreSQL** (database server)

## Installation

### 1. Clone the Repository
```bash
git clone https://github.com/Kurs24/task-manager.git
cd cd task-manager
```

### 2. Configure Database

Create a PostgreSQL database for the application:
```sql
CREATE DATABASE javatask;
```

### 3. Set Environment Variables

Create a `.env` file in the project root directry and copy this line of code
```bash
cp .env.example .env
```

### 4. Update Database Configuration

The database configuration is in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_DATABASE}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

Ensure PostgreSQL is running on `localhost:5432` or update the URL accordingly.

## Running the Project
**Please make sure that you are running the application from the root directory.**
### Using Maven

1. **Build the project**:
   ```bash
   mvn clean install
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

### Using Java JAR

1. **Build the JAR file**:
   ```bash
   mvn clean package
   ```

2. **Run the JAR**:
   ```bash
   java -jar target/javatask-0.0.1-SNAPSHOT.jar
   ```

The application will start on `http://localhost:8080` by default.

## API Endpoints

Base URL: `http://localhost:8080/api/tasks`

### Ringkasan

| Method | Endpoint | Deskripsi | Sukses | Error |
|--------|----------|-----------|--------|-------|
| `POST` | `/api/tasks` | Membuat task baru | `201 Created` | `400` |
| `GET` | `/api/tasks` | Daftar semua task | `200 OK` | `400` (status tidak valid) |
| `GET` | `/api/tasks?status={status}` | Daftar task berdasarkan status | `200 OK` | `400` (status tidak valid) |
| `GET` | `/api/tasks/{id}` | Detail satu task | `200 OK` | `404` |
| `PUT` | `/api/tasks/{id}` | Mengubah task | `200 OK` | `400`, `404` |
| `DELETE` | `/api/tasks/{id}` | Menghapus task | `204 No Content` | `404` |

### Arti Status Code

| Code | Arti | Contoh penyebab |
|------|------|-----------------|
| `200` | Request berhasil | Task berhasil diambil atau diubah |
| `201` | Resource berhasil dibuat | Task baru dibuat. Header `Location` berisi URL task tersebut |
| `204` | Berhasil, tanpa body | Task berhasil dihapus |
| `400` | Request tidak valid | `title` kosong atau lebih dari 255 karakter, `status` bukan `TODO`/`IN_PROGRESS`/`DONE`, JSON rusak |
| `404` | Task tidak ditemukan | ID tidak ada di database |
| `500` | Kesalahan server | Bug atau database tidak tersedia |

### Aturan Request Body (`POST` dan `PUT`)

| Field | Tipe | Wajib | Aturan |
|-------|------|-------|--------|
| `title` | string | Ya | Tidak boleh kosong, maksimal 255 karakter |
| `description` | string | Tidak | Maksimal 1000 karakter |
| `status` | enum | Ya | `TODO`, `IN_PROGRESS`, atau `DONE` (huruf besar) |
| `dueDate` | datetime | Tidak | Format ISO-8601 tanpa zona, misalnya `2026-12-31T23:59:59` |

`PUT` mengganti seluruh field task (bukan partial update), jadi kirim semua field yang wajib.

### Format Error

> Saat ini response error masih memakai format bawaan Spring Boot. Format error yang seragam (`{ message, data }` atau struktur khusus) akan ditambahkan di Milestone 2 bersama global exception handler.

### Create Task
- **Method**: `POST`
- **Endpoint**: `/api/tasks`
- **Request Body**:
  ```json
  {
    "title": "Complete project documentation",
    "status": "TODO",
    "dueDate": "2024-12-31T23:59:59"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Task successfully created",
    "data": {
      "id": 1,
      "title": "Complete project documentation",
      "status": "TODO",
      "dueDate": "2024-12-31T23:59:59",
      "createdAt": "2024-10-20T10:00:00",
      "updatedAt": "2024-10-20T10:00:00"
    }
  }
  ```

### Get Tasks by Status
- **Method**: `GET`
- **Endpoint**: `/api/tasks?status=TODO`
- **Query Parameters**: `status` (Optional, Will Fetch All Data If not provided) (TODO, IN_PROGRESS, or DONE) 
- **Response**:
  ```json
  {
    "message": "Task successfully retrieved",
    "data": [
      {
        "id": 1,
        "title": "Complete project documentation",
        "status": "TODO",
        "dueDate": "2024-12-31T23:59:59",
        "createdAt": "2024-10-20T10:00:00",
        "updatedAt": "2024-10-20T10:00:00"
      }
    ]
  }
  ```

### Get Task by ID
- **Method**: `GET`
- **Endpoint**: `/api/tasks/{id}`
- **Response**:
  ```json
  {
    "message": "Task successfully retrieved",
    "data": {
      "id": 1,
      "title": "Complete project documentation",
      "status": "TODO",
      "dueDate": "2024-12-31T23:59:59",
      "createdAt": "2024-10-20T10:00:00",
      "updatedAt": "2024-10-20T10:00:00"
    }
  }
  ```

### Update Task
- **Method**: `PUT`
- **Endpoint**: `/api/tasks/{id}`
- **Request Body**:
  ```json
  {
    "title": "Complete project documentation - Updated",
    "status": "IN_PROGRESS",
    "dueDate": "2024-12-31T23:59:59"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Task successfully updated",
    "data": {
      "id": 1,
      "title": "Complete project documentation - Updated",
      "status": "IN_PROGRESS",
      "dueDate": "2024-12-31T23:59:59",
      "createdAt": "2024-10-20T10:00:00",
      "updatedAt": "2024-10-20T11:00:00"
    }
  }
  ```

### Delete Task
- **Method**: `DELETE`
- **Endpoint**: `/api/tasks/{id}`
- **Response**:
  ```json
  {
    "message": "Task successfully deleted",
    "data": 1
  }
  ```

## Task Status Values

- `TODO` - Task not started
- `IN_PROGRESS` - Task currently being worked on
- `DONE` - Task completed

## Database Schema

The application uses JPA with `ddl-auto=create-drop`, which means:
- Tables are automatically created on application startup
- Tables are dropped when the application shuts down

**Task Table Structure**:
- `id` (Long, Primary Key, Auto-generated)
- `title` (String)
- `status` (Enum: TODO, IN_PROGRESS, DONE)
- `dueDate` (LocalDateTime)
- `createdAt` (LocalDateTime)
- `updatedAt` (LocalDateTime)
- `description` (String)

## Testing

This Scope will be provided in next scope

Run tests using Maven:
```bash
mvn test
```

## Quick Test

Pastikan aplikasi sudah berjalan di `http://localhost:8080`, lalu coba perintah berikut secara berurutan.
Opsi `-i` menampilkan status code dan header response.

> **Pengguna Windows:** gunakan **Git Bash**, atau **PowerShell** dengan `curl.exe` (bukan `curl`,
> karena di PowerShell itu alias untuk perintah lain). Di CMD, petik tunggal tidak berfungsi,
> jadi gunakan Postman jika kesulitan.

```bash
# 1. Buat task → 201 Created
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Belajar Spring Boot","description":"Selesaikan Milestone 1","status":"TODO","dueDate":"2026-12-31T23:59:59"}'

# 2. Buat task kedua dengan status berbeda → 201 Created
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Tulis unit test","status":"IN_PROGRESS"}'

# 3. Lihat semua task → 200 OK, berisi 2 task
curl -i http://localhost:8080/api/tasks

# 4. Filter berdasarkan status → 200 OK, hanya task TODO
curl -i "http://localhost:8080/api/tasks?status=TODO"

# 5. Ambil satu task → 200 OK
curl -i http://localhost:8080/api/tasks/1

# 6. Ubah task → 200 OK, updatedAt berubah
curl -i -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Belajar Spring Boot","status":"DONE"}'

# 7. Hapus task → 204 No Content
curl -i -X DELETE http://localhost:8080/api/tasks/2
```

### Skenario Error

```bash
# Title kosong → 400 Bad Request
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"","status":"TODO"}'

# Status tidak valid → 400 Bad Request
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Tes","status":"ABC"}'

# Filter status tidak valid → 400 Bad Request
curl -i "http://localhost:8080/api/tasks?status=ABC"

# Task tidak ada → 404 Not Found
curl -i http://localhost:8080/api/tasks/99999

# Hapus task yang sudah dihapus → 404 Not Found
curl -i -X DELETE http://localhost:8080/api/tasks/2
```

### Uji Persistensi Data

1. Jalankan langkah 1 dan 2 di atas.
2. Hentikan aplikasi, lalu jalankan lagi.
3. `curl http://localhost:8080/api/tasks` → task tadi masih ada.

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database credentials in `.env` file
- Check that the database name matches in both PostgreSQL and `.env`

### Port Already in Use
If port 8080 is already in use, you can change it in `application.properties`:
```properties
server.port=8081
```

### Build Failures
- Ensure Java 21 is installed and configured: `java -version`
- Clean and rebuild: `mvn clean install`
